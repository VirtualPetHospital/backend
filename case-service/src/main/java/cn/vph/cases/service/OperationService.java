package cn.vph.cases.service;

import cn.vph.cases.entity.Operation;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 22:35
 **/
public interface OperationService {
    Operation add(Operation operation);

    Object delete(Integer operationId);

    Operation get(Integer operationId);

    Operation update(Operation operation);

    Object list(Integer pageNum, Integer pageSize, String nameKeyword);
}
