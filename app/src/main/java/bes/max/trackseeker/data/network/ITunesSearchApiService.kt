package bes.max.trackseeker.data.network

import bes.max.trackseeker.data.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApiService {

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String) : TrackSearchResponse

}
