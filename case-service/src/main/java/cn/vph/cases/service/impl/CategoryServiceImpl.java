package cn.vph.cases.service.impl;

import cn.vph.cases.entity.Category;
import cn.vph.cases.mapper.CategoryMapper;
import cn.vph.cases.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Caroline
 * @description
 * @create 2024/3/18 0:03
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

}
