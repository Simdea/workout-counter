package com.simdea.workoutcounter.di

import com.simdea.workoutcounter.data.repository.SettingsRepositoryImpl
import com.simdea.workoutcounter.data.repository.WorkoutRepositoryImpl
import com.simdea.workoutcounter.domain.repository.SettingsRepository
import com.simdea.workoutcounter.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(
        workoutRepositoryImpl: WorkoutRepositoryImpl
    ): WorkoutRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}
