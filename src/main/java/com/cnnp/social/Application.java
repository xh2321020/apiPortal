package com.cnnp.social;

import com.cnnp.social.base.BaseSetting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableCaching
@EnableConfigurationProperties({BaseSetting.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/403.html");
                ErrorPage error404Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.jsp");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
                container.addErrorPages(error403Page, error404Page, error500Page);
            }


        };
    }
}
