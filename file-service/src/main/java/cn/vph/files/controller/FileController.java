package cn.vph.files.controller;

import cn.vph.common.Result;
import cn.vph.common.annotation.Student;
import cn.vph.files.pojo.ConvertRequest;
import cn.vph.files.pojo.FileChunkParam;
import cn.vph.files.service.FileService;
import cn.vph.files.util.TimeUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("files")
@ApiOperation(value = "文件管理", tags = {"文件管理"})
public class FileController {
    @Autowired
    private FileService fileService;

    @Student
    @PostMapping("upload")
    @ApiOperation(value = "上传文件")
    public Result<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("location") String location
    ) throws IOException {
        return Result.success(fileService.upload(file, location));
    }

    @Student
    @PostMapping("uploadChunk")
    @ApiOperation(value = "分片上传文件")
    public Result<?> uploadChunk(FileChunkParam param) throws IOException{
        log.error("开始上传时间：" + TimeUtil.currentTime());
        log.info("上传文件：{}",param);
        return Result.success(fileService.uploadByChunk(param));
    }

    @Student
    @GetMapping("checkUpload")
    @ApiOperation(value = "检查文件上传")
    public Result<?> checkUpload(@PathVariable(value = "identifier") String identifier){
        log.info("文件MD5:{}",identifier);
        return Result.success(fileService.checkUpload(identifier));
    }

    @Student
    @PostMapping("convert")
    @ApiOperation(value = "转换文件")
    public Result<?> convert(@RequestBody ConvertRequest convertRequest) throws IOException {
        return Result.success(fileService.convert(convertRequest));
    }

    @Student
    @GetMapping("download")
    @ApiOperation(value = "下载文件")
    public Result<?> download(
            @RequestParam("file_name") String fileName,
            HttpServletResponse response
    ) throws UnsupportedEncodingException {
        return Result.success(fileService.download(fileName, response));
    }

    @DeleteMapping
    @ApiOperation(value = "删除文件")
    public void delete(@RequestParam("file_name") String fileName){
        return;
//        if (fileName == null || fileName.isEmpty()) {
//            return;
//        }
//        fileService.delete(fileName);
    }
}
