package cn.vph.exam.service.impl;

import cn.vph.common.CommonConstant;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import cn.vph.exam.entity.AnswerSheet;
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
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
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
        MPJLambdaWrapper<Exam> wrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Exam> notWrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Exam.class)
                .selectAs(Participant::getParticipated, Exam::getParticipated)
                .leftJoin(Participant.class, Participant::getExamId, Exam::getExamId)
                .eq(Participant::getUserId, sessionUtil.getUserId());

        // nameKeyword
        // level
        wrapper.like(nameKeyword != null && !nameKeyword.isEmpty(), Exam::getName, nameKeyword)
                .eq(level != null, Exam::getLevel, level);

        // participated
        // null=全列表；true=查该用户已报名已交卷；false=已报名未考试
        if (participated != null) {
            wrapper.eq(Participant::getParticipated, participated);
        } else {
            notWrapper.selectAll(Exam.class);
            notWrapper.notIn(Exam::getExamId, participantMapper.selectList(new MPJLambdaWrapper<Participant>().eq(Participant::getUserId, sessionUtil.getUserId())));
            wrapper.union(notWrapper);
        }

        // sortByStartTime
        if (sortByStartTime != null) {
            wrapper.orderBy(true, sortByStartTime == CommonConstant.SORT_ASC, "start_time");
        }

        // 包装 paper字段
        IPage<Exam> curPage = examMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        if (!curPage.getRecords().isEmpty()) {
            curPage.getRecords().forEach(exam -> {
                exam.setPaper(paperService.getPaperById(exam.getPaperId()));
//                exam.setParticipated(participated);
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
        // 不与其他exam重名
        Exam checkingExam = examMapper.selectOne(new LambdaQueryWrapper<Exam>().eq(Exam::getName, exam.getName()));
        AssertUtil.isTrue(checkingExam == null || checkingExam.getExamId().equals(exam.getExamId()), CommonErrorCode.EXAM_NAME_ALREADY_EXIST);

        exists(exam.getExamId());
        examTimeIsValid(exam);
        AssertUtil.isNotNull(paperService.getPaperById(exam.getPaperId()), CommonErrorCode.PAPER_NOT_EXIST);
        examMapper.updateById(exam);
        return exam;
    }

    @Override
    @Transactional
    public void delete(Integer examId) {
        exists(examId);
        LambdaQueryWrapper<Participant> participantWrapper = new LambdaQueryWrapper<>();
        participantWrapper.eq(Participant::getExamId, examId);
        participantMapper.delete(participantWrapper);
        LambdaQueryWrapper<AnswerSheet> answerSheetWrapper = new LambdaQueryWrapper<>();
        answerSheetWrapper.eq(AnswerSheet::getExamId, examId);
        List<AnswerSheet> answerSheets = answerSheetService.list(answerSheetWrapper);
        if (!answerSheets.isEmpty()) {
            for (AnswerSheet answerSheet : answerSheets) {
                answerSheetService.delete(answerSheet.getAnswerSheetId());
            }
        }

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
        participantMapper.delete(new LambdaQueryWrapper<>(participant));
    }


    private void exists(Integer examId) {
        Exam exam = examMapper.selectById(examId);
        AssertUtil.isNotNull(exam, CommonErrorCode.EXAM_NOT_EXIST);
    }


    private void examTimeIsValid(Exam exam) {
        AssertUtil.isTrue(exam.getStartTime().isBefore(exam.getEndTime()), CommonErrorCode.EXAM_TIME_INVALID);
        AssertUtil.isTrue(ChronoUnit.MINUTES.between(exam.getStartTime(), exam.getEndTime()) == exam.getDuration(), CommonErrorCode.EXAM_TIME_INVALID);
    }

}
