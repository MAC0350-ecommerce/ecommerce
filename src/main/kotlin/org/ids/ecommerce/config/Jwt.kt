package org.ids.ecommerce.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
data class Jwt(
    val key: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long
)