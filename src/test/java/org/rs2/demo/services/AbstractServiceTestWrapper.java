package org.rs2.demo.services;

import org.rs2.demo.DemoApplication;
import org.springframework.boot.test.context.SpringBootTest;

public abstract class AbstractServiceTestWrapper {

    @SpringBootTest(classes = DemoApplication.class)
    public static class AbstractServiceTest{}

}
