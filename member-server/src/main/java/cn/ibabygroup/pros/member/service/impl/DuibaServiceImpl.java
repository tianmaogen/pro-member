package cn.ibabygroup.pros.member.service.impl;

import cn.com.duiba.credits.sdk.CreditConsumeParams;
import cn.com.duiba.credits.sdk.CreditConsumeResult;
import cn.com.duiba.credits.sdk.CreditNotifyParams;
import cn.com.duiba.credits.sdk.CreditTool;
import cn.ibabygroup.pros.member.ThirdService.NetService;
import cn.ibabygroup.pros.member.mapper.DuibaOrdersMapper;
import cn.ibabygroup.pros.member.mapper.MybatisHelper;
import cn.ibabygroup.pros.member.model.duiba.DuibaInfo;
import cn.ibabygroup.pros.member.model.mysql.DuibaOrders;
import cn.ibabygroup.pros.member.model.net.NetURL;
import cn.ibabygroup.pros.member.service.DuibaService;
import cn.ibabygroup.pros.member.utils.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tianmaogen on 2016/10/13.
 */
@Service
@Data
@Slf4j
public class DuibaServiceImpl implements DuibaService{

    @Autowired
    private DuibaInfo duibaInfo;
    @Autowired
    private NetService netService;
    @Autowired
    private MybatisHelper mybatisHelper;

    /**
     * 生成免登陸url
     * @param oriUrl 原始url
     * @param userId 用戶Id
     * @return
     */
    @Override
    public String buildUrl(String oriUrl, String userId, int userType) {
        CreditTool tool=new CreditTool(duibaInfo.getAppKey(), duibaInfo.getAppSecret());
        Map params=new HashMap();
        //游客模式
        if(userId.equals("not_login")) {
            params.put("uid",userId);
            params.put("credits", "0");
        }
        else {
            params.put("uid",userId + ":" + userType);
            Long credits = netService.getAixinbi(userId, userType);
            params.put("credits", credits.toString());
        }

        if(!oriUrl.equals("NULL")){
            String redirect = "http://www.duiba.com.cn/" + oriUrl;
            //redirect是目标页面地址，默认积分商城首页是：http://www.duiba.com.cn/chome/index
            //此处请设置成一个外部传进来的参数，方便运营灵活配置
            params.put("redirect",redirect);
        }

        String url=tool.buildUrlWithSign("http://www.duiba.com.cn/autoLogin/autologin?",params);
        return url;
    }

    @Override
    public CreditConsumeResult deductPoints(HttpServletRequest request) {
        CreditTool tool = new CreditTool(duibaInfo.getAppKey(), duibaInfo.getAppSecret());
        CreditConsumeResult result = new CreditConsumeResult(true);
        try {
            CreditConsumeParams params= tool.parseCreditConsume(request);//利用tool来解析这个请求
            log.info("deductPoints=====>请求实体为:{}", JSONUtil.toCamelCaseJSONString(params));
            String[] uidStrArr=params.getUid().split(":");//用户id
            String uid = uidStrArr[0];
            Integer userType = Integer.valueOf(uidStrArr[1]);
            Long credits=params.getCredits();
            String type=params.getType();//获取兑换类型
            String alipay=params.getAlipay();//获取支付宝账号
            String orderNum = params.getOrderNum();
            //其他参数参见 params的属性字段

            //TODO 开发者系统对uid用户扣除credits个积分
            DuibaOrders duibaOrders = new DuibaOrders();
            duibaOrders.setUserId(uid);
            duibaOrders.setCredits(credits);
            duibaOrders.setDuibaOrderNum(orderNum);
            duibaOrders.setOrderStatus(1);
            duibaOrders.setCreditsStatus(1);
            duibaOrders.setType(type);
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            duibaOrders.setGmtCreate(createTime);
//            duibaOrders.setGmtModified(createTime);

            SqlSession sqlSession = mybatisHelper.getSqlSession();
            try {
                DuibaOrdersMapper mapper = sqlSession.getMapper(DuibaOrdersMapper.class);
                mapper.insert(duibaOrders);
            } finally {
                sqlSession.close();
            }

            result.setBizId(orderNum);

            //调用net扣除积分
            Long totalcredits = netService.getAixinbi(uid, userType);
            if(totalcredits < credits) {
                result.setSuccess(false);
                result.setErrorMessage("您的余额不足，不能兑换.");
                return result;
            }
            netService.reducePoints(uid, userType, credits, "4003", orderNum);

            Long remainCredits = totalcredits - credits;
            result.setCredits(remainCredits);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public void duibaCallBack(HttpServletRequest request) {
        /*
        *  兑换订单的结果通知请求的解析方法
        *  当兑换订单成功时，兑吧会发送请求通知开发者，兑换订单的结果为成功或者失败，如果为失败，开发者需要将积分返还给用户
        */
        CreditTool tool=new CreditTool(duibaInfo.getAppKey(), duibaInfo.getAppSecret());
        try {
            CreditNotifyParams params= tool.parseCreditNotify(request);//利用tool来解析这个请求
            log.info("duibaCallBack=====>请求实体为:{}", JSONUtil.toCamelCaseJSONString(params));
            String orderNum=params.getOrderNum();
            String[] uidStrArr=params.getUid().split(":");//用户id
            String uid = uidStrArr[0];
            Integer userType = Integer.valueOf(uidStrArr[1]);

            //兑换成功
            SqlSession sqlSession = mybatisHelper.getSqlSession();
            DuibaOrders duibaOrders = new DuibaOrders();
            DuibaOrdersMapper mapper = sqlSession.getMapper(DuibaOrdersMapper.class);
            Example example = new Example(DuibaOrders.class);
            example.createCriteria().andEqualTo("duibaOrderNum", orderNum).andEqualTo("userId", uid);

            if(params.isSuccess()){
                duibaOrders.setOrderStatus(2);
                duibaOrders.setCreditsStatus(2);
            }else{
                //兑换失败，根据orderNum，对用户的金币进行返还，回滚操作
                DuibaOrders callBackDuibaOrder = mapper.selectByExample(example).get(0);
                netService.addPoints(callBackDuibaOrder.getUserId(), userType, callBackDuibaOrder.getCredits(), "4003", orderNum);

                duibaOrders.setOrderStatus(3);
                duibaOrders.setCreditsStatus(3);
            }
            try {
                mapper.updateByExampleSelective(duibaOrders, example);
            } finally {
                sqlSession.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
