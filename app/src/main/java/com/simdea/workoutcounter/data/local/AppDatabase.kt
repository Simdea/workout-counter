package com.simdea.workoutcounter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simdea.workoutcounter.data.local.converters.DateConverters
import com.simdea.workoutcounter.data.local.dao.WorkoutDao
import com.simdea.workoutcounter.data.local.entity.WorkoutEntity

@Database(entities = [WorkoutEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}
