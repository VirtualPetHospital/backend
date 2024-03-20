package cn.vph.exam.service;

import cn.vph.exam.entity.Participant;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @description
 * @create 2024/3/19 0:31
 */
public interface ParticipantService extends IService<Participant> {

    Participant add(Integer examId, Integer userId);

    Participant updateToParticipated(Integer participantId);

    boolean isEnrolled(Integer examId, Integer userId);

    Participant getParticipant(Integer examId, Integer userId);
}
