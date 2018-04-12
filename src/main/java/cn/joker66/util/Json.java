package cn.joker66.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.*;


/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:47 2018/3/28
 */
public class Json {
    public static JsonObject openJson(String fileName){
        String path = System.getProperty("user.dir") + "/src/main/resources/static/json" + fileName;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = null;
        try (FileReader fileReader = new FileReader(path)){
            jsonObject = (JsonObject) jsonParser.parse(fileReader);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
    @org.jetbrains.annotations.NotNull
    public static String format(String str){
        return str.substring(1,str.length() - 1);
    }
    public static JSONObject requestToJson(HttpServletRequest request){
        StringBuilder jsonStr = new StringBuilder();
        try(InputStream inputStream = request.getInputStream()) {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                jsonStr.append(inputStr);
            }
            streamReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(jsonStr.toString());
        return jsonObject;
    }
}
