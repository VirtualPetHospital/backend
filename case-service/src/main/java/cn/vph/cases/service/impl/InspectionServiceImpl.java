package cn.vph.cases.service.impl;

import cn.vph.cases.entity.Inspection;
import cn.vph.cases.entity.MedcaseInspection;
import cn.vph.cases.mapper.InspectionMapper;
import cn.vph.cases.mapper.MedcaseInspectionMapper;
import cn.vph.cases.service.InspectionService;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 08:50
 **/
@Service
public class InspectionServiceImpl extends ServiceImpl<InspectionMapper, Inspection> implements InspectionService {

    @Autowired
    private InspectionMapper inspectionMapper;

    @Autowired
    private MedcaseInspectionMapper medcaseInspectionMapper;

    public Inspection selectByName(String name) {
        MPJLambdaWrapper<Inspection> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.eq(Inspection::getName, name);
        return inspectionMapper.selectOne(queryWrapper);
    }
    @Override
    public Object add(Inspection inspection) {
        // 是否已经存在同名
        Inspection ins = selectByName(inspection.getName());
        AssertUtil.isNull(ins, CommonErrorCode.INSPECTION_ALREADY_EXIST);
        inspectionMapper.insert(inspection);
        return inspection;
    }

    @Override
    public Object update(Inspection inspection) {
        // 是否存在相同名字的检查项目
        Inspection ins = selectByName(inspection.getName());
        // 是否是当前检查项目
        AssertUtil.isTrue(ins == null || ins.getInspectionId().equals(inspection.getInspectionId()), CommonErrorCode.INSPECTION_ALREADY_EXIST);
        inspectionMapper.updateById(inspection);
        return inspection;
    }

    @Override
    public IPage<?> list(Integer pageNum, Integer pageSize, String nameKeyword) {
        MPJLambdaWrapper<Inspection> queryWrapper = new MPJLambdaWrapper<>();
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            queryWrapper.like(Inspection::getName, nameKeyword);
        }
        return inspectionMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public Object get(Integer inspectionId) {
        // 不存在则抛出异常
        Inspection inspection = inspectionMapper.selectById(inspectionId);
        AssertUtil.isNotNull(inspection, CommonErrorCode.INSPECTION_NOT_EXIST);
        return inspection;
    }

    @Override
    public Object delete(Integer inspectionId) {
        LambdaQueryWrapper<MedcaseInspection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MedcaseInspection::getInspectionId, inspectionId);
        AssertUtil.isTrue(medcaseInspectionMapper.selectCount(queryWrapper) == 0, CommonErrorCode.INSPECTION_HAS_MEDCASE);
        // 不存在则抛出异常
        AssertUtil.isNotNull(inspectionMapper.selectById(inspectionId), CommonErrorCode.INSPECTION_NOT_EXIST);
        inspectionMapper.deleteById(inspectionId);
        return null;
    }
}
