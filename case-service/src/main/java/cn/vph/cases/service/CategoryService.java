package cn.vph.cases.service;

import cn.vph.cases.entity.Category;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 22:53
 **/
public interface CategoryService {
    Category add(Category category);

    Object delete(Integer categoryId);

    Object update(Category category);

    Object list();
}
