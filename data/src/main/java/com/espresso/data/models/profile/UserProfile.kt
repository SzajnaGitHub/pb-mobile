package com.espresso.data.models.profile

data class UserProfile(
    val nickname: String?,
    val id: Long,
    val points: Double,
    val userType: String
) {
    fun isUnregistered() = userType == TYPE_UNREGISTERED

    companion object {
        fun resolveType(type: String?) = when (type) {
            "REGISTERED" -> TYPE_REGISTERED
            "OWNER" -> TYPE_OWNER
            "WORKER" -> TYPE_WORKER
            "UNREGISTERED" -> TYPE_UNREGISTERED
            else -> null
        }

        const val TYPE_REGISTERED = "REGISTERED"
        const val TYPE_OWNER = "OWNER"
        const val TYPE_WORKER = "WORKER"
        const val TYPE_UNREGISTERED = "UNREGISTERED"
    }
}

data class UserRegisterInfo(
    val uid: String,
    val email: String = "",
    val nickname: String = ""
)
