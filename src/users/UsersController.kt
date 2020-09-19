package net.ambitious.sharebookmarks.users

import io.ktor.application.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

class UsersController {
  @KtorExperimentalAPI
  fun auth(user: UsersModel.UserRequest, environment: ApplicationEnvironment): UsersModel.AuthResponse {
    // 共有で先にユーザー登録だけされていた方は UID を更新する
    UsersEntity.find { (UsersDao.email eq user.email) and UsersDao.uid.isNull() }.first().let { dbUser ->
      UsersDao.update({ UsersDao.id eq dbUser.id }) {
        it[uid] = user.uid
      }
    }

    return UsersEntity.find { (UsersDao.uid eq user.uid) and (UsersDao.email eq user.email) }.firstOrNull()
      .let { dbUser ->
        UsersModel.AuthResponse(
          dbUser?.premium == 1,
          Util.generateToken(
            if (dbUser == null) {
              UsersDao.insert {
                it[email] = user.email
                it[uid] = user.uid
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
}