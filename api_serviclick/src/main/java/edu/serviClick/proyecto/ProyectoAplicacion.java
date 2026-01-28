package edu.serviClick.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "edu.serviClick.proyecto")
@EnableJpaRepositories(basePackages = "edu.serviClick.proyecto.repositorios")
@EntityScan(basePackages = "edu.serviClick.proyecto.entidades")
public class ProyectoAplicacion {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoAplicacion.class, args);
	}
} 	