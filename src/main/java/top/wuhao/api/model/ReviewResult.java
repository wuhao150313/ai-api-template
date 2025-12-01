package top.wuhao.api.model;

import lombok.Data;

/**
 * 作业点评结果
 *
 * @author mqxu
 */
@Data
public class ReviewResult {
    private Integer score;
    private String summary;
    private String strengths;
    private String weaknesses;
    private String suggestions;
}
