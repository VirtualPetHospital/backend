package cn.vph.exam.service;

import cn.vph.exam.entity.Paper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Caroline
 * @description
 * @create 2024/3/17 0:35
 */
public interface PaperService extends IService<Paper> {

    Paper getPaperById(Integer paperId);

    Paper add(Paper paper);

    Paper update(Paper paper);

    void delete(Integer paperId);

    void initPaper(Paper paper);
    Paper getPaperByIdWithoutQuestions(Integer paperId);
}
