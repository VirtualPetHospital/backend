package cn.vph.cases.service.impl;

import cn.vph.cases.entity.Operation;
import cn.vph.cases.mapper.OperationMapper;
import cn.vph.cases.service.OperationService;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 22:36
 **/

@Service
public class OperationServiceImpl implements OperationService {
    @Autowired
    private OperationMapper operationMapper;
    public Operation selectByName(String name) {
        MPJLambdaQueryWrapper<Operation> queryWrapper = new MPJLambdaQueryWrapper<>();
        queryWrapper.eq(Operation::getName, name);
        return operationMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional
    public Operation add(Operation operation) {
        // 已存在则抛出异常
        Operation ope = selectByName(operation.getName());
        AssertUtil.isNull(ope, CommonErrorCode.OPERATION_ALREADY_EXIST);
        operationMapper.insert(operation);
        return operation;
    }

    @Override
    @Transactional
    public Object delete(Integer operationId) {
        // 不存在则抛出异常
        AssertUtil.isNotNull(operationMapper.selectById(operationId), CommonErrorCode.OPERATION_NOT_EXIST);
        operationMapper.deleteById(operationId);
        return null;
    }

    @Override
    public Operation get(Integer operationId) {
        // 不存在则抛出异常
        Operation operation = operationMapper.selectById(operationId);
        AssertUtil.isNotNull(operation, CommonErrorCode.OPERATION_NOT_EXIST);
        return operation;
    }

    @Override
    @Transactional
    public Operation update(Operation operation) {
        // 是否存在相同名字的operation
        Operation ope = selectByName(operation.getName());
        // 是否是当前operation
        AssertUtil.isTrue(ope == null || ope.getOperationId().equals(operation.getOperationId()), CommonErrorCode.OPERATION_ALREADY_EXIST);
        operationMapper.updateById(operation);
        return operation;
    }

    @Override
    public IPage<?> list(Integer pageNum, Integer pageSize, String nameKeyword) {
        MPJLambdaWrapper<Operation> queryWrapper = new MPJLambdaWrapper<>();
        // 名字模糊查询
        if (nameKeyword != null) {
            queryWrapper.like(Operation::getName, nameKeyword);
        }
        return operationMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }
}
