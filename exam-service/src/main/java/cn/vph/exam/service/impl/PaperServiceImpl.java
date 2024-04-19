package cn.vph.exam.service.impl;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import cn.vph.exam.entity.Paper;
import cn.vph.exam.entity.PaperQuestion;
import cn.vph.exam.mapper.PaperMapper;
import cn.vph.exam.mapper.PaperQuestionMapper;
import cn.vph.exam.mapper.QuestionMapper;
import cn.vph.exam.service.PaperQuestionService;
import cn.vph.exam.service.PaperService;
import cn.vph.exam.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caroline
 * @description 试卷接口实现
 * @create 2024/3/17 17:12
 */

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @Autowired
    private PaperQuestionService paperQuestionService;


    public Paper getPaperByIdWithoutQuestions(Integer paperId){
        exists(paperId);
        return paperMapper.selectById(paperId);
    }
    @Override
    public Paper getPaperById(Integer paperId) {
        exists(paperId);
        Paper paper = paperMapper.selectById(paperId);
        initPaper(paper);
        return paper;
    }


    @Override
    @Transactional
    public Paper add(Paper paper) {
        questionNumIsValid(paper);
        // 检查试卷名是否重复
        Paper checkingPaper = paperMapper.selectOne(new LambdaQueryWrapper<Paper>().eq(Paper::getName, paper.getName()));

        AssertUtil.isTrue(checkingPaper == null || checkingPaper.getPaperId().equals(paper.getPaperId()), CommonErrorCode.PAPER_NAME_ALREADY_EXIST);


        paperMapper.insert(paper);

        // 更新关系表
        updatePaperQuestion(paper);
        return paper;
    }

    @Override
    @Transactional
    public Paper update(Paper paper) {
        exists(paper.getPaperId());
        questionNumIsValid(paper);

        // 检查试卷名是否重复
        Paper checkingPaper = paperMapper.selectOne(new LambdaQueryWrapper<Paper>().eq(Paper::getName, paper.getName()));
        AssertUtil.isTrue(checkingPaper == null || checkingPaper.getPaperId().equals(paper.getPaperId()), CommonErrorCode.PAPER_NAME_ALREADY_EXIST);

        paperMapper.updateById(paper);

        // 更新关系表
        updatePaperQuestion(paper);
        return paper;
    }

    @Override
    @Transactional
    public void delete(Integer paperId) {
        exists(paperId);
        // 先删除关系表条目，再删除试卷
        paperQuestionService.deleteByPaperId(paperId);
        paperMapper.deleteById(paperId);
    }

    // 封装试卷的题目列表
    @Override
    public void initPaper(Paper paper) {
        List<Integer> questionIds = paperQuestionMapper.selectList(
                new LambdaQueryWrapper<PaperQuestion>()
                        .eq(PaperQuestion::getPaperId, paper.getPaperId())
        ).stream().map(PaperQuestion::getQuestionId).collect(Collectors.toList());

        if (questionIds.isEmpty()) {
            paper.setQuestions(Collections.emptyList());
        } else {
            paper.setQuestions(questionMapper.selectBatchIds(questionIds));
        }
    }

    private void updatePaperQuestion(Paper paper) {
        /**
         * 删除旧的PaperQuestion关系表条目
         */
        paperQuestionService.deleteByPaperId(paper.getPaperId());

        /**
         * 添加PaperQuestion关系表条目
         */
        paper.getQuestions().forEach(question -> {
            PaperQuestion paperQuestion = new PaperQuestion(paper.getPaperId(), question.getQuestionId());
            paperQuestionService.add(paperQuestion);
        });
    }

    private void exists(Integer paperId) {
        Paper paper = paperMapper.selectById(paperId);
        AssertUtil.isNotNull(paper, CommonErrorCode.PAPER_NOT_EXIST);
    }


    private void questionNumIsValid(Paper paper) {
        AssertUtil.isTrue(paper.getQuestionNum() > 0, CommonErrorCode.QUESTION_NUM_INVALID);
        AssertUtil.isTrue(paper.getQuestions().size() == paper.getQuestionNum(), CommonErrorCode.QUESTION_NUM_INVALID);
    }
}
