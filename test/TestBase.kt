import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.module
import net.ambitious.sharebookmarks.users.UsersDao
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.json.JSONObject
import org.json.JSONTokener
import org.junit.*
import org.slf4j.LoggerFactory

open class TestBase {
  protected lateinit var engine: TestApplicationEngine

  @KtorExperimentalAPI
  @Before
  fun setUp() {
    engine = TestApplicationEngine(applicationEngineEnvironment {
      config = MapApplicationConfig(
        "app.database.url" to "jdbc:mysql://127.0.0.1:3337/bookmarks?useSSL=false",
        "app.database.user" to "user",
        "app.database.password" to "password",
        "app.auth.audience" to "sharebookmarks",
        "app.auth.secret" to "sharebookmarks",
        "app.pretty_print" to "false"
      )
      log = LoggerFactory.getLogger("ktor.test")
    }).apply {
      start(wait = false)
      application.module()

      // テストごとにテスト用ユーザーを作成 OR 更新する
      handleRequest(HttpMethod.Post, "/users/auth") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(
          JsonObject(
            mapOf(
              "email" to "test@user",
              "uid" to "test_user",
              "fcm_token" to "test_user"
            )
          ).toString()
        )
      }.response.run {
        val json = JSONObject(JSONTokener(content!!))
        TempData.token = json["access_token"].toString()
      }
    }
  }

  @After
  fun tearDown() {
    engine.stop(0, 0)
  }

  companion object {
    @AfterClass @JvmStatic
    fun tearDownAfterClass() {
      transaction {
        UsersDao.deleteAll()
      }
    }
  }
}