package com.example.project_g09.networking

import com.example.project_g09.models.AllStatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //getState fucntion to iniate query to API and get the response type.
    @GET("api/v1/parks/")
    suspend fun getStates(@Query("stateCode") stateCode: String, @Query("api_key") apiKey: String): Response<AllStatesResponse>


}