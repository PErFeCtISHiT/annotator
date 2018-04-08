package cn.joker66.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


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
        try {
            FileReader fileReader = new FileReader(path);
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
}
