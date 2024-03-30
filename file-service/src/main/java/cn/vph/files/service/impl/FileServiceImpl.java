package cn.vph.files.service.impl;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import cn.vph.common.util.AssertUtil;
import cn.vph.files.common.FileConstants;
import cn.vph.files.pojo.VphFile;
import cn.vph.files.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 14:31
 **/
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileConstants fileConstants;

    @Override
    public Object upload(MultipartFile file, String location) throws IOException {
        // 检查是否合法位置
        AssertUtil.in(location, fileConstants.LOCATIONS, CommonErrorCode.FILE_LOCATION_ERROR);

        // 检查文件后缀
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        // type = photo or video
        String type;
        if ("user-avatar".equals(location) || "room".equals(location)) {
            // 只能存储图片的类型
            AssertUtil.in(fileType, fileConstants.PHOTO_TYPES, CommonErrorCode.FILE_TYPE_ERROR);
            type = "photo";
        } else {
            // 图片/类型
            if (containsString(fileConstants.PHOTO_TYPES, fileType)) {
                type = "photo";
            }
            // 视频类型
            else if (containsString(fileConstants.VIDEO_TYPES, fileType)) {
                type = "video";
            } else {
                throw new CommonException(CommonErrorCode.FILE_TYPE_ERROR);
            }
        }

        // 存储位置
        String uploadPath = fileConstants.FILE_DIR + File.separator + location + File.separator + type;
        // 确认文件夹存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String newFileName = location + fileConstants.SEPARATOR + type + fileConstants.SEPARATOR + generateFileNameByTime();
        String filePath = uploadPath + File.separator + newFileName;

        if ("photo".equals(type)) {
            newFileName += ".jpeg";
            filePath += ".jpeg";
            convertPhotoToJpeg(file, filePath, fileType);
        } else {
            // 视频
            newFileName += ".mp4";
            filePath += ".mp4";
            convertVideoToMp4(file, filePath, fileType);
        }
        return new VphFile(newFileName, location, type);
    }

    @Override
    public Object download(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        String[] fileNameArray = fileName.split(fileConstants.SEPARATOR);
        if (fileNameArray.length != 3) {
            throw new CommonException(CommonErrorCode.FILE_NOT_EXIST);
        }

        // 检查是否合法位置
        String location = fileNameArray[0];
        // 获取类型
        String type = fileNameArray[1];
        // 检查是否合法位置
        AssertUtil.in(location, fileConstants.LOCATIONS, CommonErrorCode.FILE_NOT_EXIST);
        // 检查类型
        if (!"photo".equals(type) && !"video".equals(type)) {
            throw new CommonException(CommonErrorCode.FILE_NOT_EXIST);
        }

        AssertUtil.in(location, fileConstants.LOCATIONS, CommonErrorCode.FILE_NOT_EXIST);

        String downloadPath = fileConstants.FILE_DIR + File.separator + location + File.separator + type;
        File file = new File(downloadPath + File.separator + fileName);

        if (file.exists()) {

            String contentType;
            if ("photo".equals(type)) {
                contentType = "image/jpeg";
            } else {
                contentType = "video/mp4";
            }
            response.setContentType(contentType);
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            response.setContentLength((int) file.length());
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(FileUtils.readFileToByteArray(file));
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                throw new CommonException(CommonErrorCode.FILE_DOWNLOAD_FAIL);
            }
        }
        throw new CommonException(CommonErrorCode.FILE_NOT_EXIST);
    }

    public boolean containsString(String[] array, String str) {
        for (String s : array) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public String generateFileNameByTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        // 获取当前时间
        Date currentDate = new Date();

        // 使用 SimpleDateFormat 对象格式化日期时间
        String formattedDateTime = sdf.format(currentDate);

        return formattedDateTime;
    }

    public void convertPhotoToJpeg(MultipartFile file, String dest, String fileType) throws IOException {
        if ("jpg".equals(fileType) || "jpeg".equals(fileType)) {
            byte[] bytes = file.getBytes();
            File destFile = new File(dest);
            FileUtils.writeByteArrayToFile(destFile, bytes);
        } else if ("png".equals(fileType)) {
            convertPngToJpeg(file, dest);
        } else if ("gif".equals(fileType)) {
            convertGifToJpeg(file, dest);
        }
    }

    public void convertVideoToMp4(MultipartFile file, String dest, String fileType) throws IOException {
        if("mp4".equals(fileType)){
            byte[] bytes = file.getBytes();
            File destFile = new File(dest);
            FileUtils.writeByteArrayToFile(destFile, bytes);
        }
    }

    public void convertPngToJpeg(MultipartFile file, String dest) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        BufferedImage newBufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        ImageIO.write(newBufferedImage, "jpeg", new File(dest));
    }

    public void convertGifToJpeg(MultipartFile file, String dest) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        BufferedImage newBufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        ImageIO.write(newBufferedImage, "jpeg", new File(dest));
    }


}
