package cn.vph.cases.service;

import cn.vph.cases.entity.Operation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 22:35
 **/
public interface OperationService extends IService<Operation> {
    Operation add(Operation operation);

    Object delete(Integer operationId);

    Operation get(Integer operationId);

    Operation update(Operation operation);

    IPage<?> list(Integer pageNum, Integer pageSize, String nameKeyword);
}
