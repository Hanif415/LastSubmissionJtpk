package com.koshka.submissionl.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koshka.submissionl.data.local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class TheMovieDbDatabase : RoomDatabase() {
    abstract fun theMovieDbDao(): TheMovieDbDao

    companion object {
        @Volatile
        private var INSTANCE: TheMovieDbDatabase? = null

        fun getInstance(context: Context): TheMovieDbDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TheMovieDbDatabase::class.java,
                    "theMovieDb.db"
                ).build().apply {
                    INSTANCE = this
                }
            }
    }
}