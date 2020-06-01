package com.pulawskk.sportseventapi;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.repository.GameRepository;
import com.pulawskk.sportseventapi.repository.OddRepository;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@EnableScheduling
@EnableSwagger2
public class AppConfig {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private OddRepository oddRepository;

    @PostConstruct
    private void postConstruct() {
        List<Game> gamesToBeDeleted = gameRepository.findAllGamesToBeDeleted(100);
        gameRepository.deleteAll(gamesToBeDeleted);
    }

    @Bean
    public CloseableHttpClient closeableHttpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public HttpPost httpPost() {
        return new HttpPost();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
