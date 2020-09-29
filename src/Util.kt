package net.ambitious.sharebookmarks

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.util.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.jsoup.Jsoup
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.net.ssl.HttpsURLConnection

@KtorExperimentalAPI
object Util {

  private const val TIMEZONE = "Asia/Tokyo"
  private val DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  val DATETIME_ZONE: DateTimeZone = DateTimeZone.forID(TIMEZONE)

  data class AuthUser(val id: Int): Principal
  const val USER_ID_CLAIM = "user_id"
  const val JWT_ISSUER = "bvlion"

  fun generateToken(
    id: Int,
    algorithm: Algorithm,
    audience: String
  ): String = JWT.create()
    .withAudience(audience)
    .withExpiresAt(Date.from(LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC)))
    .withClaim(USER_ID_CLAIM, id)
    .withIssuer(JWT_ISSUER)
    .sign(algorithm)

  fun getAlgorithm(environment: ApplicationEnvironment): Algorithm =
    Algorithm.HMAC256(environment.config.property("app.auth.secret").getString())

  fun getAudience(environment: ApplicationEnvironment) =
    environment.config.property("app.auth.audience").getString()

  fun datetimeFormat(time: Long?): String = DATETIME_FORMAT.print(time ?: DateTime(DATETIME_ZONE).millis)

  fun datetimeParse(time: String): DateTime = DATETIME_FORMAT.parseDateTime(time)

  fun getOgpImage(url: String) = try {
    Jsoup.connect(url).get().select("meta[property~=og:image]")
      .map { it.attr("content") }.let { ogImage ->
        if (ogImage.isEmpty()) {
          null
        } else {
          if (isHttpStatusOk(ogImage.first())) {
            ogImage.first()
          } else {
            null
          }
        }
      }
  } catch (e: Exception) {
    null
  }

  private fun isHttpStatusOk(url: String) = try {
    val con = (URL(url).openConnection() as HttpsURLConnection).apply { requestMethod = "GET" }
    con.connect()
    con.responseCode == HttpsURLConnection.HTTP_OK
  } catch (_: Exception) {
    false
  }

}