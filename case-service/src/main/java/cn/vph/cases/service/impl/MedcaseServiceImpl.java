package cn.vph.cases.service.impl;

import cn.vph.cases.clients.FileFeignClient;
import cn.vph.cases.entity.*;
import cn.vph.cases.mapper.*;
import cn.vph.cases.service.MedcaseInspectionService;
import cn.vph.cases.service.MedcaseMedicineService;
import cn.vph.cases.service.MedcaseService;
import cn.vph.cases.util.SessionUtil;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 13:10
 **/
@Service
public class MedcaseServiceImpl extends ServiceImpl<MedcaseMapper, Medcase> implements MedcaseService {

    @Autowired
    private MedcaseMapper medcaseMapper;
    @Autowired
    private DiseaseMapper diseaseMapper;
    @Autowired
    private OperationMapper operationMapper;
    @Autowired
    private InspectionMapper inspectionMapper;
    @Autowired
    private MedicineMapper medicineMapper;
    @Autowired
    private MedcaseInspectionService medcaseInspectionService;
    @Autowired
    private MedcaseMedicineService medcaseMedicineService;
    @Autowired
    private MedcaseInspectionMapper medcaseInspectionMapper;
    @Autowired
    private MedcaseMedicineMapper medcaseMedicineMapper;
    @Autowired
    private UserMedcaseMapper userMedcaseMapper;
    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private FileFeignClient fileFeignClient;


    @Override
    public Medcase getMedcaseById(Integer medcaseId) {
        Medcase medcase = medcaseMapper.selectById(medcaseId);
        AssertUtil.isNotNull(medcase, CommonErrorCode.MEDCASE_NOT_EXIST);

        UserMedcase userMedcase = userMedcaseMapper.selectOne(new MPJLambdaWrapper<UserMedcase>().eq(UserMedcase::getMedcaseId, medcaseId).eq(UserMedcase::getUserId, sessionUtil.getUserId()));
        if (userMedcase == null) {
            userMedcase = new UserMedcase();
            userMedcase.setMedcaseId(medcaseId);
            userMedcase.setUserId(sessionUtil.getUserId());
            userMedcase.setViewTime(LocalDateTime.now());
            userMedcaseMapper.insert(userMedcase);
        } else {
            userMedcase.setViewTime(LocalDateTime.now());
            userMedcaseMapper.updateById(userMedcase);
        }
        return initMedcase(medcase);
    }


    @Override
    public IPage<?> getMedcaseList(Integer pageSize, Integer pageNum, String infoKeyword, String nameKeyword, String diseaseName) {
        MPJLambdaWrapper<Medcase> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Medcase.class)
                .leftJoin(Disease.class, Disease::getDiseaseId, Medcase::getDiseaseId)
                .like(diseaseName != null, Disease::getName, diseaseName)
                .like(infoKeyword != null, Medcase::getInfoDescription, infoKeyword)
                .like(nameKeyword != null, Medcase::getName, nameKeyword);

        return medcaseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<?> getAllMedcaseList(String infoKeyword, String nameKeyword, String diseaseName) {
        MPJLambdaWrapper<Medcase> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Medcase.class)
                .leftJoin(Disease.class, Disease::getDiseaseId, Medcase::getDiseaseId)
                .or(validString(infoKeyword), disease -> disease.like(Disease::getName, infoKeyword))
                .or(validString(infoKeyword), name -> name.like(Medcase::getName, infoKeyword))
                .or(validString(infoKeyword), info -> info.like(Medcase::getInfoDescription, infoKeyword));

        return medcaseMapper.selectList(wrapper);
    }

    private boolean validString(String str) {
        return str != null && !str.isEmpty();
    }
    @Override
    @Transactional
    public Medcase add(Medcase medcase) {
        // 疾病存在
        AssertUtil.isNotNull(diseaseMapper.selectById(medcase.getDiseaseId()), CommonErrorCode.DISEASE_NOT_EXIST);

        // 手术存在
        AssertUtil.isNotNull(operationMapper.selectById(medcase.getOperationId()), CommonErrorCode.OPERATION_NOT_EXIST);
        // 根据病例名查病例
        LambdaQueryWrapper<Medcase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Medcase::getName, medcase.getName());
        Medcase oldMedcase = medcaseMapper.selectOne(wrapper);
        // 病例名不重复或者重复但是同一个病例
        AssertUtil.isTrue(oldMedcase == null || oldMedcase.getMedcaseId().equals(medcase.getMedcaseId()), CommonErrorCode.MEDCASE_NAME_EXIST);

        medcaseMapper.insert(medcase);

        // inspections
        // medicines
        updateInspectionsMedicines(medcase);


        return medcase;
    }

    @Override
    @Transactional
    public Medcase update(Medcase medcase) {
        Medcase checkMedcase = medcaseMapper.selectById(medcase.getMedcaseId());
        // 病例存在
        AssertUtil.isNotNull(checkMedcase, CommonErrorCode.MEDCASE_NOT_EXIST);
        // 疾病存在
        AssertUtil.isNotNull(diseaseMapper.selectById(medcase.getDiseaseId()), CommonErrorCode.DISEASE_NOT_EXIST);

        // 手术存在
        AssertUtil.isNotNull(operationMapper.selectById(medcase.getOperationId()), CommonErrorCode.OPERATION_NOT_EXIST);

        // 根据病例名查病例
        LambdaQueryWrapper<Medcase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Medcase::getName, medcase.getName());
        Medcase oldMedcase = medcaseMapper.selectOne(wrapper);
        // 病例名不重复或者重复但是同一个病例
        AssertUtil.isTrue(oldMedcase == null || oldMedcase.getMedcaseId().equals(medcase.getMedcaseId()), CommonErrorCode.MEDCASE_NAME_EXIST);
        // 更新多对多关系
        updateInspectionsMedicines(medcase);
        medcaseMapper.updateById(medcase);
        // 如果photo 或 video 有变化，则删除原来的文件
        if (checkMedcase.getInfoPhoto() != null && !checkMedcase.getInfoPhoto().equals(medcase.getInfoPhoto())) {
            fileFeignClient.delete(checkMedcase.getInfoPhoto());
        }
        if (checkMedcase.getInfoVideo() != null && !checkMedcase.getInfoVideo().equals(medcase.getInfoVideo())) {
            fileFeignClient.delete(checkMedcase.getInfoVideo());
        }
        return medcase;
    }

    @Override
    @Transactional
    public void delete(Integer medcaseId) {

        Medcase medcase = medcaseMapper.selectById(medcaseId);
        AssertUtil.isNotNull(medcase, CommonErrorCode.MEDCASE_NOT_EXIST);
        medcaseInspectionService.deleteByMedcaseId(medcaseId);
        medcaseMedicineService.deleteByMedcaseId(medcaseId);
        // 删除图片 和 视频
        fileFeignClient.delete(medcase.getInfoPhoto());
        fileFeignClient.delete(medcase.getInfoVideo());
        medcaseMapper.deleteById(medcaseId);
    }


    private Medcase initMedcase(Medcase medcase) {
        /**
         * disease
         */
        Disease disease = diseaseMapper.selectById(medcase.getDiseaseId());
        AssertUtil.isNotNull(disease, CommonErrorCode.DISEASE_NOT_EXIST);
        medcase.setDisease(disease);
        /**
         * operation
         */
        Operation operation = operationMapper.selectById(medcase.getOperationId());
        AssertUtil.isNotNull(operation, CommonErrorCode.OPERATION_NOT_EXIST);
        medcase.setOperation(operation);
        /**
         * inspections
         */
        List<MedcaseInspection> inspections = medcaseInspectionMapper.selectList(new MPJLambdaWrapper<MedcaseInspection>().eq(MedcaseInspection::getMedcaseId, medcase.getMedcaseId()));
        medcase.setInspections(inspections);

        /**
         * medicines
         */
        List<MedcaseMedicine> medicines = medcaseMedicineMapper.selectList(new MPJLambdaWrapper<MedcaseMedicine>().eq(MedcaseMedicine::getMedcaseId, medcase.getMedcaseId()));
        medcase.setMedicines(medicines);

        return medcase;
    }

    private void updateInspectionsMedicines(Medcase medcase) {
        // 删除旧的检查项目和药品
        medcaseInspectionService.deleteByMedcaseId(medcase.getMedcaseId());
        medcaseMedicineService.deleteByMedcaseId(medcase.getMedcaseId());
        // 查询Operation
        Operation operation = operationMapper.selectById(medcase.getOperationId());
        AssertUtil.isNotNull(operation, CommonErrorCode.OPERATION_NOT_EXIST);
        medcase.setOperation(operation);
        Double price = operation.getPrice();

        // 添加新的检查项目和药品
        if (medcase.getInspections() != null) {
            medcase.getInspections().forEach(medcaseInspection -> {
                // 检查项目存在
                AssertUtil.isNotNull(inspectionMapper.selectById(medcaseInspection.getInspectionId()), CommonErrorCode.INSPECTION_NOT_EXIST);
                medcaseInspection.setMedcaseId(medcase.getMedcaseId());
                medcaseInspectionService.add(medcaseInspection);
            });
        }
        if (medcase.getMedicines() != null) {
            List<MedcaseMedicine> medcaseMedicines = medcase.getMedicines();
            for (MedcaseMedicine medcaseMedicine : medcaseMedicines) {
                // 药品存在
                Medicine medicine = medicineMapper.selectById(medcaseMedicine.getMedicineId());
                AssertUtil.isNotNull(medicine, CommonErrorCode.MEDICINE_NOT_EXIST);

                medcaseMedicine.setMedcaseId(medcase.getMedcaseId());
                // 药品数量大于0
                AssertUtil.isTrue(medcaseMedicine.getNum() > 0, CommonErrorCode.MEDICINE_NUM_ERROR);
                // 更新price
                price += medicine.getPrice() * medcaseMedicine.getNum();
                medcaseMedicineService.add(medcaseMedicine);
            }
        }
        medcase.setPrice(price);
        medcaseMapper.updateById(medcase);
    }
}
