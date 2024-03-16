package cn.vph.exam.service;

import cn.vph.exam.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Caroline
 * @description
 * @create 2024/3/15 0:31
 */
public interface QuestionService extends IService<Question> {

    List<Question> listAllQuestions(Question question);
}
