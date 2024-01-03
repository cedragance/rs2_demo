package org.rs2.demo.rest;

import org.springframework.boot.test.context.SpringBootTest;

public abstract class AbstractControllerTestWrapper {

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    public static class AbstractControllerTest {}

}
