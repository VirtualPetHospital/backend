package cn.vph.cases.service.impl;

import cn.vph.cases.entity.MedcaseMedicine;
import cn.vph.cases.entity.Medicine;
import cn.vph.cases.mapper.MedcaseMedicineMapper;
import cn.vph.cases.mapper.MedicineMapper;
import cn.vph.cases.service.MedicineService;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-19 22:56
 **/
@Service
public class MedicineServiceImpl extends ServiceImpl<MedicineMapper, Medicine> implements MedicineService {
    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private MedcaseMedicineMapper medcaseMedicineMapper;

    public Medicine selectByName(String name) {
        MPJLambdaWrapper<Medicine> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.eq(Medicine::getName, name);
        return medicineMapper.selectOne(queryWrapper);
    }

    @Override
    public Object add(Medicine medicine) {
        // 已存在则抛出异常
        Medicine med = selectByName(medicine.getName());
        AssertUtil.isNull(med, CommonErrorCode.MEDICINE_ALREADY_EXIST);
        medicineMapper.insert(medicine);
        return medicine;
    }

    @Override
    public Object update(Medicine medicine) {
        // 查询id是否存在
        AssertUtil.isNotNull(medicineMapper.selectById(medicine.getMedicineId()), CommonErrorCode.MEDICINE_NOT_EXIST);
        // 是否存在相同名字的药品
        Medicine med = selectByName(medicine.getName());
        // 是否是当前药品
        AssertUtil.isTrue(med == null || med.getMedicineId().equals(medicine.getMedicineId()), CommonErrorCode.MEDICINE_ALREADY_EXIST);
        medicineMapper.updateById(medicine);
        return medicine;
    }

    @Override
    public IPage<?> list(Integer pageNum, Integer pageSize, String nameKeyword) {
        MPJLambdaWrapper<Medicine> queryWrapper = new MPJLambdaWrapper<>();
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            queryWrapper.like(Medicine::getName, nameKeyword);
        }
        return medicineMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public Object delete(Integer medicineId) {
        // 是否存在
        AssertUtil.isNotNull(medicineMapper.selectById(medicineId), CommonErrorCode.MEDICINE_NOT_EXIST);
        LambdaQueryWrapper<MedcaseMedicine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MedcaseMedicine::getMedicineId, medicineId);
        AssertUtil.isTrue(medcaseMedicineMapper.selectCount(queryWrapper) == 0, CommonErrorCode.MEDICINE_USED_BY_MEDCASE);

        medicineMapper.deleteById(medicineId);
        return null;
    }

    @Override
    public Object get(Integer medicineId) {
        // 是否存在
        Medicine medicine = medicineMapper.selectById(medicineId);
        AssertUtil.isNotNull(medicine, CommonErrorCode.MEDICINE_NOT_EXIST);
        return medicine;
    }

}
