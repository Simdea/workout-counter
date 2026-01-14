package com.simdea.workoutcounter.di

import android.content.Context
import androidx.room.Room
import com.simdea.workoutcounter.data.local.AppDatabase
import com.simdea.workoutcounter.data.local.dao.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "workout_counter_db"
        ).build()
    }

    @Provides
    fun provideWorkoutDao(database: AppDatabase): WorkoutDao {
        return database.workoutDao()
    }
}
