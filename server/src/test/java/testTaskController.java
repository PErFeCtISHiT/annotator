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
        requestHeaders.set("Content-Type", "application/json; charset=utf-8");

        String jsonStr = "{\"sponsorName\":\"kiii\",\"taskName\":\"人物标注\",\"description\":\"标出图片中的人\",\"tag\":[\"人物\",\"旅游\"],\"workerLevel\":2,\"points\":123,\"expectedNumber\":10,\"endDate\":\"2018-05-24 23:59:59\",\"imgNum\":12}";
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
        requestHeaders.set("Content-Type", "application/json; charset=utf-8");

        String jsonStr = "{\"taskID\":1,\"taskName\":\"人-\",\"description\":\"划出图片中的人\",\"tag\":[\"人物\"],\"endDate\":\"2019-03-23 23:59:59\",\"expectedNumber\":20,\"points\":150}";
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

        String jsonStr = "{\"username\":\"catt\",\"status\":0,\"userRole\":2}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);

        System.out.println(jsonData);
    }


    @Test
    public void testCheckAllTask(){
        String url = "http://localhost:8080/task/allTasks";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json");

        String jsonStr = "{\"tag\":\"\",\"status\":0,\"userRole\":1}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);


        jsonStr = "{\"tag\":\"111\",\"status\":0,\"userRole\":1}";
        httpEntity = new HttpEntity<>(jsonStr, requestHeaders);
        jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testEndTask(){
        String url = "http://localhost:8080/task/endTask?taskID=16";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testDeleteTask(){
        String url = "http://localhost:8080/task/deleteTask?taskID=1";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testCompleteTask(){
        String url = "http://localhost:8080/task/completeTask?taskID=1&username=kiki";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testAbortTask(){
        String url = "http://localhost:8080/task/abortTask?taskID=1&username=kiki";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }
    @Test
    public void testAcceptTask(){
        String url = "http://localhost:8080/task/acceptTask?taskID=1&username=kiki";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testReportTask(){
        String url = "http://localhost:8080/task/reportTask";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json; charset=utf-8");

        String jsonStr = "{\"respondent\":\"somnus\",\"reporter\":\"kiki\",\"taskID\":1,\"taskName\":\"人物标注\",\"description\":\"emmmm\"}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testReportWorker(){
        String url = "http://localhost:8080/task/reportWorker";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json; charset=utf-8");

        String jsonStr = "{\"respondent\":\"somnus\",\"reporter\":\"kiki\",\"taskID\":1,\"taskName\":\"人物标注\",\"description\":\"emmmm\"}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testCheckWorkerReport(){
        String url = "http://localhost:8080/task/checkWorkerReport";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testCheckTaskReport(){
        String url = "http://localhost:8080/task/checkTaskReport";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }


    @Test
    public void testDealReport(){
        String url = "http://localhost:8080/task/dealReport";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "text/plain");
        requestHeaders.set("Content-Type", "application/json; charset=utf-8");

        String jsonStr = "{\"reportTime\":\"2018-04-24 16:08:02\",\"Type\":0,\"description\":\"ttt\"}";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testTaskDetail(){
        String url = "http://localhost:8080/task/checkTaskDetail?taskID=1";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testCheckTaskDetail(){
        String url = "http://localhost:8080/task/checkTaskDetail?taskID=1";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }

    @Test
    public void testCheckImages(){
        String url = "http://localhost:8080/task/checkImages?taskID=1";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);
        System.out.println(jsonData);
    }
}
