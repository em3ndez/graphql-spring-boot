package graphql.kickstart.autoconfigure.web.servlet;

import graphql.kickstart.autoconfigure.web.OnSchemaOrSchemaProviderBean;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.web.servlet.DispatcherServlet;

@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = Type.SERVLET)
@Conditional(OnSchemaOrSchemaProviderBean.class)
@ConditionalOnProperty(
    value = "graphql.servlet.enabled",
    havingValue = "true",
    matchIfMissing = true)
@AutoConfigureBefore(GraphQLWebAutoConfiguration.class)
@ConditionalOnClass({DispatcherServlet.class, DefaultAuthenticationEventPublisher.class})
@EnableConfigurationProperties({GraphQLServletProperties.class, AsyncServletProperties.class})
public class GraphQLWebSecurityAutoConfiguration {

  private final GraphQLServletProperties graphqlServletProperties;
  private final AsyncServletProperties asyncServletProperties;

  @Bean("graphqlAsyncTaskExecutor")
  @ConditionalOnMissingBean(name = "graphqlAsyncTaskExecutor")
  public Executor threadPoolTaskExecutor() {
    if (isAsyncModeEnabled() && asyncServletProperties.isDelegateSecurityContext()) {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(asyncServletProperties.getThreads().getMin());
      executor.setMaxPoolSize(asyncServletProperties.getThreads().getMax());
      executor.setThreadNamePrefix(asyncServletProperties.getThreads().getNamePrefix());
      executor.initialize();
      return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
    return null;
  }

  private boolean isAsyncModeEnabled() {
    return graphqlServletProperties.getAsyncModeEnabled() != null
        ? graphqlServletProperties.getAsyncModeEnabled()
        : asyncServletProperties.isEnabled();
  }
}
