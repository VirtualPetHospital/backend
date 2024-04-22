package cn.vph.exam.service.impl;

import cn.vph.common.CommonConstant;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import cn.vph.common.util.TimeUtil;
import cn.vph.exam.entity.Exam;
import cn.vph.exam.entity.Participant;
import cn.vph.exam.mapper.ExamMapper;
import cn.vph.exam.mapper.ParticipantMapper;
import cn.vph.exam.service.AnswerSheetService;
import cn.vph.exam.service.ExamService;
import cn.vph.exam.service.PaperService;
import cn.vph.exam.util.SessionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    private AnswerSheetService answerSheetService;

    @Autowired
    private SessionUtil sessionUtil;


    @Override
    public Exam getExamById(Integer examId) {
        exists(examId);
        Exam exam = examMapper.selectById(examId);
        exam.setPaper(paperService.getPaperById(exam.getPaperId()));
        return exam;
    }


    @Override
    public IPage<Exam> getExamList(Integer pageSize, Integer pageNum, String nameKeyword, Integer level, Integer sortByStartTime, Boolean participated) {
        // 如果给了participated，直接join就行
        // 如果每给participated，需要union
        MPJLambdaWrapper<Exam> wrapper = JoinWrappers.lambda(Exam.class);
        wrapper.selectAll(Exam.class)
                .selectAs(Participant::getParticipated, Exam::getParticipated)
                .leftJoin(Participant.class, on -> on.eq(Exam::getExamId, Participant::getExamId).eq(Participant::getUserId, sessionUtil.getUserId()));

        // nameKeyword
        // level
        wrapper.like(nameKeyword != null && !nameKeyword.isEmpty(), Exam::getName, nameKeyword)
                .eq(level != null, Exam::getLevel, level);

        // participated
        // null=全列表；true=查该用户已报名已交卷；false=已报名未考试
        if (participated != null) {
            wrapper.eq(Participant::getParticipated, participated);
        }
        // sortByStartTime
        if (sortByStartTime != null) {
            wrapper.orderBy(true, sortByStartTime == CommonConstant.SORT_ASC, "start_time");
        }
        // 包装 paper字段
        IPage<Exam> curPage = examMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        if (!curPage.getRecords().isEmpty()) {
            curPage.getRecords().forEach(exam -> {
                exam.setPaper(paperService.getPaperByIdWithoutQuestions(exam.getPaperId()));
            });
        }
        return curPage;
    }


    @Override
    @Transactional
    public Exam add(Exam exam) {
        // 不与其他exam重名
        Exam checkingExam = examMapper.selectOne(new LambdaQueryWrapper<Exam>().eq(Exam::getName, exam.getName()));
        AssertUtil.isTrue(checkingExam == null, CommonErrorCode.EXAM_NAME_ALREADY_EXIST);
        examTimeIsValid(exam);
        AssertUtil.isNotNull(paperService.getPaperById(exam.getPaperId()), CommonErrorCode.PAPER_NOT_EXIST);
        examMapper.insert(exam);
        return exam;
    }

    @Override
    @Transactional
    public Exam update(Exam exam) {
        // 查询是否存在
        Exam existExam = exists(exam.getExamId());
        // 考试开始后无法修改
        // 获取当前时间，并格式化为：yyyy-MM-dd HH:mm:ss
        AssertUtil.isTrue(existExam.getStartTime().isAfter(LocalDateTime.now()), CommonErrorCode.EXAM_HAS_PAST);
        // 不与其他exam重名
        Exam checkingExam = examMapper.selectOne(new LambdaQueryWrapper<Exam>().eq(Exam::getName, exam.getName()));
        AssertUtil.isTrue(checkingExam == null || checkingExam.getExamId().equals(exam.getExamId()), CommonErrorCode.EXAM_NAME_ALREADY_EXIST);

        exists(exam.getExamId());
        examTimeIsValid(exam);
        AssertUtil.isNotNull(paperService.getPaperById(exam.getPaperId()), CommonErrorCode.PAPER_NOT_EXIST);
        examMapper.updateById(exam);
        return exam;
    }

    /**
     * 满足条件：
     *    时间：当前时间之后的考试
     *    报名情况：无学生报名
     * @param examId
     */
    @Override
    @Transactional
    public void delete(Integer examId) {
        exists(examId);
        Exam curExam = examMapper.selectById(examId);
        AssertUtil.isTrue(curExam.getStartTime().isAfter(LocalDateTime.now()), CommonErrorCode.EXAM_HAS_PAST);
        LambdaQueryWrapper<Participant> participantWrapper = new LambdaQueryWrapper<>();
        participantWrapper.eq(Participant::getExamId, examId);
        AssertUtil.isTrue(participantMapper.selectCount(participantWrapper) == 0, CommonErrorCode.EXAM_HAS_PARTICIPANTS);

        examMapper.deleteById(examId);
    }

    @Override
    @Transactional
    public void enroll(Integer examId) {
        exists(examId);
        /**
         * 1. 暂时只有学生可以报名
         * 2. 一个学生只能报名一次
         */
        // 获取学生类型
        AssertUtil.isTrue(sessionUtil.getUserType().equals(CommonConstant.USER_STUDENT), CommonErrorCode.UNAUTHORIZED_ACCESS);

        // 获取考试
        Exam exam = examMapper.selectById(examId);
        // 考试存在
        AssertUtil.isNotNull(exam, CommonErrorCode.EXAM_NOT_EXIST);
        // 学生level大于等于考试level
        AssertUtil.isTrue(sessionUtil.getUserLevel() >= exam.getLevel(), CommonErrorCode.LEVEL_NOT_MATCH);

        LambdaQueryWrapper<Participant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Participant::getExamId, examId)
                .eq(Participant::getUserId, sessionUtil.getUserId());
        // 考试开始后无法报名
        AssertUtil.isTrue(TimeUtil.getCurrentTime().compareTo(exam.getStartTime().toString()) < 0 , CommonErrorCode.UNABLE_TO_ENROLL_AFTER_START_TIME);
        AssertUtil.isTrue(
                participantMapper.selectCount(wrapper) < 1,
                CommonErrorCode.EXAM_ALREADY_ENROLLED);
        participantMapper.insert(new Participant(examId, sessionUtil.getUserId(), false));
    }

    @Override
    @Transactional
    public void unEnroll(Integer examId) {
        exists(examId);
        Participant participant = new Participant(examId, sessionUtil.getUserId(), false);
        //考试开始后无法取消报名
        participantMapper.delete(new LambdaQueryWrapper<>(participant));
    }


    private Exam exists(Integer examId) {
        Exam exam = examMapper.selectById(examId);
        AssertUtil.isNotNull(exam, CommonErrorCode.EXAM_NOT_EXIST);
        return exam;
    }


    private void examTimeIsValid(Exam exam) {
        AssertUtil.isTrue(exam.getStartTime().isBefore(exam.getEndTime()), CommonErrorCode.EXAM_TIME_INVALID);
        AssertUtil.isTrue(ChronoUnit.MINUTES.between(exam.getStartTime(), exam.getEndTime()) == exam.getDuration(), CommonErrorCode.EXAM_TIME_INVALID);
    }

}
