package cn.vph.files.controller;

import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import cn.vph.common.annotation.Student;
import cn.vph.files.service.FileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 14:12
 **/
@RestController
@RequestMapping("files")
@ApiOperation(value = "文件管理", tags = {"文件管理"})
public class FileController {
    @Autowired
    private FileService fileService;

    @Administrator
    @PostMapping("upload")
    @ApiOperation(value = "上传文件")
    public Result<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("location") String location
    ) throws IOException {
        return Result.success(fileService.upload(file, location));
    }

    @Student
    @GetMapping("download")
    @ApiOperation(value = "下载文件")
    public Result<?> download(
            @RequestParam("file_name") String fileName,
            @RequestParam("location") String location,
            HttpServletResponse response
    ) throws UnsupportedEncodingException {
        return Result.success(fileService.download(fileName, location, response));
    }
}
