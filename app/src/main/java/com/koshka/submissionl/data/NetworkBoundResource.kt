package com.koshka.submissionl.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.koshka.submissionl.data.remote.response.ApiResponse
import com.koshka.submissionl.data.remote.response.StatusResponse
import com.koshka.submissionl.utils.AppExecutors
import com.koshka.submissionl.vo.Resource

abstract class NetworkBoundResource<ResultType, RequestType>(private val mExecutors: AppExecutors) {

    // data entity
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {

        // set the loading data is null
        result.value = Resource.loading(null)

        @Suppress("LeakingThis")
        // database Source there is we want to use
        val dbSource = loadFromDb()

        //add the source
        result.addSource(dbSource) { data ->

            // remove the source
            result.removeSource(dbSource)

            //check fetch data
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {

                // add the source and get the local data
                result.addSource(dbSource) { newData ->
                    result.value = Resource.success(newData)
                }
            }
        }
    }

    // check is the data need to fetch or not
    abstract fun shouldFetch(data: ResultType?): Boolean

    // access the data from local database
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // to access remote data source
    abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    // save data from remote database to local database
    abstract fun saveCallResult(data: RequestType?)

    // idk either
    private fun onFetchFailed() {}

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        //Api Response instance
        val apiResponse = createCall()

        // add source of data entity with database source
        result.addSource(dbSource) { newData ->
            result.value = Resource.loading(newData)
        }

        // add source of data entity with api source
        result.addSource(apiResponse) { response ->

            //remove all source
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            when (response.status) {
                StatusResponse.SUCCESS ->

                    mExecutors.diskIO().execute {

                        // add the data that we got from remote to local database
                        saveCallResult(response.body)

                        // work in main thread
                        mExecutors.mainThread().execute {

                            // add source with local database instead remote data
                            result.addSource(loadFromDb()) { newData ->
                                result.value = Resource.success(newData)
                            }
                        }
                    }

                StatusResponse.EMPTY -> mExecutors.mainThread().execute {
                    result.addSource(dbSource) { newData ->
                        result.value = Resource.success(newData)
                    }
                }

                StatusResponse.ERROR -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        // send message and data
                        result.value = Resource.error(response.message, newData)
                    }
                }
            }
        }
    }

    fun asLiveData(): LiveData<Resource<ResultType>> = result
}
