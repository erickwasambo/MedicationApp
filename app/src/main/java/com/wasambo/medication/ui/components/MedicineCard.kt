package com.wasambo.medication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wasambo.medication.data.model.Drug
import com.wasambo.medication.data.model.ProblemData

@Composable
fun MedicineCard(
    problemData: ProblemData,
    onDrugClick: (Drug) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            if (problemData.medications.isNullOrEmpty()) {
                Text(
                    text = "No medications available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                problemData.medications.forEach { medication ->
                    medication.medicationsClasses?.forEach { medicationClass ->
                        // Display className drugs
                        medicationClass.className?.forEach { classData ->
                            // Display associatedDrug
                            classData.associatedDrug?.forEach { drug ->
                                DrugItem(
                                    drug = drug,
                                    onClick = { onDrugClick(drug) }
                                )
                            }
                            // Display associatedDrug#2
                            classData.associatedDrug2?.forEach { drug ->
                                DrugItem(
                                    drug = drug,
                                    onClick = { onDrugClick(drug) }
                                )
                            }
                        }

                        // Display className2 drugs
                        medicationClass.className2?.forEach { classData ->
                            // Display associatedDrug
                            classData.associatedDrug?.forEach { drug ->
                                DrugItem(
                                    drug = drug,
                                    onClick = { onDrugClick(drug) }
                                )
                            }
                            // Display associatedDrug#2
                            classData.associatedDrug2?.forEach { drug ->
                                DrugItem(
                                    drug = drug,
                                    onClick = { onDrugClick(drug) }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Display labs information if available
            problemData.labs?.forEach { lab ->
                lab.missingField?.let { missingValue ->
                    Text(
                        text = "Lab: $missingValue",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
