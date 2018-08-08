package com.github.maubergine;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ContextPathCompositeHandler;
import org.springframework.http.server.reactive.HttpHandler;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
@Configuration
public class ReactiveWebServerFactoryConfiguration {

  private static final String CONTEXT = "/context";

  @Bean
  @Profile("bugged")
  public NettyReactiveWebServerFactory buggedWebServerFactory() {
    return new NettyReactiveWebServerFactory() {
      @Override
      public WebServer getWebServer(HttpHandler httpHandler) {
        Map<String, HttpHandler> handlerMap = new HashMap<>();
        handlerMap.put(CONTEXT, httpHandler);
        return super.getWebServer(new ContextPathCompositeHandler(handlerMap));
      }
    };
  }

  @Bean
  @Profile("fixed")
  public NettyReactiveWebServerFactory fixedReactiveWebServerFactory() {
    return new NettyReactiveWebServerFactory() {
      @Override
      public WebServer getWebServer(HttpHandler httpHandler) {
        Map<String, HttpHandler> handlerMap = new HashMap<>();
        handlerMap.put(CONTEXT, httpHandler);
        return super.getWebServer(new FixedContextPathCompositeHandler(handlerMap));
      }
    };
  }

}
