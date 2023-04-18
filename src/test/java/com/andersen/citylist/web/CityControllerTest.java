package com.andersen.citylist.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.andersen.citylist.config.filter.AuthFilter.X_APP_KEY_HEADER;
import static com.andersen.citylist.web.CityControllerTest.Initializer;
import static com.andersen.citylist.web.CityControllerTest.TEST_API_KEY;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestPropertySource(properties = "app.api-key=" + TEST_API_KEY)
@ContextConfiguration(initializers = {Initializer.class})
class CityControllerTest {
    public static final String TEST_API_KEY = "test-api-key";

    @Autowired
    private MockMvc mockMvc;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.7-alpine")
            .withDatabaseName("cities")
            .withUsername("postgres")
            .withPassword("postgres");


    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    void getCities_pageRequest() throws Exception {
        mockMvc.perform(
                        get("/api/v1/cities")
                                .param("page", "0")
                                .param("size", "3")
                )
                .andDo(print())
                .andExpect(jsonPath("$.content.length()", is(3)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Tokyo")))
                .andExpect(jsonPath("$.content[0].photo").exists())
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].name", is("Jakarta")))
                .andExpect(jsonPath("$.content[1].photo").exists())
                .andExpect(jsonPath("$.content[2].id", is(3)))
                .andExpect(jsonPath("$.content[2].name", is("Delhi")))
                .andExpect(jsonPath("$.content[2].photo").exists())
                .andExpect(jsonPath("$.totalPages", is(334)))
                .andExpect(jsonPath("$.totalElements", is(1000)))
                .andExpect(jsonPath("$.size", is(3)))
                .andExpect(jsonPath("$.pageable.pageNumber", is(0)))
                .andExpect(status().isOk());
    }

    @Test
    void getCities_searchRequest() throws Exception {
        mockMvc.perform(
                        get("/api/v1/cities")
                                .param("search", "Nil")
                )
                .andDo(print())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content[0].id", is(5)))
                .andExpect(jsonPath("$.content[0].name", is("Manila")))
                .andExpect(jsonPath("$.content[0].photo").exists())
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.pageable.pageNumber", is(0)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCity() throws Exception {
        mockMvc.perform(
                        put("/api/v1/cities/4")
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .header(X_APP_KEY_HEADER, TEST_API_KEY)
                                .content("{\"name\": \"Warsaw\", \"photo\": \"https://warsawImageUrl.png\"}")
                )
                .andDo(print())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("Warsaw")))
                .andExpect(jsonPath("$.photo", is("https://warsawImageUrl.png")))
                .andExpect(status().isOk());
    }

    @Test
    void updateCity_requiredParameterIsAbsent_400Http() throws Exception {
        mockMvc.perform(
                        put("/api/v1/cities/4")
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .header(X_APP_KEY_HEADER, TEST_API_KEY)
                                .content("{\"photo\": \"warsawImageUrl\"}")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCity_invalidApiKey_403Http() throws Exception {
        mockMvc.perform(
                        put("/api/v1/cities/4")
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .header(X_APP_KEY_HEADER, "invalid-api-key")
                                .content("{\"name\": \"Warsaw\", \"photo\": \"https://warsawImageUrl.png\"}")
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void updateCity_cityDoesNotExist_404Http() throws Exception {
        mockMvc.perform(
                        put("/api/v1/cities/1001")
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .header(X_APP_KEY_HEADER, TEST_API_KEY)
                                .content("{\"id\": 1, \"name\": \"Warsaw\", \"photo\": \"https://warsawImageUrl.png\"}")
                )
                .andDo(print())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("City not found")))
                .andExpect(status().isNotFound());
    }
}
