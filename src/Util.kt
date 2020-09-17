package net.ambitious.sharebookmarks

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.ktor.application.*
import io.ktor.http.auth.*
import io.ktor.util.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@KtorExperimentalAPI
object Util {

  private const val TIMEZONE = "Asia/Tokyo"
  private val DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  val DATETIME_ZONE: DateTimeZone = DateTimeZone.forID(TIMEZONE)

  fun auth(
    httpAuthHeader: HttpAuthHeader?,
    secret: String,
    audience: String
  ) = httpAuthHeader.let {
    if (it != null &&
      it.authScheme == "Bearer" &&
      it is HttpAuthHeader.Single
    ) {
      try {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(it.blob)
      } catch (ex: Exception) {
        null
      }?.let { jws ->
        if (jws.body.audience == audience && jws.body.subject != null) {
          jws.body.subject.toIntOrNull()
        } else {
          null
        }
      }
    } else {
      null
    }
  }

  fun generateToken(
    id: Int,
    secret: String,
    audience: String
  ): String = Jwts.builder()
    .setSubject(id.toString())
    .setAudience(audience)
    .setHeaderParam("typ", "JWT")
    .setExpiration(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.of(TIMEZONE)).toInstant()))
    .signWith(SignatureAlgorithm.HS256, secret)
    .compact()

  fun getSecret(environment: ApplicationEnvironment) =
    environment.config.property("app.auth.secret").getString()

  fun getAudience(environment: ApplicationEnvironment) =
    environment.config.property("app.auth.audience").getString()

  fun datetimeFormat(time: Long?): String = DATETIME_FORMAT.print(time ?: DateTime(DATETIME_ZONE).millis)

  fun datetimeParse(time: String): DateTime = DATETIME_FORMAT.parseDateTime(time)
}