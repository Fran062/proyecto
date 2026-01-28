package edu.backend_frontend_serviclick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "edu.backend_frontend_serviclick") 
public class BackendFrontendServiclickApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendFrontendServiclickApplication.class, args);
    }
}