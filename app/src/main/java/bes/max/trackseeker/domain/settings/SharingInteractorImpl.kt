package bes.max.trackseeker.domain.settings

import bes.max.trackseeker.domain.models.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {

    override fun shareApp(link: String) {
        externalNavigator.shareApp(link)
    }

    override fun openUserAgreement(link: String) {
        externalNavigator.openUserAgreement(link)
    }

    override fun contactSupport(emailData: EmailData) {
        externalNavigator.sendEmail(emailData)
    }

}