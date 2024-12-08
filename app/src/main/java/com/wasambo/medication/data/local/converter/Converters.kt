package com.wasambo.medication.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.wasambo.medication.data.model.Problems

class Converters {
    @TypeConverter
    fun fromProblem(problem: Problems): String {
        return Gson().toJson(problem)
    }

    @TypeConverter
    fun toProblem(json: String): Problems {
        return Gson().fromJson(json, Problems::class.java)
    }
} 