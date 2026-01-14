package com.simdea.workoutcounter.domain.model

import com.simdea.workoutcounter.data.local.entity.WorkoutEntity
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class BackupData(
    val workouts: List<WorkoutEntity>,
    val startDateEpochDay: Long,
    val workoutsPerWeek: Int
)
