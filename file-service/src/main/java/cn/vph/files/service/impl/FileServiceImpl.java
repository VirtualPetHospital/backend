package cn.vph.files.service.impl;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import cn.vph.common.util.AssertUtil;
import cn.vph.common.util.TimeUtil;
import cn.vph.files.common.FileConstants;
import cn.vph.files.entity.FileDTO;
import cn.vph.files.mapper.FileDTOMapper;
import cn.vph.files.pojo.ConvertRequest;
import cn.vph.files.pojo.FileChunkParam;
import cn.vph.files.pojo.VphFile;
import cn.vph.files.service.FileService;
import cn.vph.files.util.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
    private FileDTOMapper fileDTOMapper;
    @Autowired
    private FileUtil fileUtil;

    @Override
    public Object upload(MultipartFile file, String location) throws IOException {
        // 检查是否合法位置
        AssertUtil.in(location, fileConstants.LOCATIONS, CommonErrorCode.FILE_LOCATION_ERROR);

        // 检查文件后缀
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 文件是图片还是视频
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

        String newFileName = location + fileConstants.SEPARATOR + type + fileConstants.SEPARATOR + fileUtil.generateFileNameByTime() + "." + fileSuffix;
        String filePath = uploadPath + File.separator + newFileName;

        // 先对文件进行存储
        File destFile = new File(filePath);
        file.transferTo(destFile);
        // 再对存储的文件进行统一文件格式转化
        if ("photo".equals(type)) {
            // 转换图片格式
            fileUtil.convertPhotoToJpeg(uploadPath, newFileName);
        } else {
            // 转换视频格式
            fileUtil.convertVideoToMp4(uploadPath, newFileName);
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

    @Override
    public Object uploadByChunk(FileChunkParam param) throws FileNotFoundException {
        if (null == param.getFile()) {
            throw new CommonException(CommonErrorCode.UPLOAD_FILE_NOT_NULL);
        }
        // 创建文件夹
        File fileTempDir = new File(fileConstants.FILE_DIR);
        if (!fileTempDir.exists()) {
            if (!fileTempDir.mkdirs()) {
                throw new CommonException(CommonErrorCode.FILE_UPLOAD_FAIL);
            }
        }
        // 获取原文件后缀
        String fileName = param.getFilename();
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = TimeUtil.getCurrentTime() + fileSuffix;
        String fullFileName = fileConstants.FILE_DIR + File.separator + fileName;

        saveFileChunk(fullFileName, param);
        // 返回文件名
        return fileName;
    }

    @Override
    public Object checkUpload(String identifier) {
        LambdaQueryWrapper<FileDTO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileDTO::getFileIdentifier, identifier);
        FileDTO fileDTO = fileDTOMapper.selectOne(queryWrapper);
        if (null == fileDTO) {
            return null;
        }
        return fileDTO.getFileName();
    }

    @Override
    public Object convert(ConvertRequest cov) throws IOException {
        String type = cov.getType(), fileName = cov.getFileName();
       if("photo".equals(type)){
           fileName = fileUtil.convertPhotoToJpeg(fileConstants.FILE_DIR, fileName);
         }else if("video".equals(type)) {
           fileName = fileUtil.convertVideoToMp4(fileConstants.FILE_DIR, fileName);
       }
       // 存储到数据库
        return fileName;
    }

    private void saveFileChunk(String fullFileName, FileChunkParam param) throws FileNotFoundException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fullFileName, "rw")) {
            long chunkSize = param.getChunkSize() == 0L ? fileConstants.DEFAULT_CHUNK_SIZE : param.getChunkSize().longValue();
            long offset = chunkSize * (param.getChunkNumber() - 1);
            randomAccessFile.seek(offset);
            randomAccessFile.write(param.getFile().getBytes());
        } catch (Exception e) {
            throw new CommonException(CommonErrorCode.FILE_UPLOAD_FAIL);
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
