package cn.vph.exam.service.impl;

import cn.vph.exam.entity.Question;
import cn.vph.exam.mapper.QuestionMapper;
import cn.vph.exam.service.QuestionService;
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
 * @create 2024/3/15 0:32
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Question> listAllQuestions(Question question){
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Question::getQuestionId);
        return questionMapper.selectList(queryWrapper);
    }
}
