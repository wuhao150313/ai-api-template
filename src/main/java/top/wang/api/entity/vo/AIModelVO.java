package top.wang.api.entity.vo;

import lombok.Data;

/**
 * AI模型信息VO
 *
 * @author wang
 */
@Data
public class AIModelVO {
    
    /**
     * 模型名称
     */
    private String name;
    
    /**
     * 模型显示名称
     */
    private String displayName;
    
    /**
     * 模型描述
     */
    private String description;
    
    /**
     * 是否为当前模型
     */
    private Boolean isCurrent;
}