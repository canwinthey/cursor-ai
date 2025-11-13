package com.cursor.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 * <p>
 * This class configures the OpenAPI specification for the Product CRUD API,
 * including API information, contact details, and license information.
 * </p>
 *
 * @author Cursor Product Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates and configures the OpenAPI bean for Swagger documentation.
     *
     * @return configured OpenAPI instance with API information
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product CRUD API")
                        .version("1.0.0")
                        .description("REST API for Product CRUD operations")
                        .contact(new Contact()
                                .name("Product API Support")
                                .email("support@cursor.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}

