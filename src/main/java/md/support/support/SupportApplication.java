package md.support.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.io.*;

@EnableScheduling
@SpringBootApplication
public class SupportApplication extends SpringBootServletInitializer {



    public static void main(String[] args) throws IOException {

        SpringApplication.run(SupportApplication.class, args);


    }


}
