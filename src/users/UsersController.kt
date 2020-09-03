package net.ambitious.sharebookmarks.users

import io.ktor.application.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

class UsersController {
  @KtorExperimentalAPI
  fun auth(user: UsersModel.UserRequest, environment: ApplicationEnvironment) =
    UsersEntity.find { UsersDao.email eq user.email }.firstOrNull().let { dbUser ->
      UsersModel.AuthResponse(
        dbUser?.premium == 1,
        Util.generateToken(
          if (dbUser == null) {
            UsersDao.insert {
              it[email] = user.email
              it[fcmToken] = user.fcmToken
            } get UsersDao.id
          } else {
            UsersDao.update({ UsersDao.id eq dbUser.id }) {
              it[fcmToken] = user.fcmToken
            }
            dbUser.id
          }.value,
          Util.getSecret(environment),
          Util.getAudience(environment)
        )
      )
    }
}