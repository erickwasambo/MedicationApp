package com.wasambo.medication.di

import com.wasambo.medication.data.datasource.local.dao.MedicalDataDao
import com.wasambo.medication.data.datasource.remote.ApiService
import com.wasambo.medication.data.repository.remote.medical.MedicalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides RemoteDataRepository for access mock api service method
     */
    @Singleton
    @Provides
    fun provideMedicalRepository(
        apiService: ApiService,
        medicalDataDao: MedicalDataDao,
    ): MedicalRepository {
        return MedicalRepository(
            apiService,
            medicalDataDao
        )
    }
}