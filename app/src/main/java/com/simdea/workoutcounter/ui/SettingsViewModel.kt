package com.simdea.workoutcounter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simdea.workoutcounter.data.local.converters.MoshiOffsetDateTimeAdapter
import com.simdea.workoutcounter.domain.model.BackupData
import com.simdea.workoutcounter.domain.repository.SettingsRepository
import com.simdea.workoutcounter.domain.repository.WorkoutRepository
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDate
import javax.inject.Inject

data class SettingsUiState(
    val startDate: LocalDate = LocalDate.now(),
    val workoutsPerWeek: Int = 1,
    val isLoading: Boolean = false,
    val message: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val moshi = Moshi.Builder()
        .add(MoshiOffsetDateTimeAdapter())
        .build()
    private val backupAdapter = moshi.adapter(BackupData::class.java)

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.startDate,
        settingsRepository.workoutsPerWeek
    ) { startDate, workoutsPerWeek ->
        SettingsUiState(
            startDate = startDate,
            workoutsPerWeek = workoutsPerWeek
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun updateStartDate(date: LocalDate) {
        viewModelScope.launch {
            settingsRepository.setStartDate(date)
        }
    }

    fun updateWorkoutsPerWeek(count: Int) {
        viewModelScope.launch {
            settingsRepository.setWorkoutsPerWeek(count)
        }
    }

    fun exportData(outputStream: OutputStream) {
        viewModelScope.launch {
            try {
                val workouts = workoutRepository.getAllWorkouts().first()
                val currentState = uiState.value
                val backupData = BackupData(
                    workouts = workouts,
                    startDateEpochDay = currentState.startDate.toEpochDay(),
                    workoutsPerWeek = currentState.workoutsPerWeek
                )

                withContext(Dispatchers.IO) {
                    val json = backupAdapter.toJson(backupData)
                    outputStream.use { it.write(json.toByteArray()) }
                }
            } catch (e: Exception) {
                // In a real app, update state with error message
            }
        }
    }

    fun importData(inputStream: InputStream) {
        viewModelScope.launch {
            try {
                val json = withContext(Dispatchers.IO) {
                    inputStream.use { it.readBytes().toString(Charsets.UTF_8) }
                }
                val backupData = backupAdapter.fromJson(json)

                if (backupData != null) {
                    // Update Settings
                    settingsRepository.setStartDate(LocalDate.ofEpochDay(backupData.startDateEpochDay))
                    settingsRepository.setWorkoutsPerWeek(backupData.workoutsPerWeek)

                    // Update Workouts
                    workoutRepository.replaceWorkouts(backupData.workouts)
                }
            } catch (e: Exception) {
                // In a real app, update state with error message
            }
        }
    }
}
