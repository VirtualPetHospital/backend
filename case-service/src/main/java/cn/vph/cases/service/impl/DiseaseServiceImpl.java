package cn.vph.cases.service.impl;

import cn.vph.cases.entity.Disease;
import cn.vph.cases.mapper.CategoryMapper;
import cn.vph.cases.mapper.DiseaseMapper;
import cn.vph.cases.service.DiseaseService;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import cn.vph.common.utils.AssertUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-19 22:42
 **/
@Service
public class DiseaseServiceImpl extends ServiceImpl<DiseaseMapper, Disease> implements DiseaseService {
    @Autowired
    private DiseaseMapper diseaseMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public Disease selectByName(String name) {
        MPJLambdaWrapper<Disease> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.eq(Disease::getName, name);
        return diseaseMapper.selectOne(queryWrapper);
    }

    @Override
    public Object add(Disease disease) {
        // 判断疾病是否存在
        Disease dis = selectByName(disease.getName());
        AssertUtil.isNull(dis, CommonErrorCode.DISEASE_ALREADY_EXIST);
        // 判断病种是否存在
        AssertUtil.isNotNull(categoryMapper.selectById(disease.getCategoryId()), CommonErrorCode.CATEGORY_NOT_EXIST);
        diseaseMapper.insert(disease);
        return disease;
    }

    @Override
    public Object delete(Integer diseaseId) {
        // 不存在则抛出异常
        AssertUtil.isNotNull(diseaseMapper.selectById(diseaseId), CommonErrorCode.DISEASE_NOT_EXIST);
        // 如果不能删除（外键依赖），则抛出异常
        try{
            diseaseMapper.deleteById(diseaseId);
        }catch (Exception e){
            throw new CommonException(CommonErrorCode.CANNOT_DELETE_DISEASE);
        }
        return null;
    }

    @Override
    public Object update(Disease disease) {
        // 判断疾病是否存在
        Disease dis = selectByName(disease.getName());
        // 判断是否是当前疾病
        AssertUtil.isTrue(dis == null || dis.getDiseaseId().equals(disease.getDiseaseId()), CommonErrorCode.DISEASE_ALREADY_EXIST);
        // 判断病种是否存在
        AssertUtil.isNotNull(categoryMapper.selectById(disease.getCategoryId()), CommonErrorCode.CATEGORY_NOT_EXIST);
        diseaseMapper.updateById(disease);
        return disease;
    }

    @Override
    public Object listByCategory(Integer categoryId) {
        // 判断病种是否存在
        AssertUtil.isNotNull(categoryMapper.selectById(categoryId), CommonErrorCode.CATEGORY_NOT_EXIST);
        MPJLambdaWrapper<Disease> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.eq(Disease::getCategoryId, categoryId);
        return diseaseMapper.selectList(queryWrapper);
    }

    @Override
    public Object get(Integer diseaseId) {
        // 判断是否存在疾病
        Disease disease = diseaseMapper.selectById(diseaseId);
        AssertUtil.isNotNull(disease, CommonErrorCode.DISEASE_NOT_EXIST);
        return disease;
    }
}
