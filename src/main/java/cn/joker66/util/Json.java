package cn.joker66.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:47 2018/3/28
 */
public class Json {
    /**
     * @author:pis
     * @description: 读取一个json
     * @date: 15:05 2018/4/13
     */
    public static JsonObject openJson(String fileName) {
        String path = System.getProperty("user.dir") + "/src/main/resources/static/" + fileName;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = null;
        try (FileReader fileReader = new FileReader(path)) {
            jsonObject = (JsonObject) jsonParser.parse(fileReader);
            System.out.println(jsonObject.toString());
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    /**
     * @author:pis
     * @description: 格式化json,仅用于使用了jsonParser工具打开的json对象
     * @date: 15:05 2018/4/13
     */
    @org.jetbrains.annotations.NotNull
    public static String format(String str) {
        return str.substring(1, str.length() - 1);
    }

    public static JSONObject requestToJson(HttpServletRequest request) {
        StringBuilder jsonStr = new StringBuilder();
        try (InputStream inputStream = request.getInputStream()) {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                jsonStr.append(inputStr);
            }
            streamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject(jsonStr.toString());
    }

    /**
     * @author:pis
     * @description: 修改一个json
     * @date: 15:05 2018/4/13
     */
    public static boolean modifyJson(StringBuilder newJson, String name) {
        String path = System.getProperty("user.dir") + "/src/main/resources/static/" + name;
        File file = new File(path);
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(newJson.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }


    public static void JsonToResponse(HttpServletResponse response, JSONObject jsonObject) {
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.append(jsonObject.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
