package cn.vph.cases.service;

import cn.vph.cases.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 22:53
 **/
public interface CategoryService extends IService<Category> {
    Category add(Category category);

    Object delete(Integer categoryId);

    Object update(Category category);
}
