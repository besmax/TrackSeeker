package bes.max.trackseeker.data.search

import android.content.SharedPreferences
import bes.max.trackseeker.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val SHARED_PREF_KEY = "search_history_preferences_key"

class SearchHistoryDaoImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SearchHistoryDao {

    private var history: MutableList<Track> = arrayListOf()

    override fun saveTrack(track: Track) {
        if (sharedPreferences.contains(SHARED_PREF_KEY)) {
            val jsonString = sharedPreferences.getString(SHARED_PREF_KEY, "")
            if (!jsonString.isNullOrBlank()) {
                history = gson.fromJson(jsonString, Array<Track>::class.java).toMutableList()
                if (history.contains(track)) {
                    history.remove(track)
                }
                if (history.size > 9) {
                    history.removeFirst()
                }
            }
        }
        history.add(track)
        sharedPreferences.edit()
            .putString(SHARED_PREF_KEY, gson.toJson(history))
            .apply()
    }

    override fun getHistoryTracks(): Flow<List<Track>> = flow {
        history.clear()
        if (sharedPreferences.contains(SHARED_PREF_KEY)) {
            val jsonString = sharedPreferences.getString(SHARED_PREF_KEY, "")
            if (!jsonString.isNullOrBlank()) {
                history = gson.fromJson(jsonString, Array<Track>::class.java).toMutableList()
            }
        }
        emit(history.toList())
    }

    override fun clearTracksHistory() {
        history.clear()
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}