package net.ambitious.sharebookmarks

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.jsoup.Jsoup
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object Util {

  private const val TIMEZONE = "Asia/Tokyo"
  private val DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  val DATETIME_ZONE: DateTimeZone = DateTimeZone.forID(TIMEZONE)
  const val LAST_UPDATE_DEFAULT = "2020-01-01 00:00:00"

  data class AuthUser(val id: Int): Principal
  const val JWT_ISSUER = "bvlion"

  fun generateToken(
    hash: String,
    algorithm: Algorithm,
    audience: String
  ): String = JWT.create()
    .withAudience(audience)
    .withIssuer(JWT_ISSUER)
    .withIssuedAt(DateTime(DATETIME_ZONE).toDate())
    .withSubject(hash)
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