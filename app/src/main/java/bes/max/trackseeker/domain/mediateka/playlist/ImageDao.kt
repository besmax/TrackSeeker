package bes.max.trackseeker.domain.mediateka.playlist

import android.net.Uri

interface ImageDao {

    suspend fun saveImage(uri: Uri) : Uri
}