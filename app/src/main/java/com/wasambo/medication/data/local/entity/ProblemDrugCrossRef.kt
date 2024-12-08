package com.wasambo.medication.data.local.entity

import androidx.room.Entity

@Entity(tableName = "problem_drug_cross_ref", primaryKeys = ["problemId", "drugId"])
data class ProblemDrugCrossRef(
    val problemId: Long,
    val drugId: Long,
    val className: String
) 