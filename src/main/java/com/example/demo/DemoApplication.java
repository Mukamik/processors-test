package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.context.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;

@SpringBootApplication
@RestController
public class DemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    private static HashMap<String, String> availableProcessors = new LinkedHashMap<>();

    public static void main(String[] args) {
        recordEvent("Start of main");
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return availableProcessors.toString().replace(",", "<br>");
    }

    //	The following events are in order that they occur
//	https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.spring-application.application-events-and-listeners
    @EventListener(ApplicationStartingEvent.class)
    public void starting() {
        recordEvent("ApplicationStartingEvent");
    }

    @EventListener(ApplicationEnvironmentPreparedEvent.class)
    public void environmentPrepared() {
        recordEvent("ApplicationEnvironmentPreparedEvent");
    }

    @EventListener(ApplicationContextInitializedEvent.class)
    public void initialized() {
        recordEvent("ApplicationContextInitializedEvent");
    }

    @EventListener(ApplicationPreparedEvent.class)
    public void prepared() {
        recordEvent("ApplicationPreparedEvent");
    }

    @EventListener(ApplicationStartedEvent.class)
    public void started() {
        recordEvent("ApplicationStartedEvent");
    }

    @EventListener(AvailabilityChangeEvent.class)
    public void live() {
        recordEvent("AvailabilityChangeEvent - live");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        recordEvent("ApplicationReadyEvent");
    }

    @EventListener(AvailabilityChangeEvent.class)
    public void acceptingTraffic() {
        recordEvent("AvailabilityChangeEvent - accepting traffic");
    }

    //	Included for completeness, likely will never occur.
    @EventListener(ApplicationFailedEvent.class)
    public void failed() {
        recordEvent("ApplicationFailedEvent");
    }

    private static void recordEvent(String name) {
        String event = getRecordOfProcessors();
        availableProcessors.put(name, event);
        logger.info(event);
    }

    private static String getRecordOfProcessors() {
        return Runtime.getRuntime().availableProcessors() + " @ " + Instant.now();
    }

}
