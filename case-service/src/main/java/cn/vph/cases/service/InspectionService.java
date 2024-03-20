package cn.vph.cases.service;

import cn.vph.cases.entity.Inspection;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 08:50
 **/
public interface InspectionService {
    Object add(Inspection inspection);

    Object update(Inspection inspection);

    Object list(Integer pageNum, Integer pageSize, String nameKeyword);

    Object get(Integer inspectionId);

    Object delete(Integer inspectionId);
}
