package cn.ibabygroup.pros.member.model.net;

import lombok.Data;

/**
 * Created by tianmaogen on 2016/9/6.
 * .net接口返回消息
 */
@Data
public class NetResponse {
    //执行状态
    private Boolean Success;
    //返回消息
    private String Msg;
    //
    private Object Data;
    //执行结果 200 为正常
    private Integer Code;
}
