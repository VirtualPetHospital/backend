package cn.vph.cases.service;

import cn.vph.cases.entity.Medicine;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-19 22:56
 **/
public interface MedicineService {
    Object add(Medicine medicine);

    Object update(Medicine medicine);

    Object list(Integer pageNum, Integer pageSize, String nameKeyword);

    Object delete(Integer medicineId);
}
