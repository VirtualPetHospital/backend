package cn.vph.exam.service.impl;

import cn.vph.common.util.QueryPage;
import cn.vph.exam.entity.Exam;
import cn.vph.exam.mapper.ExamMapper;
import cn.vph.exam.service.ExamService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Caroline
 * @description
 * @create 2024/3/13 11:57
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Autowired
    private ExamMapper examMapper;


    @Override
    public IPage<Exam> listAllExams(Exam exam, QueryPage queryPage){
        IPage<Exam> page = new Page<>(queryPage.getPage(), queryPage.getLimit());
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Exam::getExamId);
        return examMapper.selectPage(page, queryWrapper);

    }

    @Override
    public List<Exam> listAllExams(Exam exam){
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Exam::getExamId);
        return examMapper.selectList(queryWrapper);
    }
}
