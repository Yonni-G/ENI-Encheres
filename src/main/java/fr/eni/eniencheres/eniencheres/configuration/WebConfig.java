package fr.eni.eniencheres.eniencheres.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/imagesUtilisateurs/**")
                .addResourceLocations("file:C:/Users/acoulon2024/Desktop/Encheres ENI/imagesUtilisateurs/")
                .setCachePeriod(0); // Désactive le cache
    }
}
