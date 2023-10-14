package bes.max.trackseeker.domain.settings

import bes.max.trackseeker.domain.models.EmailData

interface SharingInteractor {

    fun shareApp(link: String)

    fun openUserAgreement(link: String)

    fun contactSupport(emailData: EmailData)

}