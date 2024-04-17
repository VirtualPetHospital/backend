package cn.vph.cases.service;

import cn.vph.cases.entity.Medcase;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MedcaseService extends IService<Medcase> {

    IPage<?> getMedcaseList(Integer pageSize, Integer pageNum, String infoKeyword, String nameKeyword, String diseaseName);

    Medcase getMedcaseById(Integer medcaseId);

    Medcase add(Medcase medcase);

    Medcase update(Medcase medcase);

    void delete(Integer medcaseId);

    List<?> getAllMedcaseList(String infoKeyword, String nameKeyword, String diseaseName);
}
