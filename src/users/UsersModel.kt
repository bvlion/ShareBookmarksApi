package net.ambitious.sharebookmarks.users

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty

object UsersModel {
  data class UserRequest(
    val email: String,
    @JsonAlias("fcm_token") val fcmToken: String
  )

  data class AuthResponse(
    val premium: Boolean,
    @JsonProperty("access_token") val accessToken: String
  )
}