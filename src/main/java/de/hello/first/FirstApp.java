package de.hello.first;

import de.hello.first.backend.v1.account.AccountClient;
import de.hello.first.backend.v1.account.AccountServiceConfiguration;
import de.hello.first.backend.v1.config.rest.RestServiceConfiguration;
import de.hello.first.backend.v1.rest.MitarbeiterAendernService;
import de.hello.first.backend.v1.rest.VsnrAendernService;
import de.hello.first.config.CamundaConfiguration;
import de.hello.first.v1.firstBpm.inbound.AccountServiceEndpoint;
import de.hello.first.v1.firstBpm.inbound.HelloController;
import de.hello.first.v1.firstBpm.inbound.configuration.WebServiceAdapterConfig;
import de.hello.first.v1.firstBpm.process.ProzessStarter;
import de.hello.first.v1.firstBpm.process.config.HelloProcessConfig;
import de.hello.first.v1.firstBpm.process.delegate.AsyncAntowrtBewertenTask;
import de.hello.first.v1.firstBpm.process.delegate.SayHalloTask;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "de.hello.first.v1.firstBpm.process.delegate")
@EnableProcessApplication("app-bpm")
@ComponentScan(basePackageClasses = {WebServiceAdapterConfig.class, AccountServiceEndpoint.class, CamundaConfiguration.class,
        AccountServiceConfiguration.class, AccountClient.class, HelloController.class, ProzessStarter.class, SayHalloTask.class,
        RestServiceConfiguration.class, MitarbeiterAendernService.class, VsnrAendernService.class, HelloProcessConfig.class, AsyncAntowrtBewertenTask.class})
public class FirstApp {
    public static void main(String[] args) {
        SpringApplication.run(FirstApp.class, args);
    }
}