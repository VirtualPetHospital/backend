package cn.vph.exam.service.impl;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import cn.vph.exam.clients.CaseServiceFeignClient;
import cn.vph.exam.entity.*;
import cn.vph.exam.mapper.AnswerSheetItemMapper;
import cn.vph.exam.mapper.AnswerSheetMapper;
import cn.vph.exam.mapper.ExamMapper;
import cn.vph.exam.mapper.ParticipantMapper;
import cn.vph.exam.service.AnswerSheetService;
import cn.vph.exam.service.PaperService;
import cn.vph.exam.service.ParticipantService;
import cn.vph.exam.util.SessionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    @Autowired
    private CaseServiceFeignClient caseServiceFeignClient;
    @Autowired
    private ParticipantMapper participantMapper;


    /**
     * 条件：
     *     已报名，且在合法时间内
     * @param answerSheet
     * @return
     */
    @Override
    @Transactional
    public AnswerSheet add(AnswerSheet answerSheet) {

        // 设置用户id
        answerSheet.setUserId(sessionUtil.getUserId());
        // 检查答题卡是否不存在
        AssertUtil.isFalse(answerSheetAlreadyExist(answerSheet.getExamId(), answerSheet.getUserId()), CommonErrorCode.ANSWER_SHEET_ALREADY_EXIST);

        // 检查是否报名 未报名无法提交答题卡
        boolean enrolled = participantService.isEnrolled(answerSheet.getExamId(), sessionUtil.getUserId());
        AssertUtil.isTrue(enrolled, CommonErrorCode.NOT_ENROLLED);

        // 检查是否在考试时间内
        AssertUtil.isTrue(examMapper.selectById(answerSheet.getExamId()).getEndTime().isAfter(LocalDateTime.now()), CommonErrorCode.EXAM_TIME_EXPIRED);
        /*

        if (!enrolled) {
            participantService.add(answerSheet.getExamId(), sessionUtil.getUserId());
        }*/
        // participated 设置为true
        Participant participant = participantService.getParticipant(answerSheet.getExamId(), sessionUtil.getUserId());
        participantService.updateToParticipated(participant.getId());

        // 查出对应的试卷
        Paper paper = paperService.getPaperById(examMapper.selectById(answerSheet.getExamId()).getPaperId());
        // 检查答题卡是否和试卷对应
        checkAnswerSheetMatchAndCalculateScore(answerSheet, paper);

        answerSheet.setUpdateTime(LocalDateTime.now());
        answerSheetMapper.insert(answerSheet);

        answerSheet.getAnswers().forEach(answerSheetItem -> {
            AssertUtil.isTrue(answerSheetItem.getQuestionId() != null, CommonErrorCode.QUESTION_NOT_EXIST);

            answerIsValid(answerSheetItem.getAnswer());
            answerSheetItem.setAnswerSheetId(answerSheet.getAnswerSheetId());
            answerSheetItemMapper.insert(answerSheetItem);
        });
        return answerSheet;
    }

    /**
     * 条件：
     *     已有答题卡，且在合法时间内
     * @param answerSheetId
     * @param answerSheet
     * @return
     */
    @Override
    @Transactional
    public AnswerSheet update(Integer answerSheetId, AnswerSheet answerSheet) {
        AssertUtil.isTrue(answerSheetId != null && answerSheet != null && answerSheetId.equals(answerSheet.getAnswerSheetId()), CommonErrorCode.EXAM_NOT_EXIST);
        answerSheet.setUserId(sessionUtil.getUserId());
        answerSheet.setAnswerSheetId(answerSheetId);
        answerSheet.setUpdateTime(LocalDateTime.now());
        // 检查答题卡是否已存在
        AssertUtil.isTrue(answerSheetAlreadyExist(answerSheet.getExamId(), answerSheet.getUserId()), CommonErrorCode.ANSWER_SHEET_NOT_EXIST);

        // add 时已经添加了participant了 这里不处理

        // 查出对应的试卷
        Paper paper = paperService.getPaperById(examMapper.selectById(answerSheet.getExamId()).getPaperId());
        // 检查答题卡是否和试卷对应
        checkAnswerSheetMatchAndCalculateScore(answerSheet, paper);

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
        setAnswerSheetAnswers(answerSheet);
//        List<Integer> answerSheetItemIds = answerSheetItemMapper.selectList(
//                        new LambdaQueryWrapper<AnswerSheetItem>()
//                                .eq(AnswerSheetItem::getAnswerSheetId, answerSheet.getAnswerSheetId()))
//                .stream()
//                .map(AnswerSheetItem::getId)
//                .collect(Collectors.toList());
//        if (answerSheetItemIds.isEmpty()) {
//            answerSheet.setAnswers(Collections.emptyList());
//        } else {
//            answerSheet.setAnswers(answerSheetItemMapper.selectBatchIds(answerSheetItemIds));
//        }
        return answerSheet;
    }

    @Override
    public List<UserAnswerSheet> getAnswerSheets(Integer examId) {
        // 查看考试是否存在
        AssertUtil.isNotNull(examMapper.selectOne(new LambdaQueryWrapper<Exam>().eq(Exam::getExamId, examId)), CommonErrorCode.EXAM_NOT_EXIST);

        // 查询 对应考试内 所有有效的答题卡id和用户id
        List<UserAnswerSheet> userAnswerSheets = participantMapper.selectJoinList(UserAnswerSheet.class, new MPJLambdaWrapper<Participant>()
                .leftJoin(AnswerSheet.class, AnswerSheet::getUserId, Participant::getUserId)
                .selectAs(AnswerSheet::getAnswerSheetId, UserAnswerSheet::getAnswerSheetId)
                .selectAs(Participant::getUserId, UserAnswerSheet::getUserId)
                .eq(AnswerSheet::getExamId, Participant::getExamId)
                .eq(Participant::getExamId, examId)
                .eq(Participant::getParticipated, true)
        );
        // 无答题卡时
        AssertUtil.isFalse(userAnswerSheets.isEmpty(), CommonErrorCode.NO_ANSWER_SHEET);
        // 从userAnswerSheets中抽离出userid List
        List<Integer> userIds = userAnswerSheets.stream().map(UserAnswerSheet::getUserId).collect(Collectors.toList());
        // 获取nickname List
        List<String> nicknames = caseServiceFeignClient.getNicknamesByIds(userIds);

        // 合并user_ids, nicknames, answer
        for (int i = 0; i < userAnswerSheets.size(); i++) {
            userAnswerSheets.get(i).setNickname(nicknames.get(i));
        }
        return userAnswerSheets;
    }

    @Override
    public Object getAnswerSheetByAnswerSheetId(Integer answerSheetId) {
        AnswerSheet answerSheet = answerSheetMapper.selectOne(new LambdaQueryWrapper<AnswerSheet>().eq(AnswerSheet::getAnswerSheetId, answerSheetId));
        AssertUtil.isNotNull(answerSheet, CommonErrorCode.ANSWER_SHEET_NOT_EXIST);
        setAnswerSheetAnswers(answerSheet);
        return answerSheet;
    }

    public void setAnswerSheetAnswers(AnswerSheet answerSheet) {
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
    }

    @Override
    @Transactional
    public void delete(Integer answerSheetId) {
        if (answerSheetId == null) {
            return;
        }
        AnswerSheet answerSheet = answerSheetMapper.selectById(answerSheetId);
        if (answerSheet != null) {

            answerSheetItemMapper.delete(new LambdaQueryWrapper<AnswerSheetItem>().eq(AnswerSheetItem::getAnswerSheetId, answerSheetId));
            answerSheetMapper.deleteById(answerSheetId);
        }
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

    private void checkAnswerSheetMatchAndCalculateScore(AnswerSheet answerSheet, Paper paper) {

        int size = paper.getQuestionNum();
        int correctCnt = 0;
        AssertUtil.isTrue(answerSheet.getAnswers().size() == size, CommonErrorCode.ANSWER_SHEET_NOT_MATCH);
        // 遍历答题卡中的题目
        for (int i = 0; i < answerSheet.getAnswers().size(); i++) {
            AnswerSheetItem answerSheetItem = answerSheet.getAnswers().get(i);
            String userAnswer = answerSheetItem.getAnswer();
            // 答题卡中的题目和paper中的对应
            AssertUtil.isTrue(answerSheetItem.getQuestionId().equals(paper.getQuestions().get(i).getQuestionId()), CommonErrorCode.ANSWER_SHEET_NOT_MATCH);
            // 查询试题，比较answersheet的答案和正确答案
            if (userAnswer != null && userAnswer.equals(paper.getQuestions().get(i).getAnswer())) {
                correctCnt++;
            }
        }
        answerSheet.setScore((double) correctCnt / size * 100);
        calculateUpgrade();
    }

    private void calculateUpgrade() {
        // 连接participant表和exam表，查询用户已经参与的,level等于user_level的exam
        Integer userLevel = sessionUtil.getUserLevel();
        MPJLambdaWrapper<Participant> wrapper = new MPJLambdaWrapper<>();
        wrapper.leftJoin(Exam.class, Exam::getExamId, Participant::getExamId);
        wrapper.eq(Participant::getUserId, sessionUtil.getUserId());
        wrapper.eq(Participant::getParticipated, true);
        wrapper.eq(Exam::getLevel, userLevel);
        // selectCount
        int count = participantMapper.selectCount(wrapper).intValue();
        caseServiceFeignClient.upgrade(count, sessionUtil.getUserId(), sessionUtil.getSessionId());
        log.info(sessionUtil.getSessionId());
    }
}
