package bes.max.trackseeker.ui

import bes.max.trackseeker.domain.models.Track
import com.google.gson.Gson

object GsonTrackConverter {

    fun fromJsonToTrack(json: String): Track {
        return Gson().fromJson(json, Track::class.java)

    }

    fun convertTrackToJson(track: Track): String {
        return Gson().toJson(track)
    }

}