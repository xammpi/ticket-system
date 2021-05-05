package md.support.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


import java.io.*;


@SpringBootApplication
public class SupportApplication extends SpringBootServletInitializer {
    
    public static void main(String[] args) throws IOException {

        SpringApplication.run(SupportApplication.class, args);


    }


}
