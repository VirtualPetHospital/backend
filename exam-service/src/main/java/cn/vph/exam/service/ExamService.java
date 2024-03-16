package cn.vph.exam.service;

import cn.vph.common.util.QueryPage;
import cn.vph.exam.entity.Exam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ExamService extends IService<Exam> {

    IPage<Exam> listAllExams(Exam exam, QueryPage queryPage);

    List<Exam> listAllExams(Exam exam);

}