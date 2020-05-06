package com.ibm.challenge.login.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ibm.challenge.data.entity.Entity
import com.ibm.challenge.login.domain.model.LoginResponseDomain

data class LoginResponse(@Expose @SerializedName("userAccount") val userAccount: UserAccount? = null,
                         @Expose @SerializedName("error") val error: Error? = null): Entity<LoginResponseDomain>() {

    companion object {
        fun fromDomainModel(domainModel: LoginResponseDomain): LoginResponse {
            val userAccount = if (domainModel.userAccount != null) UserAccount.fromDomainModel(domainModel.userAccount) else null
            val error = if (domainModel.error != null) Error.fromDomainModel(domainModel.error) else null

            return LoginResponse(userAccount, error)
        }
    }

    override fun asDomainModel(): LoginResponseDomain {
        return LoginResponseDomain(userAccount?.asDomainModel(), error?.asDomainModel())
    }

}