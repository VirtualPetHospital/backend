package cn.vph.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 15:56
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionData implements Serializable {
    private Integer userId;
    private String type;
    private Integer level;
}
