package cn.vph.cases.service;

import cn.vph.cases.entity.Disease;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-19 22:42
 **/
public interface DiseaseService {
    Object add(Disease disease);

    Object delete(Integer diseaseId);

    Object update(Disease disease);

    Object listByCategory(Integer categoryId);

    Object get(Integer diseaseId);
}
