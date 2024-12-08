package com.wasambo.medication.data.repository.local.medical

import com.wasambo.medication.data.datasource.local.dao.MedicalDataDao
import com.wasambo.medication.data.local.dao.DrugDao
import com.wasambo.medication.data.local.entity.DrugEntity
import com.wasambo.medication.data.model.Drug
import com.wasambo.medication.data.model.DrugClass
import com.wasambo.medication.data.model.Lab
import com.wasambo.medication.data.model.Medication
import com.wasambo.medication.data.model.MedicationClass
import com.wasambo.medication.data.model.ProblemData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalMedicalRepository @Inject constructor(
    private val medicalDataDao: MedicalDataDao,
    private val drugDao: DrugDao
) {

    suspend fun findDrugByName(name: String): Flow<DrugEntity> {
        return drugDao.findDrugByName(name)
    }

    suspend fun getProblems(): Map<String, List<ProblemData>> {
        val problemsWithDrugs = medicalDataDao.getProblemsWithDrugs()

        return problemsWithDrugs.map { (problem, drugs) ->
            val medications = drugs.groupBy { drug ->
                medicalDataDao.getProblemClasses(problem.id).first()
            }.map { (className, drugList) ->
                Medication(
                    medicationsClasses = listOf(
                        MedicationClass(
                            className = listOf(
                                DrugClass(
                                    associatedDrug = drugList.map { drug ->
                                        Drug(
                                            name = drug.name,
                                            dose = drug.dose,
                                            strength = drug.strength
                                        )
                                    },
                                    associatedDrug2 = emptyList()
                                )
                            ),
                            className2 = emptyList()
                        )
                    )
                )
            }

            problem.name to listOf(
                ProblemData(
                    medications = medications,
                    labs = if (problem.hasLabs) listOf(Lab(missingField = null)) else emptyList()
                )
            )
        }.toMap()
    }

    fun getDrugsByProblem(problemId: Long): Flow<List<DrugEntity>> {
        return drugDao.getDrugsByProblem(problemId)
    }

} 