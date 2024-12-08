package com.wasambo.medication.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wasambo.medication.data.datasource.local.dao.MedicalDataDao
import com.wasambo.medication.data.local.converter.Converters
import com.wasambo.medication.data.local.dao.DrugDao
import com.wasambo.medication.data.local.entity.DrugEntity
import com.wasambo.medication.data.local.entity.ProblemDrugCrossRef
import com.wasambo.medication.data.local.entity.ProblemEntity

@TypeConverters(Converters::class)
@Database(version = 1, entities = [ProblemEntity::class, ProblemDrugCrossRef::class, DrugEntity::class], exportSchema = false)
abstract class MedicalDatabase : RoomDatabase() {
    abstract fun getMedicalDataDao(): MedicalDataDao
    abstract fun getDrugDao(): DrugDao
}