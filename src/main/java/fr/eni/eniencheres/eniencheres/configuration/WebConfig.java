package fr.eni.eniencheres.eniencheres.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Dossier par défaut (src/main/resources/static)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // Dossier personnalisé (par exemple, dans Documents)
        registry.addResourceHandler("/imagesUtilisateurs/**")
                .addResourceLocations("file:" + System.getProperty("user.home") + "/Documents/imagesUtilisateurs/");
    }
}