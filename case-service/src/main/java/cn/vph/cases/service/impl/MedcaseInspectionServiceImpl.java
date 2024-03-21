package cn.vph.cases.service.impl;

import cn.vph.cases.entity.MedcaseInspection;
import cn.vph.cases.mapper.MedcaseInspectionMapper;
import cn.vph.cases.service.MedcaseInspectionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Caroline
 * @description
 * @create 2024/3/20 23:30
 */
@Service
public class MedcaseInspectionServiceImpl extends ServiceImpl<MedcaseInspectionMapper, MedcaseInspection> implements MedcaseInspectionService {

    @Autowired
    private MedcaseInspectionMapper medcaseInspectionMapper;

    @Override
    @Transactional
    public MedcaseInspection add(MedcaseInspection medcaseInspection) {
        if(exists(medcaseInspection.getMedcaseId(), medcaseInspection.getInspectionId())){
            medcaseInspectionMapper.updateById(medcaseInspection);
        }
        else{
            medcaseInspectionMapper.insert(medcaseInspection);
        }
        return medcaseInspection;
    }

    @Override
    @Transactional
    public MedcaseInspection update(MedcaseInspection medcaseInspection) {
        medcaseInspectionMapper.updateById(medcaseInspection);
        return medcaseInspection;
    }

    @Override
    @Transactional
    public void deleteByMedcaseId(Integer medcaseId) {
        medcaseInspectionMapper.delete(
                new LambdaQueryWrapper<MedcaseInspection>()
                        .eq(MedcaseInspection::getMedcaseId, medcaseId)
        );
    }

    @Override
    public boolean exists(Integer medcaseId, Integer inspectionId) {

        return medcaseInspectionMapper.exists(
                new LambdaQueryWrapper<MedcaseInspection>()
                        .eq(MedcaseInspection::getMedcaseId, medcaseId)
                        .eq(MedcaseInspection::getInspectionId, inspectionId)
        );
    }
}
