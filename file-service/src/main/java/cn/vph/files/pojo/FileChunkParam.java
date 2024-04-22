package cn.vph.files.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-21 18:49
 **/
@Data
public class FileChunkParam implements Serializable {
    /**
     * 当前分片的编号，从1开始
     */
    @JsonProperty("chunk_number")
    private Integer chunkNumber;

    /**
     * 预设的每个分片的大小
     */
    @JsonProperty("chunk_size")
    private Float chunkSize;

    /**
     * 当前分片的大小
     */
    @JsonProperty("current_chunk_size")
    private Float currentChunkSize;


    /**
     * 一共多少个分片
     */
    @JsonProperty("total_chunk")
    private Integer totalChunk;

    /**
     * 文件总大小
     */
    @JsonProperty("total_size")
    private Double totalSize;

    /**
     * 文件的MD5值
     */
    private String identifier;

    /**
     * 文件的一片
     */
    private MultipartFile file;

    /**
     * 文件名
     */
    @JsonProperty("file_name")
    private String fileName;

    /**
     * 文件类型
     */
    private String type;
}
