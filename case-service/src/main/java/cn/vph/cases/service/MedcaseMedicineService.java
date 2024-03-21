package cn.vph.cases.service;

import cn.vph.cases.entity.MedcaseMedicine;
import com.baomidou.mybatisplus.extension.service.IService;

public interface MedcaseMedicineService extends IService<MedcaseMedicine> {

    MedcaseMedicine add(MedcaseMedicine medcaseMedicine);
    MedcaseMedicine update(MedcaseMedicine medcaseMedicine);
    void deleteByMedcaseId(Integer medcaseId);

    boolean exists(Integer medcaseId, Integer medicineId);
}
