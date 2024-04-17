package cn.vph.files.service.impl;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import cn.vph.common.util.AssertUtil;
import cn.vph.files.common.FileConstants;
import cn.vph.files.pojo.VphFile;
import cn.vph.files.service.FileService;
import cn.vph.files.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    @Autowired
    FileUtil fileUtil;

    @Override
    public Object upload(MultipartFile file, String location) throws IOException {
        // 检查是否合法位置
        AssertUtil.in(location, fileConstants.LOCATIONS, CommonErrorCode.FILE_LOCATION_ERROR);

        // 检查文件后缀
        String fileName = file.getOriginalFilename();
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        // type = photo or video
        String type;
        if ("user-avatar".equals(location) || "room".equals(location)) {
            // 只能存储图片的类型
            AssertUtil.in(fileSuffix, fileConstants.PHOTO_TYPES, CommonErrorCode.FILE_TYPE_ERROR);
            type = "photo";
        } else {
            // 图片/类型
            if (containsString(fileConstants.PHOTO_TYPES, fileSuffix)) {
                type = "photo";
            }
            // 视频类型
            else if (containsString(fileConstants.VIDEO_TYPES, fileSuffix)) {
                type = "video";
            } else {
                throw new CommonException(CommonErrorCode.FILE_TYPE_ERROR);
            }
        }

        // 存储位置
        String uploadPath = fileConstants.FILE_DIR;
        // 确认文件夹存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String newFileName = location + fileConstants.SEPARATOR + type + fileConstants.SEPARATOR + fileUtil.generateFileNameByTime();
        String filePath = uploadPath + File.separator + newFileName;

        // 先对文件进行存储
        String dest = filePath + "." + fileSuffix;
        File destFile = new File(dest);
        file.transferTo(destFile);
        // 再对存储的文件进行统一文件格式转化
        if ("photo".equals(type)) {
            // 转换图片格式
            fileUtil.convertPhotoToJpeg(dest);
            newFileName += ".jpeg";
        } else {
            // 转换视频格式
            fileUtil.convertVideoToMp4(dest);
            newFileName += ".mp4";
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

        String downloadPath = fileConstants.FILE_DIR;
        File file = new File(downloadPath + File.separator + fileName);

        AssertUtil.isTrue(file.exists(), CommonErrorCode.FILE_NOT_EXIST);
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
        return null;
    }

    @Override
    public void delete(String fileName) {
        String downloadPath = fileConstants.FILE_DIR;
        File file = new File(downloadPath + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean containsString(String[] array, String str) {
        for (String s : array) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }
}
