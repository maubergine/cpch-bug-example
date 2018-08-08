package com.github.maubergine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("bugged")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestBuggedApplication {

  @LocalServerPort
  private int port;

  private WebTestClient rest;

  @Before
  public void setUp() {
    rest = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
  }

  @Test
  public void testInContextIsOkWithBody() {
    rest.get()
        .uri("/context/hello")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class).isEqualTo("hello");
  }

  @Test
  public void testOutOfContextIsNotFound() {
    rest.get()
        .uri("/somewhere/else")
        .exchange()
        .expectStatus().isNotFound();
  }

}
