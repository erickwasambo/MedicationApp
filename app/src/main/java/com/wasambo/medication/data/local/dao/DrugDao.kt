package com.wasambo.medication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wasambo.medication.data.local.entity.DrugEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrugDao {
    @Query("SELECT * FROM drugs")
    fun getAllDrugs(): Flow<List<DrugEntity>>

    @Query("SELECT * FROM drugs WHERE name LIKE :name")
    fun findDrugByName(name: String): Flow<DrugEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrugs(drugs: List<DrugEntity>)

    @Query("DELETE FROM drugs")
    suspend fun deleteAllDrugs()

    @Query("""
        SELECT d.* FROM drugs d
        INNER JOIN problem_drug_cross_ref pd ON d.id = pd.drugId
        WHERE pd.problemId = :problemId
    """)
    fun getDrugsByProblem(problemId: Long): Flow<List<DrugEntity>>
} 