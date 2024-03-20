package cn.vph.exam.service.impl;

import cn.vph.exam.entity.PaperQuestion;
import cn.vph.exam.mapper.PaperQuestionMapper;
import cn.vph.exam.service.PaperQuestionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Caroline
 * @description 试卷题目多对多关系表
 * @create 2024/3/17 17:37
 */

@Service
public class PaperQuestionServiceImpl extends ServiceImpl<PaperQuestionMapper, PaperQuestion> implements PaperQuestionService {

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    private boolean exists(PaperQuestion paperQuestion){
        LambdaQueryWrapper<PaperQuestion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaperQuestion::getPaperId, paperQuestion.getPaperId());
        queryWrapper.eq(PaperQuestion::getQuestionId, paperQuestion.getQuestionId());
        return paperQuestionMapper.selectList(queryWrapper).size() > 0;
    }


    @Override
    public void add(PaperQuestion paperQuestion) {
        if(!exists(paperQuestion)){
            paperQuestionMapper.insert(paperQuestion);
        }
    }

    @Override
    @Transactional
    public void deleteByPaperId(Integer paperId) {
        LambdaQueryWrapper<PaperQuestion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaperQuestion::getPaperId, paperId);
        paperQuestionMapper.delete(queryWrapper);
    }


    @Override
    @Transactional
    public void deleteByQuestionId(Integer questionId) {
        LambdaQueryWrapper<PaperQuestion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaperQuestion::getQuestionId, questionId);
        paperQuestionMapper.delete(queryWrapper);
    }

}
