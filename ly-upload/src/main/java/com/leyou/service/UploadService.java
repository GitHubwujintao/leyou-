package com.leyou.service;


import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.exception.LyException;
import com.leyou.web.UploadController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final List<String> suffixes = Arrays.asList("image/png","image/jpeg");

    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadImage(MultipartFile file) throws IOException {
        String type = file.getContentType();
        if (!suffixes.contains(type)){
            logger.info("上传失败，文件类型不匹配：{}", type);
            return null;
        }
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null){
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }
        } catch (IOException e) {
            throw  new LyException();
        }
        String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
        // 2、保存图片
//        // 2.1、生成保存目录
//        File dir = new File("C:\\image\\upload");
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        file.transferTo(new File(dir,file.getOriginalFilename()));
        System.out.println(file.getOriginalFilename());

        // 2.3、拼接图片地址
        String url = "http://image.leyou.com/" + storePath.getFullPath();
        return url;
    }
}
