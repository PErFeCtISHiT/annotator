import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 16:18 2018/4/15
 */
public class testStatisticController {
    /**
    *@author:pis
    *@description: 测试查看工人奖励
    *@date: 16:19 2018/4/15
    */
    @Test
    public void testCheckImage(){
        String url = "http://localhost:8080/statistic/checkBonus?username=test";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);

        System.out.println(jsonData);
    }
    /**
    *@author:pis
    *@description: 测试查看排名
    *@date: 21:37 2018/4/17
    */
    @Test
    public void testCheckRank(){
        String url = "http://localhost:8080/statistic/checkRanking";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);

        System.out.println(jsonData);
    }

    /**
    *@author:pis
    *@description: 测试查看用户
    *@date: 21:54 2018/4/17
    */
    @Test
    public void testCheckUserNum(){
        String url = "http://localhost:8080/statistic/checkUserNum";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);

        System.out.println(jsonData);
    }

    /**
     *@author:pis
     *@description: 测试查看任务
     *@date: 21:54 2018/4/17
     */
    @Test
    public void testCheckTaskNum(){
        String url = "http://localhost:8080/statistic/checkTaskNum";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url,String.class);

        System.out.println(jsonData);
    }
}
