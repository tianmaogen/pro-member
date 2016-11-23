package cn.ibabygroup.pros.member.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * Created by tianmaogen on 2016/8/18.
 */
@Slf4j
public class RESTUtil {
    private static RestTemplate rest ;

    static {
        rest = new RestTemplate();
        rest.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    public static <T> T postForObject(String url,Object object, Class<T> responseType) {
        return postForObject(url, object, responseType, false);
    }

    public static <T> T postForObject(String url,Object object, Class<T> responseType, boolean camelCaseFlag) {
        HttpHeaders headers = new HttpHeaders();
        return postForObject(url, object, responseType, headers, camelCaseFlag);
    }

    public static <T> T postForObject(String url,Object object, Class<T> responseType, HttpHeaders headers) {
        return postForObject(url, object, responseType, headers, false);
    }


    public static <T> T postForObject(String url,Object object, Class<T> responseType, HttpHeaders headers, boolean camelCaseFlag) {
        return forObject(url, object, responseType, headers, camelCaseFlag, HttpMethod.POST);
    }

    public static <T> T getForObject(String url,Object object, Class<T> responseType, boolean camelCaseFlag) {
        HttpHeaders headers = new HttpHeaders();
        return forObject(url, object, responseType, headers, camelCaseFlag, HttpMethod.GET);
    }

    public static <T> T getForObject(String url,Object object, Class<T> responseType, HttpHeaders headers, boolean camelCaseFlag) {
        return forObject(url, object, responseType, headers, camelCaseFlag, HttpMethod.GET);
    }

    public static <T> T forObject(String url,Object object, Class<T> responseType, HttpHeaders headers, boolean camelCaseFlag, HttpMethod method) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonObj;
        if(camelCaseFlag)
            jsonObj = JSONUtil.toCamelCaseJSONString(object);
        else
            jsonObj = JSONUtil.toPascalCaseJSONString(object);

        HttpEntity<String> request = new HttpEntity<>(jsonObj, headers);
        log.info("rest请求=============>url:{},jsonObj:{}", url, jsonObj);
        ResponseEntity<String> response = rest.exchange(url, method, request, String.class);
        log.info("rest响应=============>response:{}", JSONUtil.toCamelCaseJSONString(response));
        if(response.getStatusCode() == HttpStatus.OK) {
            T t = JSON.parseObject(response.getBody(), responseType);
            return t;
        }
        else
            return null;
    }
}
