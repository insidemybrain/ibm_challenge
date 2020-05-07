package com.ibm.challenge.app.login.domain.model

import com.ibm.challenge.domain.model.DomainModel

data class UserAccountDomain(val userID: Long? = null,
                             val name: String? = null,
                             val bankAccount: String? = null,
                             val agency: String? = null,
                             val balance: Double? = null): DomainModel() {

    override var baseCacheKey: String?
        get() = "user_account_domain"
        set(value) {}

}