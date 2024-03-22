package cn.vph.exam.service;

import cn.vph.exam.entity.Exam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ExamService extends IService<Exam> {

    IPage<Exam> getExamList(Integer pageSize, Integer pageNum, String nameKeyword, Integer level, Integer sortByStartTime, Boolean participated);

    Exam getExamById(Integer examId);

    Exam add(Exam exam);

    Exam update(Exam exam);

    void delete(Integer examId);

    void enroll(Integer examId);

    void unEnroll(Integer examId);

}