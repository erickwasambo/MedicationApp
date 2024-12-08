package com.wasambo.medication.data.repository.remote.medical

import com.wasambo.medication.data.datasource.local.dao.MedicalDataDao
import com.wasambo.medication.data.datasource.remote.ApiService
import com.wasambo.medication.data.local.entity.DrugEntity
import com.wasambo.medication.data.local.entity.ProblemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MedicalRepository @Inject constructor(
    private val apiService: ApiService,
    private val medicalDataDao: MedicalDataDao
) {
    suspend fun fetchAndSaveMedicalData() {
        println("Starting fetchAndSaveMedicalData in coroutine: ${Thread.currentThread().name}")
        try {
            println("Making API call to fetch medical problems")
            withContext(Dispatchers.IO) {
                val response = apiService.medicalProblems()
                if (response.isSuccessful && response.body() != null) {
                    val problems = response.body()!!
                    println("Successfully parsed response body with ${problems.problems.size} problems")

                    problems.problems.forEach { problemMap ->
                        problemMap.forEach { (problemName, problemDataList) ->
                            problemDataList.forEach { problemData ->
                                // Create problem entity
                                val problem = ProblemEntity(
                                    name = problemName,
                                    hasLabs = !problemData.labs.isNullOrEmpty()
                                )

                                // Extract drugs from medications
                                val drugs = mutableListOf<DrugEntity>()
                                problemData.medications?.forEach { medication ->
                                    medication.medicationsClasses?.forEach { medicationClass ->
                                        // Process className drugs
                                        medicationClass.className?.forEach { drugClass ->
                                            drugClass.associatedDrug?.forEach { drug ->
                                                drugs.add(DrugEntity(
                                                    name = drug.name ?: "",
                                                    dose = drug.dose ?: "",
                                                    strength = drug.strength ?: ""
                                                ))
                                            }
                                            drugClass.associatedDrug2?.forEach { drug ->
                                                drugs.add(DrugEntity(
                                                    name = drug.name ?: "",
                                                    dose = drug.dose ?: "",
                                                    strength = drug.strength ?: ""
                                                ))
                                            }
                                        }

                                        // Process className2 drugs
                                        medicationClass.className2?.forEach { drugClass ->
                                            drugClass.associatedDrug?.forEach { drug ->
                                                drugs.add(DrugEntity(
                                                    name = drug.name ?: "",
                                                    dose = drug.dose ?: "",
                                                    strength = drug.strength ?: ""
                                                ))
                                            }
                                            drugClass.associatedDrug2?.forEach { drug ->
                                                drugs.add(DrugEntity(
                                                    name = drug.name ?: "",
                                                    dose = drug.dose ?: "",
                                                    strength = drug.strength ?: ""
                                                ))
                                            }
                                        }
                                    }
                                }

                                // Save problem with its drugs
                                medicalDataDao.insertProblemWithDrugs(problem, drugs, "default")
                            }
                        }
                    }
                } else {
                    // Handle error case
                    throw Exception("Failed to fetch medical data: ${response.message()}")
                }
            }
        } catch (e: Exception) {
            println("Error in fetchAndSaveMedicalData: ${e.message}")
            throw e
        }
    }
}