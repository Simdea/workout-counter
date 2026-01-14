package com.simdea.workoutcounter.data.repository

import com.simdea.workoutcounter.data.local.dao.WorkoutDao
import com.simdea.workoutcounter.data.local.entity.WorkoutEntity
import com.simdea.workoutcounter.domain.model.WorkoutStats
import com.simdea.workoutcounter.domain.repository.SettingsRepository
import com.simdea.workoutcounter.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val settingsRepository: SettingsRepository
) : WorkoutRepository {

    override fun getWorkoutStats(): Flow<WorkoutStats> {
        return combine(
            workoutDao.getWorkoutCount(),
            settingsRepository.startDate,
            settingsRepository.workoutsPerWeek
        ) { count, startDate, workoutsPerWeek ->
            val now = LocalDate.now()
            val weeksPassed = if (now.isBefore(startDate)) {
                0
            } else {
                ChronoUnit.WEEKS.between(startDate, now).toInt() + 1
            }

            val totalRequired = weeksPassed * workoutsPerWeek

            WorkoutStats(
                totalDone = count,
                totalRequired = totalRequired,
                toCompensate = (totalRequired - count).coerceAtLeast(0)
            )
        }
    }

    override fun getAllWorkouts(): Flow<List<WorkoutEntity>> {
        return workoutDao.getAllWorkouts()
    }

    override suspend fun logWorkout(date: OffsetDateTime) {
        workoutDao.insertWorkout(WorkoutEntity(date = date))
    }

    override suspend fun deleteWorkout(workout: WorkoutEntity) {
        workoutDao.deleteWorkout(workout)
    }

    override suspend fun updateWorkout(workout: WorkoutEntity) {
        workoutDao.updateWorkout(workout)
    }
}
