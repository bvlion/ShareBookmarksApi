package users

import JsonObject
import TestBase
import io.ktor.http.*
import io.ktor.server.testing.*
import net.ambitious.sharebookmarks.users.UsersDao
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.json.JSONObject
import org.json.JSONTokener
import org.junit.Assert
import org.junit.Test

class UsersTest : TestBase() {
  /** POST しただけではエラーになる */
  @Test
  fun userAuthFailed() {
    with(engine) {
      handleRequest(HttpMethod.Post, "/users/auth").response.run {
        Assert.assertEquals(HttpStatusCode.UnsupportedMediaType, status())
      }
    }
  }

  /** 登録と更新 */
  @Test
  fun userCreateUpdate() {
    with(engine) {
      // 登録する
      handleRequest(HttpMethod.Post, "/users/auth") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(JsonObject(
            mapOf(
                "email" to "test@example.com",
                "uid" to "test3r2test_test",
                "fcm_token" to "test"
            )
        ).toString())
      }.response.run {
        val json = JSONObject(JSONTokener(content!!))
        Assert.assertEquals(json["premium"], false)
        Assert.assertEquals(HttpStatusCode.OK, status())
        transaction {
          val user = UsersDao.selectAll().where { UsersDao.email eq "test@example.com" }.first()
          Assert.assertEquals(user[UsersDao.uid], "test3r2test_test")
          Assert.assertEquals(user[UsersDao.fcmToken], "test")
        }
      }

      // 更新する
      handleRequest(HttpMethod.Post, "/users/auth") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(JsonObject(
          mapOf(
            "email" to "test@example.com",
            "uid" to "test3r2test_test",
            "fcm_token" to "test2"
          )
        ).toString())
      }.response.run {
        val json = JSONObject(JSONTokener(content!!))
        Assert.assertEquals(json["premium"], false)
        Assert.assertEquals(HttpStatusCode.OK, status())
        transaction {
          val user = UsersDao.selectAll().where { UsersDao.email eq "test@example.com" }.first()
          Assert.assertEquals(user[UsersDao.fcmToken], "test2")
        }
      }
    }
  }
}