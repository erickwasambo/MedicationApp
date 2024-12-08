package com.wasambo.medication.di

import android.content.Context
import androidx.room.Room
import com.wasambo.medication.data.datasource.local.MedicalDatabase
import com.wasambo.medication.data.datasource.local.dao.MedicalDataDao
import com.wasambo.medication.data.local.dao.DrugDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideMedicalDatabase(@ApplicationContext context: Context): MedicalDatabase {
        return Room.databaseBuilder(
            context,
            MedicalDatabase::class.java,
            "medical.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMedicalDataDao(medicationsDatabase: MedicalDatabase): MedicalDataDao =
        medicationsDatabase.getMedicalDataDao()

    @Singleton
    @Provides
    fun provideDrugDao(medicationsDatabase: MedicalDatabase): DrugDao =
        medicationsDatabase.getDrugDao()
}