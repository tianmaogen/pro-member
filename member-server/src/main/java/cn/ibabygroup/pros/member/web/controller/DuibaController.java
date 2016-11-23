package cn.ibabygroup.pros.member.web.controller;

import cn.com.duiba.credits.sdk.CreditConsumeResult;
import cn.ibabygroup.commons.web.annotation.LoginRequired;
import cn.ibabygroup.pros.member.service.DuibaService;
import cn.ibabygroup.pros.member.utils.JSONUtil;
import cn.ibabygroup.pros.member.web.resp.JsonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tianmaogen on 2016-10-13
 */
@RestController
@RequestMapping("/cloud/member/v1/duiba")
@Api(value = "兌吧的操作", description = "兌吧的操作controller", position = 1)
@Slf4j
public class DuibaController {

    @Autowired
    private DuibaService duibaService;

    @RequestMapping(value = "/buildUrl/{oriUrl}/{userId}/{userType}", method = RequestMethod.GET)
    @ApiOperation(value = "生成url", notes = "生成新的url")
    @ResponseBody
    public JsonResponse buildUrl(@ApiParam(value = "原始url(比如http://www.duiba.com.cn/autoLogin/autologin------------->autoLogin/autologin)。如果为null传入字符串NULL", required = false)
                                 @PathVariable String oriUrl,
                                 @ApiParam(value = "userId", required = true)
                                 @PathVariable String userId,
                                 @ApiParam(value = "userType:孕妈1，医生2，系统3", required = true)
                                 @PathVariable int userType) {

        log.info("buildUrl=====>请求实体为:{}", oriUrl);
        return JsonResponse.ok(duibaService.buildUrl(oriUrl, userId, userType));
    }

    @LoginRequired(required = false)
    @RequestMapping(value = "/deductPoints", method = RequestMethod.GET)
    @ApiOperation(value = "扣除积分", notes = "扣除积分接口,提供给兑吧调用")
    @ResponseBody
    public String deductPoints(HttpServletRequest request) {
        CreditConsumeResult result = duibaService.deductPoints(request);
        log.info("deductPoints======>响应实体为:{}", JSONUtil.toCamelCaseJSONString(result));
        return result.toString();
    }

    @LoginRequired(required = false)
    @RequestMapping(value = "/duibaCallBack", method = RequestMethod.GET)
    @ApiOperation(value = "兑吧结果回调", notes = "扣除积分之后，兑吧回掉")
    @ResponseBody
    public String duibaCallBack(HttpServletRequest request) {
        duibaService.duibaCallBack(request);
        return "ok";
    }
}
