package me.zhengjie.uma_mes.service.handheld;

import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.domain.ScanRecord;
import me.zhengjie.uma_mes.domain.ScanRecordLabel;
import me.zhengjie.uma_mes.service.ChemicalFiberLabelService;
import me.zhengjie.uma_mes.service.ChemicalFiberProductionService;
import me.zhengjie.uma_mes.service.ScanRecordLabelService;
import me.zhengjie.uma_mes.service.ScanRecordService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.dto.handheld.LabelMsgDto;
import me.zhengjie.uma_mes.service.dto.handheld.UploadDataDto;
import me.zhengjie.uma_mes.vo.handheld.ChemicalFiberLabelInfoVo;
import me.zhengjie.uma_mes.vo.handheld.ChemicalFiberProductionInfoVo;
import me.zhengjie.uma_mes.vo.handheld.LabelMsgVo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Component
public class HandheldService {

    private final ChemicalFiberLabelService chemicalFiberLabelService;

    private final ChemicalFiberProductionService chemicalFiberProductionService;

    private final ScanRecordService scanRecordService;

    private final ScanRecordLabelService scanRecordLabelService;

    public HandheldService(
            ChemicalFiberLabelService chemicalFiberLabelService,
            ChemicalFiberProductionService chemicalFiberProductionService,
            ScanRecordService scanRecordService,
            ScanRecordLabelService scanRecordLabelService) {

        this.chemicalFiberLabelService = chemicalFiberLabelService;
        this.chemicalFiberProductionService = chemicalFiberProductionService;
        this.scanRecordService = scanRecordService;
        this.scanRecordLabelService = scanRecordLabelService;
    }

    public Result getLabelMsg(LabelMsgDto labelMsgDto) {
        ChemicalFiberLabelDTO chemicalFiberLabelDTO = getChemicalFiberLabelDTOByLabelNumber(labelMsgDto.getLabelNumber());
        if (chemicalFiberLabelDTO == null) {
            return Result.error(ResultEnum.DATA_NOT_EXISTS);
        }

        // 标签信息
        String checkLabelStatusStr = checkLabelStatus(chemicalFiberLabelDTO, labelMsgDto.getStatus());
        if (!"".equals(checkLabelStatusStr)) {
            return Result.error(ResultEnum.NOT_SCHEDULED_ERROR.getCode(), checkLabelStatusStr);
        }
        ChemicalFiberLabelInfoVo chemicalFiberLabelInfoVo = new ChemicalFiberLabelInfoVo();
        ObjectTransfer.transValue(chemicalFiberLabelDTO, chemicalFiberLabelInfoVo);

        // 生产单信息
        ChemicalFiberProductionDTO chemicalFiberProductionDTO = chemicalFiberProductionService.findById(chemicalFiberLabelDTO.getProductionId());
        ChemicalFiberProductionInfoVo chemicalFiberProductionInfoVo = new ChemicalFiberProductionInfoVo();
        ObjectTransfer.transValue(chemicalFiberProductionDTO, chemicalFiberProductionInfoVo);
        LabelMsgVo labelMsgVo = new LabelMsgVo(chemicalFiberLabelInfoVo, chemicalFiberProductionInfoVo);
        return Result.success(labelMsgVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result uploadData(UploadDataDto uploadDataDto) {
        // 需要修改的标签列表
        List<ChemicalFiberLabel> chemicalFiberLabels = new ArrayList<>();
        // 扫描单号
        String scanNumber = getScanNumber(uploadDataDto.getStatus());

        // 新增扫描记录
        ScanRecord scanRecord = new ScanRecord();
        scanRecord.setScanNumber(scanNumber);
        scanRecord.setScanUser(uploadDataDto.getScanUser());
        scanRecord.setScanTime(new Timestamp(System.currentTimeMillis()));
        scanRecord.setType(getTypeStr(uploadDataDto.getStatus()));
        scanRecordService.create(scanRecord);

        List<ScanRecordLabel> scanRecordLabels = new ArrayList<>();
        for (Map map : uploadDataDto.getLabelList()) {
            String labelNumber = map.get("labelNumber").toString();
            String scanTime = map.get("scanTime").toString();
            ChemicalFiberLabelDTO chemicalFiberLabelDTO = getChemicalFiberLabelDTOByLabelNumber(labelNumber);
            if (chemicalFiberLabelDTO == null) {
                return Result.error(ResultEnum.DATA_NOT_EXISTS);
            }

            String checkLabelStatusStr = checkLabelStatus(chemicalFiberLabelDTO, uploadDataDto.getStatus());
            if (!"".equals(checkLabelStatusStr)) {
                return Result.error(ResultEnum.NOT_SCHEDULED_ERROR.getCode(), checkLabelStatusStr);
            }

            ChemicalFiberLabelDTO newChemicalFiberLabelDTO = getNewChemicalFiberLabelDTO(chemicalFiberLabelDTO, uploadDataDto.getStatus());
            ChemicalFiberLabel chemicalFiberLabel = new ChemicalFiberLabel();
            ObjectTransfer.transValue(newChemicalFiberLabelDTO, chemicalFiberLabel);
            chemicalFiberLabels.add(chemicalFiberLabel);

            Timestamp tempTimestamp = new Timestamp(Long.parseLong(scanTime));
            ScanRecordLabel scanRecordLabel = new ScanRecordLabel();
            scanRecordLabel.setLabelId(chemicalFiberLabel.getId());
            scanRecordLabel.setScanRecordId(scanRecord.getId());
            scanRecordLabel.setScanTime(tempTimestamp);
            scanRecordLabels.add(scanRecordLabel);
        }

        // 修改标签
        chemicalFiberLabelService.update(chemicalFiberLabels);

        // 新增标签扫描记录
        scanRecordLabelService.create(scanRecordLabels);

        return Result.success("上传成功");
    }

    /**
     *
     * @param chemicalFiberLabelDTO
     * @param status 入库：RK 出库：SH 退库：TK 退货：TH
     *               便签状态 0：待入库 1：入库 2：出库 3：作废 4：退库 5：退货
     * @return
     */
    private ChemicalFiberLabelDTO getNewChemicalFiberLabelDTO(ChemicalFiberLabelDTO chemicalFiberLabelDTO, Integer status) {
        switch (status) {
            case 1:
                chemicalFiberLabelDTO.setStatus(1);
                break;
            case 2:
                chemicalFiberLabelDTO.setStatus(2);
                break;
            case 4:
                chemicalFiberLabelDTO.setStatus(4);
                break;
            default:
                chemicalFiberLabelDTO.setStatus(5);
        }
        return chemicalFiberLabelDTO;
    }

    /**
     *
     * @param status 入库：RK 出库：SH 退库：TK 退货：TH
     * @return
     */
    private String getScanNumber (Integer status) {
        String scanNumber;
        String type = getTypeStr(status);
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        ScanRecordQueryCriteria scanRecordQueryCriteria = new ScanRecordQueryCriteria();
        scanRecordQueryCriteria.setStartTime(new Timestamp(Long.parseLong(timeMap.get("time").toString())));
        scanRecordQueryCriteria.setEndTime(new Timestamp(System.currentTimeMillis()));
        scanRecordQueryCriteria.setType(type);
        List<ScanRecordDTO> scanRecordDTOS = scanRecordService.queryAll(scanRecordQueryCriteria);

        if (scanRecordDTOS.size() == 0) {
            scanNumber = type + year + month + "001";
        } else {
            ScanRecordDTO scanRecordDTO = scanRecordDTOS.get(scanRecordDTOS.size() - 1);
            scanRecordDTO.getScanNumber().length();
            String tempScanNumber = scanRecordDTO.getScanNumber().substring(8);
            Integer number = Integer.parseInt(tempScanNumber) + 1;
            String tempNumberStr = String.format("%3d", number++).replace(" ", "0");
            scanNumber = type + year + month + tempNumberStr;
        }
        return scanNumber;
    }

    private String getTypeStr(Integer status) {
        String type;
        switch (status) {
            case 1:
                type = "RK";
                break;
            case 2:
                type = "SH";
                break;
            case 4:
                type = "TK";
                break;
            default:
                type = "TH";
        }
        return type;
    }

    /**
     * 根据标签号获取对象
     * @param labelNumber
     * @return
     */
    private ChemicalFiberLabelDTO getChemicalFiberLabelDTOByLabelNumber(String labelNumber) {
        ChemicalFiberLabelQueryCriteria chemicalFiberLabelQueryCriteria = new ChemicalFiberLabelQueryCriteria();
        chemicalFiberLabelQueryCriteria.setLabelNumber(labelNumber);
        List<ChemicalFiberLabelDTO> chemicalFiberLabelDTOList = chemicalFiberLabelService.queryAll(chemicalFiberLabelQueryCriteria);
        if (chemicalFiberLabelDTOList.size() == 0) {
            return null;
        }
        return chemicalFiberLabelDTOList.get(0);
    }

    /**
     * 检查标签当前状态与手持机提交状态
     * @param chemicalFiberLabelDTO
     * @param status 0：待入仓 1：入仓 2：出仓 3：作废 4：退仓 5：退货
     * @return
     */
    private String checkLabelStatus(ChemicalFiberLabelDTO chemicalFiberLabelDTO, Integer status) {
        String checkLabelStatusStr;
        switch (chemicalFiberLabelDTO.getStatus()) {
            case 0:
                checkLabelStatusStr = status != 1 ? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【待入仓】，请先入仓" : "";
                break;
            case 1:
                checkLabelStatusStr = status != 2 ? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已入仓】，请出仓" : "";
                break;
            case 2:
                checkLabelStatusStr = status != 4 && status != 5 ? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已出仓】，请返仓或退货" : "";
                break;
            case 3:
                checkLabelStatusStr =  chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已作废】";
                break;
            case 4:
                checkLabelStatusStr = status != 2 ? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已返仓】，请出仓" : "";
                break;
            default:
                checkLabelStatusStr = status != 1 && status != 3 ? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已退货】，请入仓或作废" : "";
        }
        return checkLabelStatusStr;
    }

    /**
     * 获取当月0时时间戳
     * @return
     */
    private Map monthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("month", month);
        map.put("year", year);
        return map;
    }
}
