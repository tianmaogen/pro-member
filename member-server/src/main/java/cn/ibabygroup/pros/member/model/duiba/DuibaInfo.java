package cn.ibabygroup.pros.member.model.duiba;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by tianmaogen on 2016/10/14.
 */
@Component
@Data
public class DuibaInfo {
    @Value("${ibabygroup.duiba.appKey}")
    private String appKey;

    @Value("${ibabygroup.duiba.appSecret}")
    private String appSecret;
}
