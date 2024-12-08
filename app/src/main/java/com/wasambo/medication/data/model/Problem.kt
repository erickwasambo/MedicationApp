package com.wasambo.medication.data.model

import com.google.gson.annotations.SerializedName

data class Problems(
    val problems: List<Map<String, List<ProblemData>>>
)

data class ProblemData(
    val medications: List<Medication>? = null,
    val labs: List<Lab>? = null
)

data class Medication(
    val medicationsClasses: List<MedicationClass>? = null
)

data class MedicationClass(
    val className: List<DrugClass>? = null,
    val className2: List<DrugClass>? = null
)

data class DrugClass(
    val associatedDrug: List<Drug>? = null,
    @SerializedName("associatedDrug#2")
    val associatedDrug2: List<Drug>? = null
)

data class Drug(
    val name: String? = null,
    val dose: String? = null,
    val strength: String? = null
)

data class Lab(
    @SerializedName("missing_field")
    val missingField: String?
)