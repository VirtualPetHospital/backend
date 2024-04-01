package cn.vph.exam.service;

import cn.vph.exam.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Caroline
 * @description
 * @create 2024/3/17 0:31
 */
public interface QuestionService extends IService<Question> {

    Question getQuestionById(Integer questionId);

    Question add(Question question);

    Question update(Question question);

    void delete(Integer questionId);

    void exists(Integer questionId);

}
