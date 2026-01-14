package com.simdea.workoutcounter.domain.repository

import com.simdea.workoutcounter.data.local.entity.WorkoutEntity
import com.simdea.workoutcounter.domain.model.WorkoutStats
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface WorkoutRepository {
    fun getWorkoutStats(): Flow<WorkoutStats>
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>
    suspend fun logWorkout(date: OffsetDateTime = OffsetDateTime.now())
    suspend fun deleteWorkout(workout: WorkoutEntity)
    suspend fun updateWorkout(workout: WorkoutEntity)
    suspend fun deleteAllWorkouts()
    suspend fun insertAllWorkouts(workouts: List<WorkoutEntity>)
    suspend fun replaceWorkouts(workouts: List<WorkoutEntity>)
}
