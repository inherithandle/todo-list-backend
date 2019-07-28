package com.gtchoi.todolistbackend;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoListBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoListBackendApplication.class, args);
		ServiceRegistry standardRegistry =
				new StandardServiceRegistryBuilder().build();

		MetadataSources sources = new MetadataSources( standardRegistry );
	}

}
