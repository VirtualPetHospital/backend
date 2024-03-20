package cn.vph.exam.service;

import cn.vph.exam.entity.AnswerSheet;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AnswerSheetService extends IService<AnswerSheet> {
    
    AnswerSheet add(AnswerSheet answerSheet);

    AnswerSheet getAnswerSheetById(Integer answerSheetId);
}
