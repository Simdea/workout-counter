package com.simdea.workoutcounter.ui

import com.simdea.workoutcounter.domain.model.WorkoutStats

sealed interface WorkoutUiState {
    object Loading : WorkoutUiState
    data class Success(val stats: WorkoutStats) : WorkoutUiState
    data class Error(val message: String) : WorkoutUiState
}
