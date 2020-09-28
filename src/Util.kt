package net.ambitious.sharebookmarks

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
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

  data class AuthUser(val id: Int): Principal
  const val USER_ID_CLAIM = "user_id"
  const val JWT_ISSUER = "bvlion"

  fun generateToken(
    id: Int,
    algorithm: Algorithm,
    audience: String
  ): String = JWT.create()
    .withAudience(audience)
    .withExpiresAt(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.of(TIMEZONE)).toInstant()))
    .withClaim(USER_ID_CLAIM, id)
    .withIssuer(JWT_ISSUER)
    .sign(algorithm)

  fun getAlgorithm(environment: ApplicationEnvironment): Algorithm =
    Algorithm.HMAC256(environment.config.property("app.auth.secret").getString())

  fun getAudience(environment: ApplicationEnvironment) =
    environment.config.property("app.auth.audience").getString()

  fun datetimeFormat(time: Long?): String = DATETIME_FORMAT.print(time ?: DateTime(DATETIME_ZONE).millis)

  fun datetimeParse(time: String): DateTime = DATETIME_FORMAT.parseDateTime(time)
}