package top.wuhao.api.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.wuhao.api.entity.vo.AIModelVO;
import top.wuhao.api.service.IAIModelService;

import java.util.ArrayList;
import java.util.List;

/**
 * AI模型管理服务实现类
 *
 * @author wang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AIModelServiceImpl implements IAIModelService {
    
    private final DashScopeChatOptions dashScopeChatOptions;
    
    // 预定义的可用模型列表
    private static final List<ModelInfo> AVAILABLE_MODELS = List.of(
        new ModelInfo("qwen-turbo", "通义千问-Turbo", "适合快速响应的通用对话场景"),
        new ModelInfo("qwen-plus", "通义千问-Plus", "效果和速度均衡的通用对话模型"),
        new ModelInfo("qwen-max", "通义千问-Max", "效果最好、推理能力最强的通用对话模型"),
        new ModelInfo("qwen-max-latest", "通义千问-Max-Latest", "最新版本的最强通用对话模型"),
        new ModelInfo("qwen-vl-plus", "通义千问-VL-Plus", "支持图像理解的多模态模型"),
        new ModelInfo("qwen-vl-max", "通义千问-VL-Max", "效果最强的多模态模型，支持图像理解")
    );
    
    @Override
    public List<AIModelVO> getModelList() {
        List<AIModelVO> modelList = new ArrayList<>();
        String currentModel = dashScopeChatOptions.getModel();
        
        for (ModelInfo modelInfo : AVAILABLE_MODELS) {
            AIModelVO modelVO = new AIModelVO();
            modelVO.setName(modelInfo.getName());
            modelVO.setDisplayName(modelInfo.getDisplayName());
            modelVO.setDescription(modelInfo.getDescription());
            modelVO.setIsCurrent(modelInfo.getName().equals(currentModel));
            modelList.add(modelVO);
        }
        
        return modelList;
    }
    
    @Override
    public AIModelVO getCurrentModel() {
        String currentModelName = dashScopeChatOptions.getModel();
        
        for (ModelInfo modelInfo : AVAILABLE_MODELS) {
            if (modelInfo.getName().equals(currentModelName)) {
                AIModelVO modelVO = new AIModelVO();
                modelVO.setName(modelInfo.getName());
                modelVO.setDisplayName(modelInfo.getDisplayName());
                modelVO.setDescription(modelInfo.getDescription());
                modelVO.setIsCurrent(true);
                return modelVO;
            }
        }
        
        // 如果当前模型不在预定义列表中，返回基本信息
        AIModelVO modelVO = new AIModelVO();
        modelVO.setName(currentModelName);
        modelVO.setDisplayName(currentModelName);
        modelVO.setDescription("当前使用模型");
        modelVO.setIsCurrent(true);
        return modelVO;
    }
    
    @Override
    public AIModelVO switchModel(String modelName) {
        // 检查模型是否在可用列表中
        boolean isValidModel = AVAILABLE_MODELS.stream()
                .anyMatch(model -> model.getName().equals(modelName));
        
        if (!isValidModel) {
            throw new IllegalArgumentException("不支持的模型: " + modelName);
        }
        
        // 切换模型
        dashScopeChatOptions.setModel(modelName);
        log.info("已切换模型为: {}", modelName);
        
        // 返回切换后的当前模型信息
        return getCurrentModel();
    }
    
    /**
     * 模型信息内部类
     */
    private static class ModelInfo {
        private final String name;
        private final String displayName;
        private final String description;
        
        public ModelInfo(String name, String displayName, String description) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
}