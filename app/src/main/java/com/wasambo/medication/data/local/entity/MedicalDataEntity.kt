package com.wasambo.medication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.wasambo.medication.data.local.converter.Converters
import com.wasambo.medication.data.model.Problems

@Entity(tableName = "medical_data")
@TypeConverters(Converters::class)
data class MedicalDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val problems: List<Problems>
) 