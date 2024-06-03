package com.sandeshjain.tutorials;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author Sandesh Jain
 */
@OpenAPIDefinition(info = @Info(title = "Tutorials REST API(Mongo Database) Documentation", description = "Sandesh_Jain_Publication REST API Documentation", version = "v1", contact = @Contact(name = "Sandesh Jain", email = "tutor@sandesh-jain.com", url = "https://www.sandesh-jain.com"), license = @License(name = "Apache 2.0", url = "https://www.sandesh-jain.com")), externalDocs = @ExternalDocumentation(description = "Sandesh_Jain_Publication REST API Documentation", url = "http://localhost:8080/webjars/swagger-ui/index.html"))
@SpringBootApplication
public class SandeshjainMongodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SandeshjainMongodbApplication.class, args);
	}

}
