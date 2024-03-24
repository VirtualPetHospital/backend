package cn.vph.files.service.impl;

import cn.vph.files.common.FileConstants;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import cn.vph.files.pojo.VphFile;
import cn.vph.files.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    //TODO 文件名自动生成
    @Override
    public Object upload(MultipartFile file, String location) throws IOException {
        String uploadPath = fileConstants.getFileDir() + File.separator + location;
        // 确认文件夹存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        byte[] bytes = file.getBytes();
        File dest = new File(uploadPath + File.separator + file.getOriginalFilename());
        FileUtils.writeByteArrayToFile(dest, bytes);

        return new VphFile(file.getOriginalFilename(), location);
    }

    @Override
    public Object download(String fileName, String location, HttpServletResponse response) throws UnsupportedEncodingException {
        String downloadPath = fileConstants.getFileDir() + File.separator + location;
        File file = new File(downloadPath + File.separator + fileName);
        if (file.exists()) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.addHeader("Content-Disposition", "attachment;fileName=" +  new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(FileUtils.readFileToByteArray(file));
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                throw new CommonException(CommonErrorCode.FILE_DOWNLOAD_FAIL);
            }
        }
        return null;
    }


}
