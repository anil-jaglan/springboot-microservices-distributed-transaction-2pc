package io.learning.transaction.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Transactional Server", description = "REST API for managing transaction", version = "1.0"))
public class OpenApiConfig {

}
