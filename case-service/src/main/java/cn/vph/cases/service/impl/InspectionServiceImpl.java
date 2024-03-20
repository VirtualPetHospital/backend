package cn.vph.cases.service.impl;

import cn.vph.cases.entity.Inspection;
import cn.vph.cases.mapper.InspectionMapper;
import cn.vph.cases.service.InspectionService;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 08:50
 **/
@Service
public class InspectionServiceImpl implements InspectionService {
    @Autowired
    private InspectionMapper inspectionMapper;

    public Inspection selectByName(String name) {
        MPJLambdaQueryWrapper<Inspection> queryWrapper = new MPJLambdaQueryWrapper<>();
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
        MPJLambdaQueryWrapper<Inspection> queryWrapper = new MPJLambdaQueryWrapper<>();
        if (nameKeyword != null) {
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
        // 不存在则抛出异常
        AssertUtil.isNotNull(inspectionMapper.selectById(inspectionId), CommonErrorCode.INSPECTION_NOT_EXIST);
        inspectionMapper.deleteById(inspectionId);
        return null;
    }
}
