package com.simdea.workoutcounter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simdea.workoutcounter.data.local.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @androidx.room.Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    @androidx.room.Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<WorkoutEntity>)

    @Query("DELETE FROM workouts")
    suspend fun deleteAllWorkouts()

    @androidx.room.Transaction
    suspend fun replaceWorkouts(workouts: List<WorkoutEntity>) {
        deleteAllWorkouts()
        insertAll(workouts)
    }

    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>

    @Query("SELECT COUNT(*) FROM workouts")
    fun getWorkoutCount(): Flow<Int>
}
