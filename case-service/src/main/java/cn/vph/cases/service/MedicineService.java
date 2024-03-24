package cn.vph.cases.service;

import cn.vph.cases.entity.Medicine;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-19 22:56
 **/
public interface MedicineService extends IService<Medicine> {
    Object add(Medicine medicine);

    Object update(Medicine medicine);

    IPage<?> list(Integer pageNum, Integer pageSize, String nameKeyword);

    Object delete(Integer medicineId);

    Object get(Integer medicineId);
}
