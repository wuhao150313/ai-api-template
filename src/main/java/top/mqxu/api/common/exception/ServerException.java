package top.mqxu.api.common.exception;

import lombok.Getter;
import top.mqxu.api.common.result.ResultCode;

/**
 * 自定义业务异常
 *
 * @author mqxu
 */
@Getter
public class ServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;

    public ServerException(String message) {
        super(message);
        this.code = ResultCode.FAIL.getCode();
    }

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServerException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
}
