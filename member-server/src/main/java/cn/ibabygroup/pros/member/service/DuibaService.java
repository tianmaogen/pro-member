package cn.ibabygroup.pros.member.service;

import cn.com.duiba.credits.sdk.CreditConsumeResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tianmaogen on 2016/10/13.
 */
public interface DuibaService {
    /**
     * 生成免登陸url
     * @param oriUrl 原始url
     * @param userId 用戶Id
     * @param userType 用戶类型 孕妈1，医生2，系统3
     * @return
     */
    String buildUrl(String oriUrl, String userId, int userType);

    /**
     * 扣除会员积分
     * @param request
     */
    CreditConsumeResult deductPoints(HttpServletRequest request);

    /**
     * 兑吧回调接口
     * @param request
     */
    void duibaCallBack(HttpServletRequest request);
}
