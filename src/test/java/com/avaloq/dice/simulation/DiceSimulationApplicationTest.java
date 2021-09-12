package com.avaloq.dice.simulation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(classes = DiceSimulationApplication.class, webEnvironment = RANDOM_PORT)
class DiceSimulationApplicationTest {

    @Container
    private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer("mongo:4.4.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

    @BeforeAll
    static void setUpAll() {
        MONGO_DB_CONTAINER.start();
    }

    @AfterAll
    static void tearDownAll() {
        if (!MONGO_DB_CONTAINER.isShouldBeReused()) {
            MONGO_DB_CONTAINER.stop();
        }
    }

    @Test
    void testContext() throws InterruptedException {
        log.info("It's alive");
    }

}