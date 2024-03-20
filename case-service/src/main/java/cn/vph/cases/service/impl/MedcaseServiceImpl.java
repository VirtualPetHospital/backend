package cn.vph.cases.service.impl;

import cn.vph.cases.entity.Medcase;
import cn.vph.cases.mapper.*;
import cn.vph.cases.service.MedcaseService;
import cn.vph.common.CommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.vph.common.util.AssertUtil;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 13:10
 **/
@Service
public class MedcaseServiceImpl implements MedcaseService {
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

    @Override
    public Object add(Medcase medcase) {
        // 疾病存在
        AssertUtil.isNotNull(diseaseMapper.selectById(medcase.getDiseaseId()), CommonErrorCode.DISEASE_NOT_EXIST);
        // 手术存在
        AssertUtil.isNotNull(operationMapper.selectById(medcase.getOperationId()), CommonErrorCode.OPERATION_NOT_EXIST);
        // inspections中的检查项目存在
        medcase.getInspections().forEach(inspection -> {
            AssertUtil.isNotNull(inspectionMapper.selectById(inspection.getId()), CommonErrorCode.INSPECTION_NOT_EXIST);
        });
        // medicines中的药品存在
        medcase.getMedicines().forEach(medicine -> {
            AssertUtil.isNotNull(medicineMapper.selectById(medicine.getId()), CommonErrorCode.MEDICINE_NOT_EXIST);
        });
        return null;

    }
}
