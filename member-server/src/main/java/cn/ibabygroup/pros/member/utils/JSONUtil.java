package cn.ibabygroup.pros.member.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;

/**
 * Created by tianmaogen on 2016/8/18.
 *
 */
public class JSONUtil {
    public static SerializeConfig pascalCaseConfig;
    public static SerializeConfig camelCaseConfig;
    static {
        pascalCaseConfig = new SerializeConfig();
        camelCaseConfig = new SerializeConfig();
        /**
             CamelCase	persionId
             PascalCase	PersonId
             SnakeCase	person_id
             KebabCase	person-id
         */
        pascalCaseConfig.propertyNamingStrategy = PropertyNamingStrategy.PascalCase;
        camelCaseConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
    }

    public static String toPascalCaseJSONString(Object o) {
        String jsonObj = JSON.toJSONString(o, pascalCaseConfig);
        return jsonObj;
    }

    public static String toCamelCaseJSONString(Object o) {
        String jsonObj = JSON.toJSONString(o, camelCaseConfig);
        return jsonObj;
    }

    public static JSONObject parseObject(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        return jsonObject;
    }

//    public static void main(String[] args) {
//        JSONObject jsonObject = JSON.parseObject("{\"text\":\"123\"}");
//        String text = (String) jsonObject.get("text");
//        System.out.println(text);
//    }
}
