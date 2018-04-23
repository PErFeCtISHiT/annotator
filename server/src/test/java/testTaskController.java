import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class testTaskController {
    @Test
    public void testReleaseTask(){
        String url = "http://localhost:8080/task/releaseTask";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"sponsorName\":\"ki\",\"taskName\":\"人物标注\",\"description\":\"标出图片中的人\",\"tag\":[\"人物\",\"旅游\"],\"workerLevel\":2,\"points\":123,\"expectedNumber\":10,\"endDate\":\"2018#05#24#23#59#59\",\"imgNum\":12}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }

    @Test
    public void testModifyTask(){
        String url = "http://localhost:8080/task/modifyTask";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"taskID\":12,\"taskName\":\"人物\",\"description\":\"划出图片中的人\",\"tag\":\"人物\",\"endDate\":\"2019-03-23 23#59#59\",\"expectedNumber\":20,\"points\":150}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }

    @Test
    public void testCheckMyTask(){
        String url = "http://localhost:8080/task/myTasks";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"userName\":null,\"status\":0,\"userRole\":1}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }


}
