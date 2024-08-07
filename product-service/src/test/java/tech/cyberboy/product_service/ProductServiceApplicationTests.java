package tech.cyberboy.product_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tech.cyberboy.product_service.dto.ProductRequest;
import tech.cyberboy.product_service.dto.ProductResponse;
import tech.cyberboy.product_service.model.Product;
import tech.cyberboy.product_service.repository.ProductRepository;
import tech.cyberboy.product_service.service.ProductService;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;
//	@MockBean
//	private ProductService productService;

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {

		ProductRequest productRequest = ProductRequest.builder()
				.name("iPhone 15 Pro Max")
				.description("This is the description of product iPhone 15 Pro Max")
				.price(BigDecimal.valueOf(1200))
				.build();

		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
					.contentType(MediaType.APPLICATION_JSON)
					.content(productRequestString))
				.andExpect(status().isCreated());

		Assertions.assertEquals(1, productRepository.findAll().size());
	}

//	@Test
//	void shouldGetAllProducts() throws Exception {
//		ProductResponse product1 = ProductResponse.builder()
//				.id("1234")
//				.name("iPhone 15 Pro Max")
//				.description("This is the description of product iPhone 15 Pro Max")
//				.price(BigDecimal.valueOf(1200))
//				.build();
//
//		ProductResponse product2 = ProductResponse.builder()
//				.id("12345")
//				.name("Samsung S24 Ultra")
//				.description("This is the description of product Samsung S24 Ultra")
//				.price(BigDecimal.valueOf(2100))
//				.build();
//
//		Mockito.when(productService.getAllProducts()).thenReturn(asList(product1, product2));
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.size()", Matchers.is(2)))
//				.andExpect(jsonPath("$[0].id", Matchers.is("1234")))
//				.andExpect(jsonPath("$[0].name", Matchers.is("iPhone 15 Pro Max")))
//				.andExpect(jsonPath("$[0].description", Matchers.is("This is the description of product iPhone 15 Pro Max")))
//				.andExpect(jsonPath("$[0].price", Matchers.is(1200)))
//				.andExpect(jsonPath("$[1].id", Matchers.is("12345")))
//				.andExpect(jsonPath("$[1].name", Matchers.is("Samsung S24 Ultra")))
//				.andExpect(jsonPath("$[1].description", Matchers.is("This is the description of product Samsung S24 Ultra")))
//				.andExpect(jsonPath("$[1].price", Matchers.is(2100)));
//	}

}
