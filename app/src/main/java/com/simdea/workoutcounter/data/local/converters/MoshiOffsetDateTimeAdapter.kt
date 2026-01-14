package com.simdea.workoutcounter.data.local.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class MoshiOffsetDateTimeAdapter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @ToJson
    fun toJson(value: OffsetDateTime): String {
        return value.format(formatter)
    }

    @FromJson
    fun fromJson(value: String): OffsetDateTime {
        return formatter.parse(value, OffsetDateTime::from)
    }
}
