package com.simdea.workoutcounter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simdea.workoutcounter.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    val uiState: StateFlow<WorkoutUiState> = repository.getWorkoutStats()
        .map<_, WorkoutUiState> { stats -> WorkoutUiState.Success(stats) }
        .catch { e -> emit(WorkoutUiState.Error(e.message ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WorkoutUiState.Loading
        )

    fun logWorkout() {
        viewModelScope.launch {
            try {
                repository.logWorkout(OffsetDateTime.now())
            } catch (e: Exception) {
                // Error handling could be implemented via a separate SharedFlow for one-time events
            }
        }
    }
}
