package manj.springframework.spring5webfluxrest.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;

import manj.springframework.spring5webfluxrest.domain.Category;
import manj.springframework.spring5webfluxrest.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CategoryControllerTest {

	WebTestClient webTestClient;

	CategoryRepository categoryRepository;

	CategoryController categoryController;

	@BeforeEach
	void setUp() throws Exception {
		categoryRepository = Mockito.mock(CategoryRepository.class);
		categoryController = new CategoryController(categoryRepository);
		webTestClient = WebTestClient.bindToController(categoryController).build();
	}

	@Test
	void testList() {
		BDDMockito.given(categoryRepository.findAll()).willReturn(Flux
				.just(Category.builder().description("Cat1").build(), Category.builder().description("Cat2").build()));

		webTestClient.get().uri("/api/v1/categories/").exchange().expectBodyList(Category.class).hasSize(2);
	}

	@Test
	void testGetById() {
		BDDMockito.given(categoryRepository.findById("someid"))
				.willReturn(Mono.just(Category.builder().description("Cat").build()));

		webTestClient.get().uri("/api/v1/categories/someid").exchange().expectBody(Category.class);
	}

	@Test
	void testCreate() {
		BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
				.willReturn(Flux.just(Category.builder().description("descrp").build()));

		Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Some Cat").build());

		webTestClient.post().uri("/api/v1/categories").body(catToSaveMono, Category.class).exchange().expectStatus()
				.isCreated();
	}

	@Test
	public void testUpdate() {
		given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

		Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("Some Cat").build());

		webTestClient.put().uri("/api/v1/categories/asdfasdf").body(catToUpdateMono, Category.class).exchange()
				.expectStatus().isOk();
	}

	@Test
	public void testPatchWithChanges() {
		given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().build()));

		given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

		Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("New Description").build());

		webTestClient.patch().uri("/api/v1/categories/asdfasdf").body(catToUpdateMono, Category.class).exchange()
				.expectStatus().isOk();

		verify(categoryRepository).save(any());
	}

	@Test
	public void testPatchNoChanges() {
		given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().build()));

		given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

		Mono<Category> catToUpdateMono = Mono.just(Category.builder().build());

		webTestClient.patch().uri("/api/v1/categories/asdfasdf").body(catToUpdateMono, Category.class).exchange()
				.expectStatus().isOk();

		verify(categoryRepository, never()).save(any());
	}

}
