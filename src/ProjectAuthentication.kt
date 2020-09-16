package net.ambitious.sharebookmarks

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.util.*

class ProjectAuthentication(val config: Configuration) {

  companion object : ApplicationFeature<ApplicationCallPipeline, Configuration, ProjectAuthentication> {
    override val key: AttributeKey<ProjectAuthentication> = AttributeKey("ProjectAuthentication")

    @KtorExperimentalAPI
    override fun install(
      pipeline: ApplicationCallPipeline,
      configure: Configuration.() -> Unit
    ): ProjectAuthentication {
      val config = Configuration().apply(configure)
      val feature = ProjectAuthentication(config)
      pipeline.intercept(ApplicationCallPipeline.Features) {
        Util.auth(
          call.request.parseAuthorizationHeader(),
          Util.getSecret(application.environment),
          Util.getAudience(application.environment)
        )?.let { call.attributes.put(feature.config.userIdKey, it) }
      }
      return feature
    }
  }

  class Configuration {
    val userIdKey = AttributeKey<Int>("userId")
  }
}