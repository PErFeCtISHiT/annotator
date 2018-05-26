import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

public class ClassificationTest {

    @Test
    public void get() {
        String url = "http://localhost:8080/test/1";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);

        System.out.println(jsonData);
    }
}