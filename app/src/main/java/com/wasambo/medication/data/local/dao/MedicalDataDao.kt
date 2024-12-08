package com.wasambo.medication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wasambo.medication.data.local.entity.MedicalDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalData(medicalData: MedicalDataEntity)

    @Query("SELECT * FROM medical_data")
    fun getAllMedicalData(): Flow<List<MedicalDataEntity>>

    @Query("DELETE FROM medical_data")
    suspend fun deleteAllMedicalData()
} 