package uk.co.brett.surviving.io;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class InputConfigTest {


    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void getTestFiles() {

        contextRunner.withPropertyValues("data.live=false")
                .withUserConfiguration(InputConfig.class)
                .run(context -> {
                    Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
                    assertThat(context).hasBean("getTestFiles");
                });

    }

    @Test
    void getFiles() {

        contextRunner.withPropertyValues("data.live=true")
                .withUserConfiguration(InputConfig.class)
                .run(context -> {
                    Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
                    assertThat(context).hasBean("getFiles");
                });

    }


}