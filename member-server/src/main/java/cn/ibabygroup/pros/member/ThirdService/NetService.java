package cn.ibabygroup.pros.member.ThirdService;

import cn.ibabygroup.pros.member.model.exception.MemberException;
import cn.ibabygroup.pros.member.model.net.NetResponse;
import cn.ibabygroup.pros.member.model.net.NetURL;
import cn.ibabygroup.pros.member.utils.JSONUtil;
import cn.ibabygroup.pros.member.utils.RESTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tianmaogen on 2016/9/6.
 * 调用.net系统的service
 */
@Service
@Slf4j
public class NetService {
    @Autowired
    private NetURL netURL;

    /**
     * 获取爱心币
     * @return
     */
    public Long getAixinbi(String userId, int userType) {
        String url = netURL.getAixinbiUrl(userId, userType);
        NetResponse netResponse = RESTUtil.getForObject(url.toString(), null, NetResponse.class, true);
        if(!netResponse.getSuccess()) {
            log.error("调用.net的getAixinbi接口失败====>失败信息为:{}", JSONUtil.toCamelCaseJSONString(netResponse));
            throw new MemberException("调用.net的getAixinbi接口失败", JSONUtil.toCamelCaseJSONString(netResponse));
        }
        Long credits = Long.valueOf(netResponse.getData().toString());
        return credits;
    }

    public NetResponse reducePoints(String userId, int userType, Long integral, String integralCode, String operateObject) {
        String url = netURL.getReducePointsUrl(userId, userType, integral, integralCode, operateObject);
        NetResponse netResponse = RESTUtil.postForObject(url.toString(), null, NetResponse.class, true);
        if(!netResponse.getSuccess()) {
            log.error("调用.net的reducePoints接口失败====>失败信息为:{}", JSONUtil.toCamelCaseJSONString(netResponse));
            throw new MemberException("调用.net的reducePoints接口失败", JSONUtil.toCamelCaseJSONString(netResponse));
        }
        return netResponse;
    }

    public NetResponse addPoints(String userId, int userType, Long integral, String integralCode, String operateObject) {
        String url = netURL.getAddPointsUrl(userId, userType, integral, integralCode, operateObject);
        NetResponse netResponse = RESTUtil.postForObject(url.toString(), null, NetResponse.class, true);
        if(!netResponse.getSuccess()) {
            log.error("调用.net的addPoints接口失败====>失败信息为:{}", JSONUtil.toCamelCaseJSONString(netResponse));
            throw new MemberException("调用.net的addPoints接口失败", JSONUtil.toCamelCaseJSONString(netResponse));
        }
        return netResponse;
    }


}
