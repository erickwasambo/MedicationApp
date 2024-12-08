package com.wasambo.medication.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wasambo.medication.data.local.entity.DrugEntity
import com.wasambo.medication.data.local.entity.ProblemDrugCrossRef
import com.wasambo.medication.data.local.entity.ProblemEntity

@Dao
interface MedicalDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProblem(problem: ProblemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrug(drug: DrugEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProblemDrugCrossRef(crossRef: ProblemDrugCrossRef)

    @Transaction
    suspend fun insertProblemWithDrugs(
        problem: ProblemEntity,
        drugs: List<DrugEntity>,
        className: String
    ) {
        val problemId = insertProblem(problem)
        drugs.forEach { drug ->
            val drugId = insertDrug(drug)
            insertProblemDrugCrossRef(
                ProblemDrugCrossRef(
                    problemId = problemId,
                    drugId = drugId,
                    className = className
                )
            )
        }
    }

    @Transaction
    @Query("""
        SELECT p.*, d.* 
        FROM problems p
        LEFT JOIN problem_drug_cross_ref pd ON p.id = pd.problemId
        LEFT JOIN drugs d ON pd.drugId = d.id
    """)
    suspend fun getProblemsWithDrugs(): Map<ProblemEntity, List<DrugEntity>>

    @Query("SELECT DISTINCT className FROM problem_drug_cross_ref WHERE problemId = :problemId")
    suspend fun getProblemClasses(problemId: Long): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrugs(drugs: List<DrugEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProblemDrugCrossRefs(crossRefs: List<ProblemDrugCrossRef>)
} 