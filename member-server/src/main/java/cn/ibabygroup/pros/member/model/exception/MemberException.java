package cn.ibabygroup.pros.member.model.exception;

import cn.ibabygroup.commons.exception.ProsScopeException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by tianmaogen on 2016/9/2.
 */
@Slf4j
public class MemberException extends ProsScopeException {
    private static final int DEFAULT_ERROR_CODE = 404000;

    public MemberException(String message) {
        this( message, null);
    }

    public MemberException(String message, Object data) {
        super(DEFAULT_ERROR_CODE, message, data, null);
        if(data != null)
            log.error(message+"data===={}", data.toString());
        else
            log.error(message);
    }
}
