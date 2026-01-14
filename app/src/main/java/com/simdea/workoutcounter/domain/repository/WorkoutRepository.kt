package com.simdea.workoutcounter.domain.repository

import com.simdea.workoutcounter.domain.model.WorkoutStats
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface WorkoutRepository {
    fun getWorkoutStats(): Flow<WorkoutStats>
    suspend fun logWorkout(date: OffsetDateTime = OffsetDateTime.now())
}
