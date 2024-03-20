package cn.vph.exam.service.impl;

import cn.vph.common.CommonConstant;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import cn.vph.common.util.QueryPage;
import cn.vph.exam.entity.Exam;
import cn.vph.exam.entity.Paper;
import cn.vph.exam.entity.Participant;
import cn.vph.exam.mapper.ExamMapper;
import cn.vph.exam.mapper.ParticipantMapper;
import cn.vph.exam.service.ExamService;
import cn.vph.exam.service.PaperService;
import cn.vph.exam.util.SessionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * @author Caroline
 * @description
 * @create 2024/3/13 11:57
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private PaperService paperService;
    
    @Autowired
    private ParticipantMapper participantMapper;

    @Autowired
    private SessionUtil sessionUtil;


    @Override
    public Exam getExamById(Integer examId){
        exists(examId);
        Exam exam = examMapper.selectById(examId);
        exam.setPaper(paperService.getPaperById(exam.getPaperId()));
        return exam;
    }


    @Override
    public IPage<Exam> getExamList(Integer pageSize, Integer pageNum, String nameKeyword, Integer level, Integer sortByStartTime, Boolean participated){
        MPJLambdaWrapper<Exam> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Exam.class)
               .leftJoin(Participant.class, Participant::getExamId, Exam::getExamId);

        // nameKeyword
        // level
        wrapper.like(nameKeyword != null && !nameKeyword.isEmpty(), Exam::getName, nameKeyword)
                .eq(level != null, Exam::getLevel, level);

        // participated
        // null=全列表；true=查该用户已报名已交卷；false=已报名未考试
        if(participated != null){
            wrapper.eq(Participant::getUserId, sessionUtil.getUserId())
                   .eq(Participant::getParticipated, participated);
        }

        // sortByStartTime
        if(sortByStartTime != null){
            wrapper.orderBy(true, sortByStartTime == CommonConstant.SORT_ASC, "start_time");
        }

        // 包装 paper字段
        IPage<Exam> curPage = examMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        if(!curPage.getRecords().isEmpty()){
            curPage.getRecords().forEach(exam -> {
                exam.setPaper(paperService.getPaperById(exam.getPaperId()));
            });
        }
        return curPage;
    }


    @Override
    @Transactional
    public Exam add(Exam exam){
        examTimeIsValid(exam);
        examMapper.insert(exam);
        return exam;
    }

    @Override
    @Transactional
    public Exam update(Exam exam){
        exists(exam.getExamId());
        examTimeIsValid(exam);
        examMapper.updateById(exam);
        return exam;
    }

    @Override
    @Transactional
    public void delete(Integer examId){
        exists(examId);
        examMapper.deleteById(examId);
    }

    @Override
    @Transactional
    public void enroll(Integer examId){
        exists(examId);
        /**
         * 1. 暂时只有学生可以报名
         * 2. 一个学生只能报名一次
         */
        AssertUtil.isTrue(sessionUtil.getUserType().equals(CommonConstant.USER_STUDENT), CommonErrorCode.UNAUTHORIZED_ACCESS);
        Participant participant = new Participant(examId, sessionUtil.getUserId(), false);
        AssertUtil.isTrue(participantMapper.selectCount(new LambdaQueryWrapper<>(new Participant(examId, participant.getUserId()))) > 0, CommonErrorCode.EXAM_ALREADY_ENROLLED);

        participantMapper.insert(participant);
    }

    @Override
    @Transactional
    public void unEnroll(Integer examId){
        exists(examId);
        Participant participant = new Participant(examId, sessionUtil.getUserId(), false);
        participantMapper.delete(new LambdaQueryWrapper<>(participant));
    }

    /**
     * ----------------------------------------------------
     * discard the method below
     * ----------------------------------------------------
     */
    @Override
    public IPage<Exam> listAllExams(Exam exam, QueryPage queryPage){
        IPage<Exam> page = new Page<>(queryPage.getPage(), queryPage.getLimit());
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Exam::getExamId);
        return examMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Exam> listAllExams(Exam exam){
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Exam::getExamId);
        return examMapper.selectList(queryWrapper);
    }


    private void exists(Integer examId){
        Exam exam = examMapper.selectById(examId);
        AssertUtil.isNotNull(exam, CommonErrorCode.EXAM_NOT_EXIST);
    }

    // TODO 是否检查exam.name重名

    private void examTimeIsValid(Exam exam){
        AssertUtil.isTrue(exam.getStartTime().isBefore(exam.getEndTime()), CommonErrorCode.EXAM_TIME_INVALID);
        AssertUtil.isTrue(ChronoUnit.MINUTES.between(exam.getStartTime(), exam.getEndTime()) == exam.getDuration(), CommonErrorCode.EXAM_TIME_INVALID);
    }
}
