package top.wuhao.api.infrastructure.oss;

import org.springframework.web.multipart.MultipartFile;

/**
 * OSS 服务接口
 *
 * @author mqxu
 */
public interface OssService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件访问 URL
     */
    String upload(MultipartFile file);

    /**
     * 删除文件
     *
     * @param url 文件 URL
     */
    void delete(String url);
}
