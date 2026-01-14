package com.simdea.workoutcounter.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.simdea.workoutcounter.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private object PreferencesKeys {
        val START_DATE_EPOCH_DAY = longPreferencesKey("start_date_epoch_day")
        val WORKOUTS_PER_WEEK = intPreferencesKey("workouts_per_week")
    }

    // Default start date: 2023-11-20
    private val defaultStartDate = LocalDate.of(2023, 11, 20)
    private val defaultWorkoutsPerWeek = 1

    override val startDate: Flow<LocalDate> = context.dataStore.data.map { preferences ->
        val epochDay = preferences[PreferencesKeys.START_DATE_EPOCH_DAY]
        if (epochDay != null) {
            LocalDate.ofEpochDay(epochDay)
        } else {
            defaultStartDate
        }
    }

    override val workoutsPerWeek: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.WORKOUTS_PER_WEEK] ?: defaultWorkoutsPerWeek
    }

    override suspend fun setStartDate(date: LocalDate) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.START_DATE_EPOCH_DAY] = date.toEpochDay()
        }
    }

    override suspend fun setWorkoutsPerWeek(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WORKOUTS_PER_WEEK] = count
        }
    }
}
