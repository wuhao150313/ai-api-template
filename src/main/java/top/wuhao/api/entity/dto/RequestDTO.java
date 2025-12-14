package top.wuhao.api.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @description 请求 AI Agent 的数据传输对象
 */
@Data
public class RequestDTO implements Serializable {

    /**
     * 用户唯一标识（用于生成 threadId）
     */
    private String userId;

    /**
     * 用户发送的消息内容
     */
    private String message;
}

