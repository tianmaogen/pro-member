package cn.ibabygroup.pros.member.model.net;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by tianmaogen on 2016/8/22.
 * .net URL常量
 */
@Component
public class NetURL {
    @Value("${ibabygroup.net.host}")
    private String host;
    private String getAixinbiUrl = "api/system/Integral/GetIntegral";
    private String operatePointsUrl = "api/system/Integral/Operate";

    public String getAixinbiUrl(String userId, int userType) {
        return host + getAixinbiUrl + "?userId=" + userId + "&userType=" + userType;
    }

    public String getReducePointsUrl(String userId, int userType, Long integral, String integralCode, String operateObject) {
        return host + operatePointsUrl + "?type=2"
                + "&userId=" + userId
                + "&userType=" + userType
                + "&integral=" + integral
                + "&integralCode=" + integralCode
                + "&operateObject=" + operateObject;
    }

    public String getAddPointsUrl(String userId, int userType, Long integral, String integralCode, String operateObject) {
        return host + operatePointsUrl + "?type=1"
                + "&userId=" + userId
                + "&userType=" + userType
                + "&integral=" + integral
                + "&integralCode=" + integralCode
                + "&operateObject=" + operateObject;
    }

}
