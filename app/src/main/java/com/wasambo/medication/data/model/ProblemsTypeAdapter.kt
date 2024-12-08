package com.wasambo.medication.data.model

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class ProblemsTypeAdapter : TypeAdapter<Problems>() {
    override fun write(out: JsonWriter, value: Problems?) {
        out.beginObject()
        out.name("problems")
        out.beginArray()
        value?.problems?.forEach { problemMap ->
            out.beginObject()
            problemMap.forEach { (disease, problemDataList) ->
                out.name(disease)
                out.beginArray()
                problemDataList.forEach { problemData ->
                    out.beginObject()

                    // Write medications
                    problemData.medications?.let { medications ->
                        out.name("medications")
                        out.beginArray()
                        medications.forEach { medication ->
                            writeMedication(out, medication)
                        }
                        out.endArray()
                    }

                    // Write labs
                    problemData.labs?.let { labs ->
                        out.name("labs")
                        out.beginArray()
                        labs.forEach { lab ->
                            out.beginObject()
                            lab.missingField?.let {
                                out.name("missing_field")
                                out.value(it)
                            }
                            out.endObject()
                        }
                        out.endArray()
                    }

                    out.endObject()
                }
                out.endArray()
            }
            out.endObject()
        }
        out.endArray()
        out.endObject()
    }

    private fun writeMedication(out: JsonWriter, medication: Medication) {
        out.beginObject()
        out.name("medicationsClasses")
        out.beginArray()
        medication.medicationsClasses?.forEach { medicationClass ->
            out.beginObject()

            // Write className
            medicationClass.className?.let { drugClasses ->
                out.name("className")
                writeDrugClasses(out, drugClasses)
            }

            // Write className2
            medicationClass.className2?.let { drugClasses ->
                out.name("className2")
                writeDrugClasses(out, drugClasses)
            }

            out.endObject()
        }
        out.endArray()
        out.endObject()
    }

    private fun writeDrugClasses(out: JsonWriter, drugClasses: List<DrugClass>) {
        out.beginArray()
        drugClasses.forEach { drugClass ->
            out.beginObject()

            // Write associatedDrug
            drugClass.associatedDrug?.let { drugs ->
                out.name("associatedDrug")
                writeDrugs(out, drugs)
            }

            // Write associatedDrug#2
            drugClass.associatedDrug2?.let { drugs ->
                out.name("associatedDrug#2")
                writeDrugs(out, drugs)
            }

            out.endObject()
        }
        out.endArray()
    }

    private fun writeDrugs(out: JsonWriter, drugs: List<Drug>) {
        out.beginArray()
        drugs.forEach { drug ->
            out.beginObject()
            drug.name?.let { out.name("name").value(it) }
            drug.dose?.let { out.name("dose").value(it) }
            drug.strength?.let { out.name("strength").value(it) }
            out.endObject()
        }
        out.endArray()
    }

    override fun read(input: JsonReader): Problems {
        val problems = mutableListOf<Map<String, List<ProblemData>>>()

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "problems" -> {
                    input.beginArray()
                    while (input.hasNext()) {
                        val problemMap = mutableMapOf<String, List<ProblemData>>()
                        input.beginObject()
                        while (input.hasNext()) {
                            val diseaseName = input.nextName()
                            val problemDataList = mutableListOf<ProblemData>()

                            input.beginArray()
                            while (input.hasNext()) {
                                problemDataList.add(readProblemData(input))
                            }
                            input.endArray()

                            problemMap[diseaseName] = problemDataList
                        }
                        input.endObject()
                        problems.add(problemMap)
                    }
                    input.endArray()
                }
                else -> input.skipValue()
            }
        }
        input.endObject()

        return Problems(problems)
    }

    private fun readProblemData(input: JsonReader): ProblemData {
        var medications: List<Medication>? = null
        var labs: List<Lab>? = null

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "medications" -> {
                    val medicationsList = mutableListOf<Medication>()
                    input.beginArray()
                    while (input.hasNext()) {
                        medicationsList.add(readMedication(input))
                    }
                    input.endArray()
                    medications = medicationsList
                }
                "labs" -> {
                    val labsList = mutableListOf<Lab>()
                    input.beginArray()
                    while (input.hasNext()) {
                        input.beginObject()
                        var missingField: String? = null
                        while (input.hasNext()) {
                            if (input.nextName() == "missing_field") {
                                missingField = input.nextString()
                            } else {
                                input.skipValue()
                            }
                        }
                        input.endObject()
                        labsList.add(Lab(missingField))
                    }
                    input.endArray()
                    labs = labsList
                }
                else -> input.skipValue()
            }
        }
        input.endObject()

        return ProblemData(medications, labs)
    }

    private fun readMedication(input: JsonReader): Medication {
        var medicationsClasses: List<MedicationClass>? = null

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "medicationsClasses" -> {
                    val classList = mutableListOf<MedicationClass>()
                    input.beginArray()
                    while (input.hasNext()) {
                        classList.add(readMedicationClass(input))
                    }
                    input.endArray()
                    medicationsClasses = classList
                }
                else -> input.skipValue()
            }
        }
        input.endObject()

        return Medication(medicationsClasses)
    }

    private fun readMedicationClass(input: JsonReader): MedicationClass {
        var className: List<DrugClass>? = null
        var className2: List<DrugClass>? = null

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "className" -> className = readDrugClasses(input)
                "className2" -> className2 = readDrugClasses(input)
                else -> input.skipValue()
            }
        }
        input.endObject()

        return MedicationClass(className, className2)
    }

    private fun readDrugClasses(input: JsonReader): List<DrugClass> {
        val drugClasses = mutableListOf<DrugClass>()

        input.beginArray()
        while (input.hasNext()) {
            var associatedDrug: List<Drug>? = null
            var associatedDrug2: List<Drug>? = null

            input.beginObject()
            while (input.hasNext()) {
                when (input.nextName()) {
                    "associatedDrug" -> associatedDrug = readDrugs(input)
                    "associatedDrug#2" -> associatedDrug2 = readDrugs(input)
                    else -> input.skipValue()
                }
            }
            input.endObject()

            drugClasses.add(DrugClass(associatedDrug, associatedDrug2))
        }
        input.endArray()

        return drugClasses
    }

    private fun readDrugs(input: JsonReader): List<Drug> {
        val drugs = mutableListOf<Drug>()

        input.beginArray()
        while (input.hasNext()) {
            var name: String? = null
            var dose: String? = null
            var strength: String? = null

            input.beginObject()
            while (input.hasNext()) {
                when (input.nextName()) {
                    "name" -> name = input.nextString()
                    "dose" -> dose = input.nextString()
                    "strength" -> strength = input.nextString()
                    else -> input.skipValue()
                }
            }
            input.endObject()

            drugs.add(Drug(name, dose, strength))
        }
        input.endArray()

        return drugs
    }
}