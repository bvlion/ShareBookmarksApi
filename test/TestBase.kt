import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.module
import org.junit.After
import org.junit.Before
import org.slf4j.LoggerFactory

open class TestBase {
  protected lateinit var engine: TestApplicationEngine

  @KtorExperimentalAPI
  @Before
  fun before() {
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
    }
  }

  @After
  fun after() {
    engine.stop(0, 0)
  }
}