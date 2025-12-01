package top.wuhao.api.infrastructure.oss.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.wuhao.api.common.exception.ServerException;
import top.wuhao.api.infrastructure.oss.AliyunOssProperties;
import top.wuhao.api.infrastructure.oss.OssService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * 阿里云 OSS 服务实现
 *
 * @author mqxu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunOssServiceImpl implements OssService {
    private final AliyunOssProperties aliyunOssProperties;

    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};

    private OSS ossClient;

    @PostConstruct
    public void init() {
        ossClient = new OSSClientBuilder().build(
                aliyunOssProperties.getEndpoint(),
                aliyunOssProperties.getAccessKey(),
                aliyunOssProperties.getAccessSecret()
        );
    }

    @Override
    public String upload(MultipartFile file) {
        String returnImgUrl;
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StrUtil.endWith(file.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {
            // 如果图片格式不合法
            throw new ServerException("图片格式不正确");
        }
        // 获取文件原名称
        String originalFilename = file.getOriginalFilename();
        // 获取文件类型
        assert originalFilename != null;
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 新文件名称
        String newFileName = UUID.randomUUID() + fileType;
        // 构建日期路径, 例如：OSS目标文件夹/2024/04/31/文件名
        String filePath = DateUtil.format(new Date(), "yyyy/MM/dd");
        // 文件上传的路径地址
        String uploadUrl = filePath + "/" + newFileName;
        // 获取文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.error("获取文件输入流失败", e);
        }
        /*
         * 阿里云OSS获取图片链接后，图片是下载链接，而并非在线浏览链接，
         * 因此，这里在上传的时候要解决ContentType的问题，将其改为image/jpg
         */
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpg");
        //文件上传至阿里云OSS
        ossClient.putObject(aliyunOssProperties.getBucket(), uploadUrl, inputStream, meta);
        // 获取文件上传后的图片返回地址
        returnImgUrl = "https://" + aliyunOssProperties.getBucket() + "." + aliyunOssProperties.getEndpoint() + "/" + uploadUrl;
        return returnImgUrl;
    }

    @Override
    public void delete(String url) {
        try {
            // 从 URL 中提取文件名
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            ossClient.deleteObject(aliyunOssProperties.getBucket(), fileName);
            log.info("文件删除成功，URL: {}", url);
        } catch (Exception e) {
            log.error("文件删除失败，URL: {}", url, e);
            throw new ServerException("文件删除失败");
        }
    }
}
