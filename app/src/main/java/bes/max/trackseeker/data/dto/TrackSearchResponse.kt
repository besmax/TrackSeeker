package bes.max.trackseeker.data.dto

data class TrackSearchResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()
