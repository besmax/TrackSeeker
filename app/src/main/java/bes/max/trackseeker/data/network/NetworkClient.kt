package bes.max.trackseeker.data.network

import bes.max.trackseeker.data.dto.Response

interface NetworkClient {

    suspend fun searchTracks(dto: Any) : Response

}