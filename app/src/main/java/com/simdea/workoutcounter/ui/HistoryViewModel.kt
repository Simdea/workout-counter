package com.simdea.workoutcounter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simdea.workoutcounter.data.local.entity.WorkoutEntity
import com.simdea.workoutcounter.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

sealed interface HistoryUiState {
    object Loading : HistoryUiState
    data class Success(val workouts: List<WorkoutEntity>) : HistoryUiState
    data class Error(val message: String) : HistoryUiState
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = repository.getAllWorkouts()
        .map { HistoryUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryUiState.Loading
        )

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }

    fun updateWorkoutDate(workout: WorkoutEntity, newDate: OffsetDateTime) {
        viewModelScope.launch {
            repository.updateWorkout(workout.copy(date = newDate))
        }
    }
}
