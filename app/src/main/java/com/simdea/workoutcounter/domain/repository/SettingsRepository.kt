package com.simdea.workoutcounter.domain.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SettingsRepository {
    val startDate: Flow<LocalDate>
    val workoutsPerWeek: Flow<Int>

    suspend fun setStartDate(date: LocalDate)
    suspend fun setWorkoutsPerWeek(count: Int)
}
