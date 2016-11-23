package cn.ibabygroup.pros.member.model.mysql;

import cn.ibabygroup.commons.data.model.CommonsMysqlModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by tianmaogen on 2016/10/14.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name="duiba_orders")
public class DuibaOrders implements Serializable {
    @Id
    protected Long id;
    @Column(name="user_id")
    private String userId; //用户Id
    private Long credits; //本次兑换扣除的积分
    @Column(name="actual_price")
    private int actualPrice; //实际扣除价格
    @Column(name="duiba_order_num")
    private String duibaOrderNum; //兑吧订单号
    @Column(name="order_status")
    private int orderStatus; //订单状态（1创建、2成功、3失败）。
    @Column(name="credits_status")
    private int creditsStatus; //积分状态（1预扣、2成功、3返还）。
    private String type; //支付类型 --- 支付宝等
    private String description;
    @Column(name="gmt_create")
    private Timestamp gmtCreate; //订单创建时间
    @Column(name="gmt_modified")
    private Timestamp gmtModified; //订单修改时间
}
