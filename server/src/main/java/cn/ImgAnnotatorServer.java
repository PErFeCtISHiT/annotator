package cn;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImgAnnotatorServer {

    public static void main(String[] args) {
        SpringApplication.run(ImgAnnotatorServer.class, args);
    }
}
