package cn.ibabygroup.pros.member.web.resp;

/**
 * Created by tianmaogen on 2016/8/24.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class JsonResponse implements Serializable {

    private static final long serialVersionUID = -8201352503902193959L;
    @JsonProperty(value = "Code")
    private int code=200;
    @JsonProperty(value = "Success")
    private boolean success=true;
    @JsonProperty(value = "Msg")
    private String msg="执行成功！";
    @JsonProperty(value = "Data")
    private Object data;

    public JsonResponse(){
    }
    public JsonResponse(Object data) {
        this.data = data;
    }

    public JsonResponse(int code, String msg) {
        this.data = code;
        this.msg = msg;
    }

    public static JsonResponse ok(Object data) {
//        String str = JSONUtil.toJSONString(new IMJsonResponse(data));
//        return str;
        return new JsonResponse(data);
    }

    public static JsonResponse error(int Code, String Msg) {
        return new JsonResponse(Code, Msg);
    }


    public String toString() {
        return "JsonResponse{Code=" + this.code + ", Msg=\'" + this.msg + '\'' + ", Data=" + this.data + '}';
    }

//    static {
//        SUCCESS_CODE = HttpStatus.OK.value();
//        ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
//        BADREQUEST_CODE = HttpStatus.BAD_REQUEST.value();
//    }
}
