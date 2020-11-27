package com.example.calbard.ui.main

import io.reactivex.Observable
import retrofit2.http.*

interface IApiService {
    @GET("/pics")
    fun getAllPictures(): Observable<Array<Picture>>

    @Headers("Content-Type: application/json")
    @PUT("/pic/")
    fun savePicture(@Body json: Picture, @Header ("Content-Length") length: String): Observable<String>
}