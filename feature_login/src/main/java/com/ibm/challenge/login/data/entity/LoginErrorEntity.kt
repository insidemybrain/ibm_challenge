package com.ibm.challenge.login.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ibm.challenge.core.model.Entity

data class LoginErrorEntity(@Expose @SerializedName("code") val code: Long? = null,
                            @Expose @SerializedName("message") val message: String? = null): Entity()