package cn.vph.exam.service.impl;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import cn.vph.exam.entity.AnswerSheet;
import cn.vph.exam.entity.AnswerSheetItem;
import cn.vph.exam.entity.Paper;
import cn.vph.exam.entity.Participant;
import cn.vph.exam.mapper.AnswerSheetItemMapper;
import cn.vph.exam.mapper.AnswerSheetMapper;
import cn.vph.exam.mapper.ExamMapper;
import cn.vph.exam.service.AnswerSheetService;
import cn.vph.exam.service.PaperService;
import cn.vph.exam.service.ParticipantService;
import cn.vph.exam.util.SessionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caroline
 * @description 答题卡接口实现
 * @create 2024/3/19 20:00
 */
@Service
public class AnswerSheetServiceImpl extends ServiceImpl<AnswerSheetMapper, AnswerSheet> implements AnswerSheetService {

    @Autowired
    private AnswerSheetMapper answerSheetMapper;

    @Autowired
    private PaperService paperService;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private AnswerSheetItemMapper answerSheetItemMapper;


    @Override
    @Transactional
    public AnswerSheet add(AnswerSheet answerSheet) {

        // 设置用户id
        answerSheet.setUserId(sessionUtil.getUserId());
        // 检查答题卡是否不存在
        AssertUtil.isFalse(answerSheetAlreadyExist(answerSheet.getExamId(), answerSheet.getUserId()), CommonErrorCode.ANSWER_SHEET_ALREADY_EXIST);

        // 检查是否报名 未报名自动报名
        // participated 设置为true
        boolean enrolled = participantService.isEnrolled(answerSheet.getExamId(), sessionUtil.getUserId());
        if (!enrolled) {
            participantService.add(answerSheet.getExamId(), sessionUtil.getUserId());
        }
        Participant participant = participantService.getParticipant(answerSheet.getExamId(), sessionUtil.getUserId());
        participantService.updateToParticipated(participant.getId());

        // 查出对应的试卷
        Paper paper = paperService.getPaperById(examMapper.selectById(answerSheet.getExamId()).getPaperId());
        // 检查答题卡是否和试卷对应
        checkAnswerSheetAndPaper(answerSheet, paper);

        answerSheet.setCreateTime(LocalDateTime.now());
        answerSheetMapper.insert(answerSheet);

        answerSheet.getAnswers().forEach(answerSheetItem -> {
            AssertUtil.isTrue(answerSheetItem.getQuestionId() != null, CommonErrorCode.QUESTION_NOT_EXIST);

            answerIsValid(answerSheetItem.getAnswer());
            answerSheetItem.setAnswerSheetId(answerSheet.getAnswerSheetId());
            answerSheetItemMapper.insert(answerSheetItem);
        });
        return answerSheet;
    }

    @Override
    @Transactional
    public AnswerSheet update(Integer answerSheetId, AnswerSheet answerSheet) {
        AssertUtil.isTrue(answerSheetId != null && answerSheet != null, CommonErrorCode.EXAM_NOT_EXIST);
        answerSheet.setUserId(sessionUtil.getUserId());
        answerSheet.setAnswerSheetId(answerSheetId);
        answerSheet.setCreateTime(LocalDateTime.now());
        // 检查答题卡是否已存在
        AssertUtil.isTrue(answerSheetAlreadyExist(answerSheet.getExamId(), answerSheet.getUserId()), CommonErrorCode.ANSWER_SHEET_NOT_EXIST);

        // add 时已经添加了participant了 这里不处理

        // 查出对应的试卷
        Paper paper = paperService.getPaperById(examMapper.selectById(answerSheet.getExamId()).getPaperId());
        // 检查答题卡是否和试卷对应
        checkAnswerSheetAndPaper(answerSheet, paper);

        answerSheetMapper.updateById(answerSheet);

        answerSheet.getAnswers().forEach(answerSheetItem -> {
            AssertUtil.isNotNull(answerSheetItem.getId(), CommonErrorCode.ANSWER_SHEET_ITEM_VALUE_ERROR);
            answerIsValid(answerSheetItem.getAnswer());
            answerSheetItem.setAnswerSheetId(answerSheet.getAnswerSheetId());
            // xxx 已设置answer字段可更新为null
            answerSheetItemMapper.updateById(answerSheetItem);
        });
        return answerSheet;
    }

    @Override
    public AnswerSheet getAnswerSheetByExamId(Integer examId) {
        AssertUtil.isTrue(examId != null, CommonErrorCode.EXAM_NOT_EXIST);

        LambdaQueryWrapper<AnswerSheet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnswerSheet::getExamId, examId);
        queryWrapper.eq(AnswerSheet::getUserId, sessionUtil.getUserId());
        AnswerSheet answerSheet = answerSheetMapper.selectOne(queryWrapper);

        AssertUtil.isTrue(answerSheet != null, CommonErrorCode.ANSWER_SHEET_NOT_EXIST);
        List<Integer> answerSheetItemIds = answerSheetItemMapper.selectList(
                        new LambdaQueryWrapper<AnswerSheetItem>()
                                .eq(AnswerSheetItem::getAnswerSheetId, answerSheet.getAnswerSheetId()))
                .stream()
                .map(AnswerSheetItem::getId)
                .collect(Collectors.toList());
        if (answerSheetItemIds.isEmpty()) {
            answerSheet.setAnswers(Collections.emptyList());
        } else {
            answerSheet.setAnswers(answerSheetItemMapper.selectBatchIds(answerSheetItemIds));
        }
        return answerSheet;
    }

    private boolean answerSheetAlreadyExist(Integer examId, Integer userId) {
        return answerSheetMapper.selectCount(
                new LambdaQueryWrapper<AnswerSheet>()
                        .eq(AnswerSheet::getExamId, examId)
                        .eq(AnswerSheet::getUserId, userId)) > 0;
    }

    private void answerIsValid(String answer) {
        AssertUtil.isTrue(
                answer == null ||
                        answer.equals("A") || answer.equals("B") ||
                        answer.equals("C") || answer.equals("D"),
                CommonErrorCode.QUESTION_ANSWER_NOT_VALID);
    }

    private void checkAnswerSheetAndPaper(AnswerSheet answerSheet, Paper paper) {
        AssertUtil.isTrue(answerSheet.getAnswers().size() == paper.getQuestions().size(), CommonErrorCode.ANSWER_SHEET_NOT_MATCH);
        // 遍历答题卡中的题目
        for (int i = 0; i < answerSheet.getAnswers().size(); i++) {
            // 答题卡中的题目和paper中的对应
            AssertUtil.isTrue(answerSheet.getAnswers().get(i).getQuestionId().equals(paper.getQuestions().get(i).getQuestionId()), CommonErrorCode.ANSWER_SHEET_NOT_MATCH);
        }
    }
}
