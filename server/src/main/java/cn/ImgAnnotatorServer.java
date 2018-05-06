package cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "cn.joker.entity")
@EnableJpaRepositories(basePackages = "cn.joker.dao")
public class ImgAnnotatorServer {
    public static void main(String[] args) {
        SpringApplication.run(ImgAnnotatorServer.class, args);
    }
}
