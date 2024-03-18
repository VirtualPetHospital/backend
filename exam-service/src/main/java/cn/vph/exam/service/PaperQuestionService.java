package cn.vph.exam.service;


import cn.vph.exam.entity.PaperQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Caroline
 * @description
 * @create 2024/3/17 0:31
 */
public interface PaperQuestionService extends IService<PaperQuestion> {

    void add(PaperQuestion paperQuestion);

    void deleteByPaperId(Integer paperId);

    void deleteByQuestionId(Integer questionId);
}
