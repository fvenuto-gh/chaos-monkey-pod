package it.sisal.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;


@SpringBootApplication
@PropertySource({ "classpath:application.properties" })
@ComponentScan("it.sisal.app.*")
public class ChaosMonkeyPodApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChaosMonkeyPodApplication.class);

	
	public static void main(String[] args) {
		LOGGER.info("Starting ChaosMonkeyPod...");
		SpringApplication.run(ChaosMonkeyPodApplication.class, args);
	}

	@Bean
	public CoreV1Api api() throws Exception{    	
		try {
			// get access to the Kubernetes API server
			ApiClient client  = Config.defaultClient();
			Configuration.setDefaultApiClient(client);
			return new CoreV1Api(client);
		} catch (Exception e) {
			LOGGER.error("Error while getting access to the Kubernetes API server: "+e.getMessage());
			throw e;
		}        
    }
}
