package com.github.maubergine;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
@RestController
public class TestController {

  @GetMapping("/hello")
  public Mono<String> hello() {
    return Mono.just("hello");
  }

}
