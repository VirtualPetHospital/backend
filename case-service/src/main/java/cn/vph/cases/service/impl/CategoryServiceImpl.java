package cn.vph.cases.service.impl;

import cn.vph.cases.clients.QuestionFeignClient;
import cn.vph.cases.entity.Category;
import cn.vph.cases.mapper.CategoryMapper;
import cn.vph.cases.service.CategoryService;
import cn.vph.cases.util.SessionUtil;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 22:53
 **/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private QuestionFeignClient questionFeignClient;

    public Category selectByName(String name) {
        MPJLambdaWrapper<Category> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.eq(Category::getName, name);
        return categoryMapper.selectOne(queryWrapper);
    }
    @Override
    @Transactional
    public Category add(Category category) {
        // 已存在则抛出异常
        Category cate = selectByName(category.getName());
        AssertUtil.isNull(cate, CommonErrorCode.CATEGORY_ALREADY_EXIST);
        categoryMapper.insert(category);
        return category;
    }

    @Autowired
    private SessionUtil sessionUtil;

    @Override
    @Transactional
    public Object delete(Integer categoryId) {
        // 不存在则抛出异常
        AssertUtil.isNotNull(categoryMapper.selectById(categoryId), CommonErrorCode.CATEGORY_NOT_EXIST);
        System.out.println("userid ===============");
        System.out.println(sessionUtil.getUserId());
        Long count = questionFeignClient.getQuestionCountByCategoryId(categoryId);
        AssertUtil.isTrue(count == 0, CommonErrorCode.CATEGORY_HAS_QUESTION);
        try {
            categoryMapper.deleteById(categoryId);
        } catch (Exception e) {
            throw new CommonException(CommonErrorCode.CANNOT_DELETE_CATEGORY);
        }
        return null;
    }

    @Override
    @Transactional
    public Object update(Category category) {
        //是否存在相同名字的病种
        Category cate = selectByName(category.getName());
        //相同名字的病种是否是当前病种（更新，单名字相同）
        AssertUtil.isTrue(cate == null || cate.getCategoryId().equals(category.getCategoryId()), CommonErrorCode.CATEGORY_ALREADY_EXIST);
        categoryMapper.updateById(category);
        return category;
    }
    
}
