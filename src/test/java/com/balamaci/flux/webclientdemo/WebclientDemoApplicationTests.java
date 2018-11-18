package com.balamaci.flux.webclientdemo;

import com.balamaci.flux.webclientdemo.order.Order;
import com.balamaci.flux.webclientdemo.user.User;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class WebclientDemoApplicationTests {

	@LocalServerPort
	private Long port;

	private WebClient webClient() {
		return WebClient.builder()
				.baseUrl("http://localhost:" + port)
				.build();
	}

	@Test
	public void retrieveUsers() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		Flux<User> users = webClient()
				.get()
				.uri("/users")
				.retrieve()
				.bodyToFlux(User.class);

		users.subscribe(user -> log.info("User {}", user),
				(ex) -> log.error("Received exception", ex), latch::countDown);
		latch.await();
	}

	@Test
	public void retrieveAllUsersOrders() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		Flux<User> users = webClient()
				.get()
				.uri("/users")
				.retrieve()
				.bodyToFlux(User.class);

		Flux<Pair<User, Order>> orders = users.flatMap(user -> webClient()
				.get()
				.uri("/orders/{username}", user.getUsername())
				.retrieve()
				.bodyToFlux(Order.class)
				.defaultIfEmpty(Order.NOT_FOUND)
				.map(order -> new Pair<>(user, order))
		);
		orders.subscribe(user -> log.info("Received {}", user),
				(ex) -> log.error("Received exception", ex), latch::countDown);
		latch.await();
	}

	@Test
	public void retrieveAllUsersIds() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		Flux<String> users = webClient()
				.get()
				.uri("/users/ids")
				.retrieve()
				.bodyToFlux(String.class);


		orders.subscribe(user -> log.info("Received {}", user),
				(ex) -> log.error("Received exception", ex), latch::countDown);
		latch.await();
	}

}
