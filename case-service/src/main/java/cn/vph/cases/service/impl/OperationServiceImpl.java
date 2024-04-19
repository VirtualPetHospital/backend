package cn.vph.cases.service.impl;

import cn.vph.cases.clients.FileFeignClient;
import cn.vph.cases.entity.Operation;
import cn.vph.cases.mapper.OperationMapper;
import cn.vph.cases.service.OperationService;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements OperationService {

    @Autowired
    private OperationMapper operationMapper;
    @Autowired
    private FileFeignClient fileFeignClient;
    
    public Operation selectByName(String name) {
        MPJLambdaWrapper<Operation> queryWrapper = new MPJLambdaWrapper<>();
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
        Operation operation = operationMapper.selectById(operationId);
        // 不存在则抛出异常
        AssertUtil.isNotNull(operation, CommonErrorCode.OPERATION_NOT_EXIST);
        // 有外键依赖，如果不能删会直接报错
        operationMapper.deleteById(operationId);
        fileFeignClient.delete(operation.getPhoto());
        fileFeignClient.delete(operation.getVideo());
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
        Operation oldOperation = operationMapper.selectById(operation.getOperationId());
        // 根据id查询是否存在
        AssertUtil.isNotNull(oldOperation, CommonErrorCode.OPERATION_NOT_EXIST);
        // 是否存在相同名字的operation
        Operation ope = selectByName(operation.getName());
        // 是否是当前operation
        AssertUtil.isTrue(ope == null || ope.getOperationId().equals(operation.getOperationId()), CommonErrorCode.OPERATION_ALREADY_EXIST);
        operationMapper.updateById(operation);
        // 如果photo 或 video 有变化，则删除原来的文件
        if (oldOperation.getPhoto() != null && !oldOperation.getPhoto().equals(operation.getPhoto())) {
            fileFeignClient.delete(oldOperation.getPhoto());
        }
        if (oldOperation.getVideo() != null && !oldOperation.getVideo().equals(operation.getVideo())) {
            fileFeignClient.delete(oldOperation.getVideo());
        }
        return operation;
    }

    @Override
    public IPage<?> list(Integer pageNum, Integer pageSize, String nameKeyword) {
        MPJLambdaWrapper<Operation> queryWrapper = new MPJLambdaWrapper<>();
        // 名字模糊查询
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            queryWrapper.like(Operation::getName, nameKeyword);
        }
        return operationMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }
}
