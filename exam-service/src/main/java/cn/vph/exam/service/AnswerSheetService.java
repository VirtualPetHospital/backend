package cn.vph.exam.service;

import cn.vph.exam.entity.AnswerSheet;
import cn.vph.exam.entity.UserAnswerSheet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface AnswerSheetService extends IService<AnswerSheet> {
    
    AnswerSheet add(AnswerSheet answerSheet);

    AnswerSheet update(Integer answerSheetId, AnswerSheet answerSheet);

    void delete(Integer answerSheetId);

    AnswerSheet getAnswerSheetByExamId(Integer examId);

    List<UserAnswerSheet> getAnswerSheets(Integer examId);

    Object getAnswerSheetByAnswerSheetId(Integer answerSheetId);
}
