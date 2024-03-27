package cn.vph.exam.service;

import cn.vph.exam.entity.AnswerSheet;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AnswerSheetService extends IService<AnswerSheet> {
    
    AnswerSheet add(AnswerSheet answerSheet);

    AnswerSheet update(Integer answerSheetId, AnswerSheet answerSheet);

    AnswerSheet getAnswerSheetByExamId(Integer examId);
}
