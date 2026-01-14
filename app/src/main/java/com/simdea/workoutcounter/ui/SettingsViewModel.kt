package com.simdea.workoutcounter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simdea.workoutcounter.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class SettingsUiState(
    val startDate: LocalDate = LocalDate.now(),
    val workoutsPerWeek: Int = 1
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

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
}
