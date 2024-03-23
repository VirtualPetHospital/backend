package cn.vph.cases.service;

import cn.vph.cases.entity.Disease;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-19 22:42
 **/
public interface DiseaseService extends IService<Disease> {
    Object add(Disease disease);

    Object delete(Integer diseaseId);

    Object update(Disease disease);

    Object listByCategory(Integer categoryId);

    Object getDiseaseById(Integer diseaseId);
}
