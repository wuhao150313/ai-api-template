package top.wuhao.api.service;

import top.wuhao.api.entity.vo.AIModelVO;
import java.util.List;

/**
 * AI模型管理服务接口
 *
 * @author wang
 */
public interface IAIModelService {
    
    /**
     * 获取所有可用模型列表
     *
     * @return 模型列表
     */
    List<AIModelVO> getModelList();
    
    /**
     * 获取当前使用的模型
     *
     * @return 当前模型信息
     */
    AIModelVO getCurrentModel();
    
    /**
     * 切换模型
     *
     * @param modelName 模型名称
     * @return 切换后的当前模型信息
     */
    AIModelVO switchModel(String modelName);
}