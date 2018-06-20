package cn.joker.util;

import cn.joker.namespace.StdName;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:47 2018/3/28
 */
public class JsonHelper {
    private static Logger logger = LoggerFactory.getLogger(JsonHelper.class);
    private static String dir = System.getProperty("user.dir") + "/annotator/";


    private JsonHelper() {
        throw new IllegalStateException(StdName.UTILCLASS);
    }

    /**
     * @author:pis
     * @description: 读取一个json
     * @date: 15:05 2018/4/13
     */
    public static JsonObject openJson(String fileName) {
        String path = dir + fileName;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = null;
        try (FileReader fileReader = new FileReader(path)) {
            jsonObject = (JsonObject) jsonParser.parse(fileReader);
        } catch (IOException e) {
            logger.error(StdName.ERROR);
        }


        return jsonObject;
    }

    /**
     * @author:pis
     * @description: 格式化json, 仅用于使用了jsonParser工具打开的json对象
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
        } catch (IOException e) {
            logger.error(StdName.ERROR);
        }
        return new JSONObject(jsonStr.toString());
    }

    /**
     * @author:pis
     * @description: 修改一个json
     * @date: 15:05 2018/4/13
     */
    public static boolean modifyJson(StringBuilder newJson, String name) {
        String path = dir + name;
        File file = new File(path);
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(newJson.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            return false;
        }
        return true;

    }


    public static void jsonToResponse(HttpServletResponse response, JSONObject jsonObject) {
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.append(jsonObject.toString());
        } catch (IOException e) {
            logger.error(StdName.ERROR);
        }
    }
}
