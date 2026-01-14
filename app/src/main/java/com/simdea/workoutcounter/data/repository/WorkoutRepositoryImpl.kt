package com.simdea.workoutcounter.data.repository

import com.simdea.workoutcounter.data.local.dao.WorkoutDao
import com.simdea.workoutcounter.data.local.entity.WorkoutEntity
import com.simdea.workoutcounter.domain.model.WorkoutStats
import com.simdea.workoutcounter.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.Period
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {

    private val startDate = LocalDate.of(2023, 11, 20)

    override fun getWorkoutStats(): Flow<WorkoutStats> {
        return workoutDao.getWorkoutCount().map { count ->
            val now = LocalDate.now()
            val weeksPassed = if (now.isBefore(startDate)) {
                0
            } else {
                ChronoUnit.WEEKS.between(startDate, now).toInt() + 1
            }
            
            WorkoutStats(
                totalDone = count,
                totalRequired = weeksPassed,
                toCompensate = (weeksPassed - count).coerceAtLeast(0)
            )
        }
    }

    override suspend fun logWorkout(date: OffsetDateTime) {
        workoutDao.insertWorkout(WorkoutEntity(date = date))
    }
}
