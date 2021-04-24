package etc

import JsonObject
import TestBase
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test

class EtcTest : TestBase() {

  @Test
  fun indexOnly() {
    with(engine) {
      handleRequest(HttpMethod.Get, "/test").response.run {
        assertEquals(HttpStatusCode.OK, status())
      }
    }
  }

  @Test
  fun index() {
    with(engine) {
      handleRequest(HttpMethod.Get, "/").response.run {
        assertEquals(HttpStatusCode.OK, status())
      }
    }
  }

  @Test
  fun ogp() {
    with(engine) {
      // 取得できるパターン
      handleRequest(HttpMethod.Get, "/etc/ogp?url=https://www.ambitious-i.net").response.run {
        assertEquals(HttpStatusCode.OK, status())
        assertEquals(
          JsonObject(
            mapOf("url" to "https://www.ambitious-i.net/img/main.jpg")
          ).toString(),
          content
        )
      }
      // 取得できないパターン
      handleRequest(HttpMethod.Get, "/etc/ogp?url=https://example.com").response.run {
        assertEquals(HttpStatusCode.OK, status())
        assertEquals(
          JsonObject(
            mapOf("url" to null)
          ).toString(),
          content
        )
      }
      // URL が http
      handleRequest(HttpMethod.Get, "/etc/ogp?url=http://vigor.tokyo").response.run {
        assertEquals(HttpStatusCode.OK, status())
        assertEquals(
          JsonObject(
            mapOf("url" to null)
          ).toString(),
          content
        )
      }
    }
  }
}