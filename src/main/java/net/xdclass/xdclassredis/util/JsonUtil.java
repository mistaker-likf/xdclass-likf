package net.xdclass.xdclassredis.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

public class JsonUtil {


    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * 把对象转字符串
     * @param data
     * @return
     */
    public static String objectToJson(Object data){
        try {

            return MAPPER.writeValueAsString(data);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * json字符串转对象
     * @param jsonData
     * @param beanType
     * @param <T>
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType){

        try {
            T t = MAPPER.readValue(jsonData,beanType);
            return t;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }

}
