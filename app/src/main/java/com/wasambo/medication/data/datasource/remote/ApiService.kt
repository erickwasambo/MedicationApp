package com.wasambo.medication.data.datasource.remote

import com.wasambo.medication.data.model.Problems
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @GET("769f8da8-aa02-45b6-a0b4-af5f3e045de3")
    @Headers(
        "Content-Type: application/json",
        "Connection: keep-alive"
    )
    suspend fun medicalProblems(): Response<Problems>
}