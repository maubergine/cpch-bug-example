package com.github.maubergine;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FixedContextPathCompositeHandler implements HttpHandler {

  private final Map<String, HttpHandler> handlerMap;


  public FixedContextPathCompositeHandler(Map<String, ? extends HttpHandler> handlerMap) {
    Assert.notEmpty(handlerMap, "Handler map must not be empty");
    this.handlerMap = initHandlers(handlerMap);
  }

  private static Map<String, HttpHandler> initHandlers(Map<String, ? extends HttpHandler> map) {
    map.keySet().forEach(FixedContextPathCompositeHandler::assertValidContextPath);
    return new LinkedHashMap<>(map);
  }

  private static void assertValidContextPath(String contextPath) {
    Assert.hasText(contextPath, "Context path must not be empty");
    if (contextPath.equals("/")) {
      return;
    }
    Assert.isTrue(contextPath.startsWith("/"), "Context path must begin with '/'");
    Assert.isTrue(!contextPath.endsWith("/"), "Context path must not end with '/'");
  }


  @Override
  public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
    // Remove underlying context path first (e.g. Servlet container)
    String path = request.getPath().pathWithinApplication().value();
    return this.handlerMap.entrySet().stream()
                          .filter(entry -> path.startsWith(entry.getKey()))
                          .findFirst()
                          .map(entry -> {
                            String contextPath = request.getPath().contextPath().value() + entry.getKey();
                            ServerHttpRequest newRequest = request.mutate().contextPath(contextPath).build();
                            return entry.getValue().handle(newRequest, response);
                          })
                          .orElseGet(() -> {
                            response.setStatusCode(HttpStatus.NOT_FOUND);
                            return Mono.defer(response::setComplete);
                          });
  }

}
