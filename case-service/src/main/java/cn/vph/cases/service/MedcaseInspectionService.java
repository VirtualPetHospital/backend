package cn.vph.cases.service;

import cn.vph.cases.entity.MedcaseInspection;
import com.baomidou.mybatisplus.extension.service.IService;

public interface MedcaseInspectionService extends IService<MedcaseInspection> {
    MedcaseInspection add(MedcaseInspection medcaseInspection);
    MedcaseInspection update(MedcaseInspection medcaseInspection);
    void deleteByMedcaseId(Integer medcaseId);

    boolean exists(Integer medcaseId, Integer inspectionId);
}
