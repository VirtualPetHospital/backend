package cn.vph.cases.service.impl;

import cn.vph.cases.entity.MedcaseMedicine;
import cn.vph.cases.mapper.MedcaseMedicineMapper;
import cn.vph.cases.service.MedcaseMedicineService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Caroline
 * @description
 * @create 2024/3/20 23:39
 */
@Service
public class MedcaseMedicineServiceImpl extends ServiceImpl<MedcaseMedicineMapper, MedcaseMedicine> implements MedcaseMedicineService {

    @Autowired
    private MedcaseMedicineMapper medcaseMedicineMapper;

    @Override
    @Transactional
    public MedcaseMedicine add(MedcaseMedicine medcaseMedicine) {
        if(exists(medcaseMedicine.getMedcaseId(), medcaseMedicine.getMedicineId())){
            medcaseMedicineMapper.updateById(medcaseMedicine);
        }
        else{
            medcaseMedicineMapper.insert(medcaseMedicine);
        }
        return medcaseMedicine;
    }

    @Override
    @Transactional
    public MedcaseMedicine update(MedcaseMedicine medcaseMedicine) {
        medcaseMedicineMapper.updateById(medcaseMedicine);
        return medcaseMedicine;
    }

    @Override
    @Transactional
    public void deleteByMedcaseId(Integer medcaseId) {
        medcaseMedicineMapper.delete(
                new LambdaQueryWrapper<MedcaseMedicine>()
                .eq(MedcaseMedicine::getMedcaseId, medcaseId));

    }

    @Override
    public boolean exists(Integer medcaseId, Integer medicineId) {

        return medcaseMedicineMapper.exists(
                new LambdaQueryWrapper<MedcaseMedicine>()
                .eq(MedcaseMedicine::getMedcaseId, medcaseId)
                .eq(MedcaseMedicine::getMedicineId, medicineId)
        );
    }
}
