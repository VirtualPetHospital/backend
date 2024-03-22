package cn.vph.exam.service.impl;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import cn.vph.common.utils.AssertUtil;
import cn.vph.exam.entity.PaperQuestion;
import cn.vph.exam.entity.Question;
import cn.vph.exam.mapper.PaperQuestionMapper;
import cn.vph.exam.mapper.QuestionMapper;
import cn.vph.exam.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Caroline
 * @description 已做工作
 * 1. 新增题目时检查answer 是否是 "A" "B" "C" "D"
 * @create 2024/3/15 0:32
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;


    @Override
    public Question getQuestionById(Integer questionId){
        exists(questionId);
        return questionMapper.selectById(questionId);
    }

    @Override
    @Transactional
    public Question add(Question question){
        answerIsValid(question.getAnswer());
        questionMapper.insert(question);
        return question;
    }

    @Override
    @Transactional
    public Question update(Question question){
        exists(question.getQuestionId());
        answerIsValid(question.getAnswer());
        questionMapper.updateById(question);
        return question;
    }

    @Override
    @Transactional
    public void delete(Integer questionId){
        exists(questionId);

        if(questionId > 0){
            // 检查 paper_question 关联项
            LambdaQueryWrapper<PaperQuestion> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PaperQuestion::getQuestionId, questionId);

            // 没有关联项才可以删除题目
            if(paperQuestionMapper.selectList(queryWrapper).size() <= 0){
                questionMapper.deleteById(questionId);
            }
            else{
                throw new CommonException(CommonErrorCode.RELATIONSHIP_STILL_EXIST);
            }
        }
    }

    @Override
    public void exists(Integer questionId) {
        AssertUtil.isNotNull(questionId, CommonErrorCode.QUESTION_NOT_EXIST);
        AssertUtil.isNotNull(questionMapper.selectById(questionId), CommonErrorCode.QUESTION_NOT_EXIST);
    }

    private void answerIsValid(String answer){
        AssertUtil.isTrue(
                answer.equals("A") ||  answer.equals("B") ||
                answer.equals("C") || answer.equals("D") ,
                CommonErrorCode.QUESTION_ANSWER_NOT_VALID);
    }
}
