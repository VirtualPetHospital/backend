package cn.vph.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Caroline
 * @description mybatis-plus 分页配置
 * @create 2024/3/13 13:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryPage implements Serializable {

    /* page_num */
    private int page;

    /* page_size */
    private int limit;
}

