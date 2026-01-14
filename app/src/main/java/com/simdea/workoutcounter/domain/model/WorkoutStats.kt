package com.simdea.workoutcounter.domain.model

data class WorkoutStats(
    val totalDone: Int,
    val totalRequired: Int,
    val toCompensate: Int
)
