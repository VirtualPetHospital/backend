package cn.vph.exam.service.impl;

import cn.vph.exam.entity.Participant;
import cn.vph.exam.mapper.ParticipantMapper;
import cn.vph.exam.service.ParticipantService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Caroline
 * @description
 * @create 2024/3/19 1:22
 */
@Service
public class ParticipantServiceImpl extends ServiceImpl<ParticipantMapper, Participant> implements ParticipantService {

    @Autowired
    private ParticipantMapper participantMapper;

    @Override
    public Participant add(Integer examId, Integer userId){
        Participant participant = new Participant(examId, userId, false);
        participantMapper.insert(participant);
        return participant;
    }

    @Override
    public Participant updateToParticipated(Integer participantId){
        Participant participant = participantMapper.selectById(participantId);
        participant.setParticipated(true);
        participantMapper.updateById(participant);
        return participant;
    }

    @Override
    public boolean isEnrolled(Integer examId, Integer userId){
        LambdaQueryWrapper<Participant> query = new LambdaQueryWrapper<>();
        query.eq(Participant::getExamId, examId)
                .eq(Participant::getUserId, userId);
        return participantMapper.selectCount(query) > 0;
    }

    @Override
    public Participant getParticipant(Integer examId, Integer userId){
        LambdaQueryWrapper<Participant> query = new LambdaQueryWrapper<>();
        query.eq(Participant::getExamId, examId)
                .eq(Participant::getUserId, userId);
        return participantMapper.selectOne(query);
    }
}
