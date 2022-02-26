package net.ambitious.sharebookmarks.users

import io.ktor.application.*
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import java.util.*

class UsersController {
  fun auth(user: UsersModel.UserRequest, environment: ApplicationEnvironment): UsersModel.AuthResponse {
    // 共有で先にユーザー登録だけされていた方は UID を更新する
    UsersEntity.find { (UsersDao.email eq user.email) and UsersDao.uid.isNull() }.firstOrNull()?.let { dbUser ->
      UsersDao.update({ UsersDao.id eq dbUser.id }) {
        it[uid] = user.uid
      }
    }

    return UsersEntity.find { (UsersDao.uid eq user.uid) and (UsersDao.email eq user.email) }.firstOrNull()
      .let { dbUser ->
        UUID.randomUUID().toString().let { uuid ->
          if (dbUser == null) {
            UsersDao.insert {
              it[email] = user.email
              it[uid] = user.uid
              it[fcmToken] = user.fcmToken
              it[hash] = uuid
            } get UsersDao.id
          } else {
            UsersDao.update({ UsersDao.id eq dbUser.id }) {
              it[fcmToken] = user.fcmToken
              it[hash] = uuid
            }
            dbUser.id
          }
          UsersModel.AuthResponse(
            dbUser?.premium == 1,
            Util.generateToken(
              uuid,
              Util.getAlgorithm(environment),
              Util.getAudience(environment)
            )
          )
        }
      }
  }
}