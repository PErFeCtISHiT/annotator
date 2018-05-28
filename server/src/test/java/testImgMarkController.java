import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 10:11 2018/4/14
 */
public class testImgMarkController {
    /**
    *@author:pis
    *@description: 测试查看图片标注结果
    *@date: 10:11 2018/4/14
    */
    @Test
    public void testCheckImage(){
        String url = "http://localhost:8080/mark/checkImage";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"taskID\":1,\"users\":[{\"username\":\"kiki\"}],\"imgName\":\"b\"}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }

    /**
     *@author:pis
     *@description: 测试上传标记结果
     *@date: 10:11 2018/4/14
     */
    @Test
    public void testPostMark(){
        String url = "http://localhost:8080/mark/postMark";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"imgURL\":\"/b.png\",\"workerName\":\"kiki\",\"sponsorName\":\"sponsorName\",\"noteRectangle\":[{\"top\":287,\"left\":161,\"width\":52,\"height\":37,\"mark\":\"people\",\"id\":\"2018#03#20#12#24#35\"}],\"notePolygon\":[{\"points\":[{\"x\":\"\",\"y\":\"\"}],\"mark\":\"people\",\"id\":\"2018#03#20#12#24#35\"}],\"noteTotal\":[{\"mark\":\"fghgfh\",\"id\":\"2018#03#20#12#24#35\"}],\"taskID\":1}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
        jsonStr = "{\"imgURL\":\"/c.png\",\"workerName\":\"kiki\",\"sponsorName\":\"sponsorName\",\"noteRectangle\":[{\"top\":286,\"left\":161,\"width\":52,\"height\":37,\"mark\":\"people\",\"id\":\"2018#03#20#12#24#35\"}],\"notePolygon\":[{\"points\":[{\"x\":\"\",\"y\":\"\"}],\"mark\":\"people\",\"id\":\"2018#03#20#12#24#35\"}],\"noteTotal\":[{\"mark\":\"fghgfh\",\"id\":\"2018#03#20#12#24#35\"}],\"taskID\":1}";
        restTemplate = new RestTemplate();
        httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }

    /**
    *@author:pis
    *@description: 测试整合
    *@date: 20:14 2018/5/28
    */
    @Test
    public void testImageIntegration(){
        String url = "http://localhost:8080/mark/checkImageIntegration";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"taskID\":13,\"imgName\":\"magazine-unlock-01-2.3.962-_ee4b7571430d457ea642d.jpg\"}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }
}
