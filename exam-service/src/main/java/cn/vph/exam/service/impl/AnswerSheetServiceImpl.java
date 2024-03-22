package cn.vph.exam.service.impl;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.utils.AssertUtil;
import cn.vph.exam.entity.AnswerSheet;
import cn.vph.exam.entity.AnswerSheetItem;
import cn.vph.exam.entity.Participant;
import cn.vph.exam.mapper.AnswerSheetItemMapper;
import cn.vph.exam.mapper.AnswerSheetMapper;
import cn.vph.exam.mapper.ParticipantMapper;
import cn.vph.exam.service.AnswerSheetService;
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
    private ParticipantMapper participantMapper;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private AnswerSheetItemMapper answerSheetItemMapper;


    @Override
    @Transactional
    public AnswerSheet add(AnswerSheet answerSheet){

        // 设置用户id
        answerSheet.setUserId(sessionUtil.getUserId());

        answerSheetAlreadyExist(answerSheet.getExamId(), answerSheet.getUserId());

        // 检查是否报名 未报名自动报名
        // participated 设置为true
        boolean enrolled = participantService.isEnrolled(answerSheet.getExamId(), sessionUtil.getUserId());
        if(!enrolled){
            participantService.add(answerSheet.getExamId(), sessionUtil.getUserId());
        }
        Participant participant = participantService.getParticipant(answerSheet.getExamId(), sessionUtil.getUserId());
        participantService.updateToParticipated(participant.getId());

        // TODO 题目数量==exam题目数量校验
        AssertUtil.isTrue(answerSheet.getAnswers().size() > 0, CommonErrorCode.ANSWERS_NOT_EXIST);

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
    public AnswerSheet getAnswerSheetById(Integer answerSheetId){
        AssertUtil.isTrue(answerSheetId != null, CommonErrorCode.ANSWER_SHEET_NOT_EXIST);

        AnswerSheet answerSheet = answerSheetMapper.selectById(answerSheetId);
        AssertUtil.isTrue(answerSheet != null, CommonErrorCode.ANSWER_SHEET_NOT_EXIST);
        List<Integer> answerSheetItemIds = answerSheetItemMapper.selectList(
                new LambdaQueryWrapper<AnswerSheetItem>()
                        .eq(AnswerSheetItem::getAnswerSheetId, answerSheetId))
                        .stream()
                        .map(AnswerSheetItem::getId)
                        .collect(Collectors.toList());
        if(answerSheetItemIds.isEmpty()){
            answerSheet.setAnswers(Collections.emptyList());
        }
        else{
            answerSheet.setAnswers(answerSheetItemMapper.selectBatchIds(answerSheetItemIds));
        }
        return answerSheet;
    }

    private void answerSheetAlreadyExist(Integer examId, Integer userId){
        AssertUtil.isTrue(
                answerSheetMapper.selectCount(
                        new LambdaQueryWrapper<AnswerSheet>()
                                .eq(AnswerSheet::getExamId, examId)
                                .eq(AnswerSheet::getUserId, userId)) > 0,
                CommonErrorCode.ANSWER_SHEET_ALREADY_EXIST);
    }
    private void answerIsValid(String answer){
        AssertUtil.isTrue(
                answer.equals("A") ||  answer.equals("B") ||
                        answer.equals("C") || answer.equals("D") ,
                CommonErrorCode.QUESTION_ANSWER_NOT_VALID);
    }
}
