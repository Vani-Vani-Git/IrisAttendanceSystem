package com.vani.irisapi;
import nu.pattern.OpenCV;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IrisapiApplication {

    static {
        OpenCV.loadLocally();
    }
	public static void main(String[] args) {
        SpringApplication.run(IrisapiApplication.class, args);
	}
}
