package com.simdea.workoutcounter.data.repository

import com.simdea.workoutcounter.data.local.dao.WorkoutDao
import com.simdea.workoutcounter.data.local.entity.WorkoutEntity
import com.simdea.workoutcounter.domain.model.WorkoutStats
import com.simdea.workoutcounter.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDate

class WorkoutRepositoryImplTest {

    @Mock
    private lateinit var workoutDao: WorkoutDao

    @Mock
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var repository: WorkoutRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = WorkoutRepositoryImpl(workoutDao, settingsRepository)
    }

    @Test
    fun `getWorkoutStats calculates correct stats based on settings`() = runTest {
        // Given
        val startDate = LocalDate.now().minusWeeks(2) // 2 weeks ago
        val workoutsPerWeek = 2
        val totalDone = 3

        `when`(settingsRepository.startDate).thenReturn(flowOf(startDate))
        `when`(settingsRepository.workoutsPerWeek).thenReturn(flowOf(workoutsPerWeek))
        `when`(workoutDao.getWorkoutCount()).thenReturn(flowOf(totalDone))

        // When
        val stats = repository.getWorkoutStats().first()

        // Then
        // Weeks passed: 2 weeks diff + 1 (current week) = 3 weeks
        // Total required: 3 weeks * 2 workouts/week = 6
        // To compensate: 6 - 3 = 3

        // Let's verify calculation logic in impl:
        // ChronoUnit.WEEKS.between(startDate, now) for exactly 2 weeks ago is 2.
        // + 1 = 3 weeks.

        assertEquals(3, stats.totalDone)
        assertEquals(6, stats.totalRequired)
        assertEquals(3, stats.toCompensate)
    }

    @Test
    fun `getWorkoutStats returns zero required if start date is in future`() = runTest {
        // Given
        val startDate = LocalDate.now().plusDays(1)
        val workoutsPerWeek = 3
        val totalDone = 0

        `when`(settingsRepository.startDate).thenReturn(flowOf(startDate))
        `when`(settingsRepository.workoutsPerWeek).thenReturn(flowOf(workoutsPerWeek))
        `when`(workoutDao.getWorkoutCount()).thenReturn(flowOf(totalDone))

        // When
        val stats = repository.getWorkoutStats().first()

        // Then
        assertEquals(0, stats.totalRequired)
        assertEquals(0, stats.toCompensate)
    }
}
