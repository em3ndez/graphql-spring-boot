package graphql.kickstart.autoconfigure.editor.voyager;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/** @author Max David Günther */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(value = "graphql.voyager.enabled", havingValue = "true")
@EnableConfigurationProperties(VoyagerPropertiesConfiguration.class)
public class ReactiveVoyagerAutoConfiguration {

  @Bean
  ReactiveVoyagerController voyagerController(VoyagerIndexHtmlTemplate voyagerIndexHtmlTemplate) {
    return new ReactiveVoyagerController(voyagerIndexHtmlTemplate);
  }

  @Bean
  public RouterFunction<ServerResponse> voyagerStaticFilesRouter() {
    return RouterFunctions.resources(
        "/vendor/voyager/**", new ClassPathResource("static/vendor/voyager/"));
  }

  @Bean
  VoyagerIndexHtmlTemplate voyagerIndexHtmlTemplate(
      final VoyagerPropertiesConfiguration voyagerPropertiesConfiguration) {
    return new VoyagerIndexHtmlTemplate(voyagerPropertiesConfiguration);
  }
}
