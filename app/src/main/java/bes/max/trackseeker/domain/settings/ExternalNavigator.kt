package bes.max.trackseeker.domain.settings

import bes.max.trackseeker.domain.models.EmailData

interface ExternalNavigator {

    fun shareApp(link: String)

    fun openUserAgreement(link: String)

    fun sendEmail(emailData: EmailData)

    fun sharePlaylist(playlistString: String)

}