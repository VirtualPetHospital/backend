package cn.vph.files.service;

import cn.vph.files.pojo.ConvertRequest;
import cn.vph.files.pojo.FileChunkParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 14:31
 **/
public interface FileService {
    Object upload(MultipartFile file, String location) throws IOException;

    Object download(String fileName, HttpServletResponse response) throws UnsupportedEncodingException;

    void delete(String fileName);

    Object uploadByChunk(FileChunkParam param) throws IOException;

    Object checkUpload(String identifier);

    Object convert(ConvertRequest c) throws IOException;
}
