package com.wasambo.medication.data.local.dao

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wasambo.medication.data.datasource.local.MedicalDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.After
import org.junit.runner.RunWith
import android.content.Context
import androidx.room.Room
import com.wasambo.medication.data.local.entity.DrugEntity

@RunWith(AndroidJUnit4::class)
class DrugDaoTest {
    private lateinit var database: MedicalDatabase
    private lateinit var drugDao: DrugDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, MedicalDatabase::class.java
        ).allowMainThreadQueries() // Add this for testing
        .build()
        drugDao = database.getDrugDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetDrug() = runTest {
        // Given
        val drug = DrugEntity(1, "Aspirin", "100mg", "Once daily")

        // When
        drugDao.insertDrugs(listOf(drug))
        val result = drugDao.getAllDrugs().first()

        // Then
        assertEquals(1, result.size)
        assertEquals(drug, result[0])
    }
} 