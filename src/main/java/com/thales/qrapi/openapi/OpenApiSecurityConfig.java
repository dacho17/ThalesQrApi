package com.thales.qrapi.openapi;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
  info =@Info(
    title = "QrCode API",
    version = "${api.version}",
    contact = @Contact(
      name = "David", email = "david@thales.com", url = "https://en.wikipedia.org/wiki/Thales_of_Miletus"
    ),
    description = "${api.description}"
  ),
  servers = @Server(
    url = "${api.server.url}",
    description = "Development"
  )
)
@SecurityScheme(
	name = "Bearer Authentication",
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	scheme = "bearer")
public class OpenApiSecurityConfig {

}
