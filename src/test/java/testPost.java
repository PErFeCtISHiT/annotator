import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 17:09 2018/3/22
 */


public class testPost {
    /**
    *@author:pis
    *@description: 测试注册
    *@date: 18:16 2018/3/22
    */
    @Test
    public void testAdd(){
        String url = "http://localhost:8080/signUp";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"username\":\"whisky\",\"password\":\"123\",\"name\":\"whisky\",\"roleList\":[3]}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);

    }

}
