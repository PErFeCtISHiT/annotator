import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 17:09 2018/3/22
 */


public class testUserController {
    /**
    *@author:pis
    *@description: 测试注册
    *@date: 18:16 2018/3/22
    */
    @Test
    public void testAddUser(){
        String url = "http://localhost:8080/user/signUp";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"username\":\"pis\",\"passwr\":\"123\",\"nickname\":\"test\"}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);

    }

    /**
    *@author:pis
    *@description: 测试修改 test->okk 123->1234
    *@date: 13:55 2018/4/13
    */
    @Test
    public void testModifyUser(){
        String url = "http://localhost:8080/user/modify";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"username\":\"yaphet\",\"passwr\":\"1234\",\"nickname\":\"ok\",\"email\":\"123\"}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }

    /**
    *@author:pis
    *@description: 测试登陆
    *@date: 13:55 2018/4/13
    */
    @Test
    public void testLogin(){
        String url = "http://localhost:8080/user/login";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"username\":\"yaphet\",\"passwr\":\"1234\"}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);
        jsonStr = "{\"username\":\"somnus\",\"passwr\":\"1234\"}";
        httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);
        jsonStr = "{\"username\":\"somnu\",\"passwr\":\"123\"}";
        httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);
    }

    /**
    *@author:pis
    *@description: 测试积分提现充值 +1234 -1233
    *@date: 14:42 2018/4/13
    */

    @Test
    public void testChangePoints(){
        String url = "http://localhost:8080/user/changePoints";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"username\":\"admin\",\"points\":9999}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
        jsonStr = "{\"username\":\"admin\",\"points\":9999}";
        restTemplate = new RestTemplate();
        httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }

    /**
     *@author:pis
     *@description: 测试修改积分 123 1234
     *@date: 14:42 2018/4/13
     */

    @Test
    public void testManagePoints(){
        String url = "http://localhost:8080/user/managePoints";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"username\":\"test\",\"points\":12345678}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }
    /**
    *@author:pis
    *@description: 测试授予积分奖励
    *@date: 14:52 2018/4/13
    */
    @Test
    public void testBonus(){
        String url = "http://localhost:8080/user/Bonus";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"taskID\":123,\"workerName\":\"somnus\",\"points\":9999}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);

    }

    /**
    *@author:pis
    *@description: 测试得到所有用户
    *@date: 18:43 2018/4/13
    */
    @Test
    public void testcheckUser(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/user/checkUser";
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }


    /**
     *@author:pis
     *@description: 测试得到当前用户
     *@date: 18:43 2018/4/13
     */
    @Test
    public void testGetCurrentUser(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/user/getCurrentUser";
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }
    /**
     *@author:pis
     *@description: 测试删除用户
     *@date: 18:43 2018/4/13
     */
    @Test
    public void testDeleteUser(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/user/deleteUser?username=pis";
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }

    /**
     *@author:pis
     *@description: 测试查找用户
     *@date: 18:43 2018/4/13
     */
    @Test
    public void testFindUser(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/user/findUser?username=test";
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
        url = "http://localhost:8080/user/findUser?username=somnus";
        jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }

}
