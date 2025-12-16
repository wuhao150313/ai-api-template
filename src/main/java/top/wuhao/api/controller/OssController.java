package top.wuhao.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.wuhao.api.common.result.Result;
import top.wuhao.api.infrastructure.oss.OssService;

import java.util.Set;

/**
 * OSS 文件上传接口
 *
 * @author wang
 */
@RestController
@RequestMapping("/api/oss")
@RequiredArgsConstructor
@Tag(name = "OSS文件上传接口")
@CrossOrigin(origins = "*")
public class OssController {
    private final OssService ossService;
    private static final Set<String> ALLOWED_TYPES = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            "image/jpg",
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            "image/bmp"
    );
    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024;

    /**
     * 上传文件到OSS
     *
     * @param file 文件
     * @return OSS文件URL
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件到OSS", description = "上传文件到OSS，返回文件访问URL")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return Result.fail("文件不能为空");
            }
            if (file.getSize() > MAX_SIZE_BYTES) {
                return Result.fail("文件过大，最大支持 5MB");
            }
            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
                return Result.fail("仅支持图片类型：jpg/png/gif/bmp");
            }
            String url = ossService.upload(file);
            return Result.ok(url);
        } catch (Exception e) {
            return Result.fail(500, "文件上传失败: " + e.getMessage());
        }
    }
}

