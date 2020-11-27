package com.example.calbard.ui.main

import io.reactivex.Observable

object ApiService {
    private val apiService = RetrofitClient.getClient().create(IApiService::class.java)

    fun getAllPictures(): Observable<Array<Picture>> {
      return apiService.getAllPictures()
    }

    fun savePicture(json: Picture, length: String): Observable<String> {
        return apiService.savePicture(json, length)
    }
}