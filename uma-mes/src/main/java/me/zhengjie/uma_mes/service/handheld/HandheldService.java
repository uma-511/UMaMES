package me.zhengjie.uma_mes.service.handheld;

import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.*;
import me.zhengjie.uma_mes.repository.*;
import me.zhengjie.uma_mes.service.*;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.dto.handheld.LabelMsgDto;
import me.zhengjie.uma_mes.service.dto.handheld.UploadDataDto;
import me.zhengjie.uma_mes.vo.handheld.ChemicalFiberLabelInfoVo;
import me.zhengjie.uma_mes.vo.handheld.ChemicalFiberProductionInfoVo;
import me.zhengjie.uma_mes.vo.handheld.LabelMsgVo;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Component
public class HandheldService {

    private final ChemicalFiberLabelService chemicalFiberLabelService;

    private final ChemicalFiberProductionService chemicalFiberProductionService;

    private final ScanRecordService scanRecordService;

    @Autowired
    private  ScanRecordRepository scanRecordRepository;

    private final ScanRecordLabelService scanRecordLabelService;

    private final ConfigClassifyService configClassifyService;

    private final ConfigService configService;

    private final ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService;

    private final ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService;

    @Autowired
    private ChemicalFiberStockRepository chemicalFiberStockRepository;

    @Autowired
    private ChemicalFiberPalletRepository chemicalFiberPalletRepository;

    @Autowired
    private ChemicalFiberPalletService chemicalFiberPalletService;

    @Autowired
    private ChemicalFiberPalletDetailService chemicalFiberPalletDetailService;

    @Autowired
    private ChemicalFiberLabelRepository chemicalFiberLabelRepository;

    @Autowired
    private ChemicalFiberProductionRepository chemicalFiberProductionRepository;

    @Autowired
    private ChemicalFiberProductionReportService productionReportService;

    @Autowired
    private ViewScanRecordService viewScanRecordService;

    @Autowired
    private ErrorTextRepository errorTextRepository;

    public HandheldService(
            ChemicalFiberLabelService chemicalFiberLabelService,
            ChemicalFiberProductionService chemicalFiberProductionService,
            ScanRecordService scanRecordService,
            ScanRecordLabelService scanRecordLabelService,
            ConfigClassifyService configClassifyService,
            ConfigService configService,
            ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService,
            ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService) {

        this.chemicalFiberLabelService = chemicalFiberLabelService;
        this.chemicalFiberProductionService = chemicalFiberProductionService;
        this.scanRecordService = scanRecordService;
        this.scanRecordLabelService = scanRecordLabelService;
        this.configClassifyService = configClassifyService;
        this.configService = configService;
        this.chemicalFiberDeliveryNoteService = chemicalFiberDeliveryNoteService;
        this.chemicalFiberDeliveryDetailService = chemicalFiberDeliveryDetailService;
    }

    public Result getLabelMsg(LabelMsgDto labelMsgDto) {
        List<LabelMsgVo> list = new ArrayList<>();
        List<ChemicalFiberLabelDTO> chemicalFiberLabelList = new ArrayList<>();
        char pallet = labelMsgDto.getLabelNumber().charAt(0);
        // 区分扫描托板还是扫描标签
        if (pallet == '2') {
            chemicalFiberLabelList = getPalletNumber(labelMsgDto.getLabelNumber());
        } else {
            chemicalFiberLabelList = getLabelNumbers(labelMsgDto.getLabelNumber());
        }
        if (chemicalFiberLabelList == null) {
            return Result.serverError("此标签为信息为空");
        }

        for (ChemicalFiberLabelDTO chemicalFiberLabelDTO : chemicalFiberLabelList) {
            if (chemicalFiberLabelDTO == null) {
                return Result.error(ResultEnum.DATA_NOT_EXISTS);
            }

            if (labelMsgDto.getIsCheckLabel()) {
                // 添加时判断各个标签的状态
                if (labelMsgDto.getStatus() != 6) {
                    // 标签信息
                    String checkLabelStatusStr = checkLabelStatus(chemicalFiberLabelDTO, labelMsgDto.getStatus());
                    if (!"".equals(checkLabelStatusStr)) {
                        return Result.error(ResultEnum.NOT_SCHEDULED_ERROR.getCode(), checkLabelStatusStr);
                    }
                }
                if (labelMsgDto.getStatus() == 10) {
                    List<ChemicalFiberPallet> palletList = chemicalFiberPalletService.getPallet(labelMsgDto.getScanNumber());
                    if (palletList.size() <= 0) {
                        return Result.serverError("[" + labelMsgDto.getScanNumber() +"]托板单号不存在");
                    }
                    ChemicalFiberLabelQueryCriteria label = new ChemicalFiberLabelQueryCriteria();
                    label.setLabelNumber(labelMsgDto.getLabelNumber());
                    List<ChemicalFiberLabelDTO> labelList = chemicalFiberLabelService.queryAll(label);
                    if (!palletList.get(0).getProdFineness().equals(labelList.get(0).getFineness()) || !palletList.get(0).getProdColor().equals(labelList.get(0).getColor())) {
                        return Result.serverError("[" + labelMsgDto.getLabelNumber() +"]数据不统一");
                    }
                    if (labelList.get(0).getStatus() == 2) {
                        return Result.serverError("[" + labelMsgDto.getLabelNumber() +"]标签已出库");
                    }
                    if (labelList.get(0).getStatus() == 1) {
                        return Result.serverError("[" + labelMsgDto.getLabelNumber() +"]标签已入库");
                    }
                    if (labelList.get(0).getStatus() == 3) {
                        return Result.serverError("[" + labelMsgDto.getLabelNumber() +"]标签已作废");
                    }
                    if (labelList.get(0).getStatus() == 9) {
                        return Result.serverError("[" + labelMsgDto.getLabelNumber() +"]标签已托板入库");
                    }
                    if (labelList.get(0).getStatus() == 5) {
                        return Result.serverError("[" + labelMsgDto.getLabelNumber() +"]标签已退货");
                    }
                }
            } else {
                if (labelMsgDto.getStatus() == 7) {
                    // 减少时判断这个标签是否存在和单号是否存在
                    ScanRecordQueryCriteria scanRecordQueryCriteria = new ScanRecordQueryCriteria();
                    scanRecordQueryCriteria.setAccurateScanNumber(labelMsgDto.getScanNumber());
                    List<ScanRecordDTO> scanRecordDTOS = scanRecordService.queryAll(scanRecordQueryCriteria);
                    if (scanRecordDTOS.size() <= 0) {
                        return Result.serverError("[" + labelMsgDto.getScanNumber() +"]出库单号不存在");
                    }
                    ScanRecordLabelQueryCriteria scanRecordLabelQueryCriteria = new ScanRecordLabelQueryCriteria();
                    scanRecordLabelQueryCriteria.setScanRecordId(scanRecordDTOS.get(0).getId());
                    scanRecordLabelQueryCriteria.setLabelId(chemicalFiberLabelDTO.getId());
                    List<ScanRecordLabelDTO> scanRecordLabelDTOS = scanRecordLabelService.queryAll(scanRecordLabelQueryCriteria);
                    if (scanRecordLabelDTOS.size() <= 0) {
                        return Result.serverError("此标签不存在于 [" + labelMsgDto.getScanNumber() + "] 出库号内");
                    }
                }
                if (labelMsgDto.getStatus() == 10) {
                    List<ChemicalFiberPallet> palletList = chemicalFiberPalletService.getPallet(labelMsgDto.getScanNumber());
                    if (palletList.size() <= 0) {
                        return Result.serverError("[" + labelMsgDto.getScanNumber() +"]托板单号不存在");
                    }
                    ChemicalFiberLabelQueryCriteria label = new ChemicalFiberLabelQueryCriteria();
                    label.setPalletId(labelMsgDto.getScanNumber());
                    label.setLabelNumber(labelMsgDto.getLabelNumber());
                    List<ChemicalFiberLabelDTO> labelList = chemicalFiberLabelService.queryAll(label);
                    if (labelList.size() <= 0) {
                        return Result.serverError("此标签不存在于 [" + labelMsgDto.getScanNumber() + "] 出库号内");
                    }
                    if (!palletList.get(0).getProdFineness().equals(labelList.get(0).getFineness()) && !palletList.get(0).getProdColor().equals(labelList.get(0).getColor())) {
                        return Result.serverError("[" + chemicalFiberLabelDTO.getLabelNumber() +"]数据不统一");
                    }
                }

            }
            if (labelMsgDto.getStatus() == 7) {
                // 出仓添加时判断这个标签是否已经在单号内
                if (!StringUtils.isEmpty(labelMsgDto.getScanNumber()) && labelMsgDto.getIsAdd()) {
                    ScanRecordQueryCriteria scanRecordQueryCriteria = new ScanRecordQueryCriteria();
                    scanRecordQueryCriteria.setScanNumber(labelMsgDto.getScanNumber());
                    List<ScanRecordDTO> scanRecordDTOS = scanRecordService.queryAll(scanRecordQueryCriteria);
                    ScanRecordDTO scanRecordDTO = scanRecordDTOS.get(0);

                    ScanRecordLabelQueryCriteria scanRecordLabelQueryCriteria = new ScanRecordLabelQueryCriteria();
                    scanRecordLabelQueryCriteria.setScanRecordId(scanRecordDTO.getId());
                    scanRecordLabelQueryCriteria.setLabelId(chemicalFiberLabelDTO.getId());
                    List<ScanRecordLabelDTO> scanRecordLabelDTOS = scanRecordLabelService.queryAll(scanRecordLabelQueryCriteria);
                    if (scanRecordLabelDTOS.size() > 0) {
                        return Result.serverError("此标签已存在 [" + labelMsgDto.getScanNumber() + "] 出库号内");
                    }
                }
            }

            if (labelMsgDto.getStatus() == 10) {
                if (!StringUtils.isEmpty(labelMsgDto.getScanNumber()) && labelMsgDto.getIsAdd()) {
                    ChemicalFiberLabelQueryCriteria label = new ChemicalFiberLabelQueryCriteria();
                    label.setPalletId(labelMsgDto.getScanNumber());
                    label.setLabelNumber(labelMsgDto.getLabelNumber());
                    List<ChemicalFiberLabelDTO> labelList = chemicalFiberLabelService.queryAll(label);
                    if (labelList.size() > 0) {
                        return Result.serverError("此标签已存在 [" + labelMsgDto.getScanNumber() + "] 出库号内");
                    }
                }
            }


            ChemicalFiberLabelInfoVo chemicalFiberLabelInfoVo = new ChemicalFiberLabelInfoVo();
            ObjectTransfer.transValue(chemicalFiberLabelDTO, chemicalFiberLabelInfoVo);

            // 生产单信息
            ChemicalFiberProductionDTO chemicalFiberProductionDTO = chemicalFiberProductionService.findById(chemicalFiberLabelDTO.getProductionId());
            ChemicalFiberProductionInfoVo chemicalFiberProductionInfoVo = new ChemicalFiberProductionInfoVo();
            //ChemicalFiberHandheld chemicalFiberHandheld = new ChemicalFiberHandheld();
            //ObjectTransfer.transValue(chemicalFiberLabelInfoVo, chemicalFiberHandheld);
            //ObjectTransfer.transValue(chemicalFiberProductionDTO, chemicalFiberHandheld);

            ObjectTransfer.transValue(chemicalFiberProductionDTO, chemicalFiberProductionInfoVo);
            LabelMsgVo labelMsgVo = new LabelMsgVo(chemicalFiberLabelInfoVo, chemicalFiberProductionInfoVo);
            list.add(labelMsgVo);
        }
        return Result.success(list);

    }

    @Transactional(rollbackFor = Exception.class)
    public Result uploadData(UploadDataDto uploadDataDto) {
        // 需要修改的标签列表
        List<ChemicalFiberLabel> chemicalFiberLabels = new ArrayList<>();
        List<ChemicalFiberPalletDetail> chemicalFiberPalletDetails = new ArrayList<>();
        List<ChemicalFiberStock> stockList = chemicalFiberStockRepository.findAll();
        BigDecimal sumWeight = new BigDecimal(0);
        BigDecimal sumTare = new BigDecimal(0);
        BigDecimal sumGrossWeight = new BigDecimal(0);
        Integer sumNumber = 0;
        Integer sumBag = 0;
        ScanRecord scanRecord;
        /*String modelAndName = "";*/
        ViewScanRecord viewScanRecord = new ViewScanRecord();
        String scanNumber = "";
        if (uploadDataDto.getStatus() != 7 && uploadDataDto.getStatus() != 10) {
            // 扫描单号
            scanNumber = getScanNumber(uploadDataDto.getStatus());

            // 新增扫描记录
            scanRecord = new ScanRecord();
            scanRecord.setScanNumber(scanNumber);
            scanRecord.setScanUser(uploadDataDto.getScanUser());
            scanRecord.setScanTime(new Timestamp(System.currentTimeMillis()));
            scanRecord.setType(getTypeStr(uploadDataDto.getStatus()));
            scanRecordService.create(scanRecord);

            String scan = scanNumber.toUpperCase();
            viewScanRecord.setScanNumber(scan);
            viewScanRecord.setScanTime(new Timestamp(System.currentTimeMillis()));
            viewScanRecord.setType(getTypeStr(uploadDataDto.getStatus()));
        } else {
            scanRecord = new ScanRecord();
            if (uploadDataDto.getStatus() == 7) {
                ScanRecordQueryCriteria scanRecordQueryCriteria = new ScanRecordQueryCriteria();
                scanRecordQueryCriteria.setAccurateScanNumber(uploadDataDto.getScanNumber());
                List<ScanRecordDTO> scanRecordDTOS = scanRecordService.queryAll(scanRecordQueryCriteria);
                if (scanRecordDTOS.size() <= 0) {
                    return Result.serverError("[" + uploadDataDto.getScanNumber() +"]出库单号不存在");
                }
                ScanRecordDTO scanRecordDTO = scanRecordDTOS.get(0);
                ScanRecord tempScanRecord = new ScanRecord();
                ObjectTransfer.transValue(scanRecordDTO, tempScanRecord);
                scanRecord = tempScanRecord;
                scanNumber = uploadDataDto.getScanNumber();
            }

            if (uploadDataDto.getStatus() == 10) {
                List<ChemicalFiberPallet> palletList = chemicalFiberPalletService.getPallet(uploadDataDto.getScanNumber());
                if (palletList.size() <= 0) {
                    return Result.serverError("[" + uploadDataDto.getScanNumber() +"]托板单号不存在");
                }
                scanNumber = getScanNumber(uploadDataDto.getStatus());
                scanRecord.setScanNumber(scanNumber);
                scanRecord.setScanUser(uploadDataDto.getScanUser());
                scanRecord.setScanTime(new Timestamp(System.currentTimeMillis()));
                scanRecord.setType(getTypeStr(uploadDataDto.getStatus()));
                scanRecordService.create(scanRecord);
            }

            String scan = scanNumber.toUpperCase();
            viewScanRecord.setScanNumber(scan);
            viewScanRecord.setScanTime(new Timestamp(System.currentTimeMillis()));
            viewScanRecord.setType(getTypeStr(uploadDataDto.getStatus()));
        }

        List<ScanRecordLabel> scanRecordLabels = new ArrayList<>();
        for (Map map : uploadDataDto.getLabelList()) {
            String labelNumber = map.get("labelNumber").toString();
            String scanTime = map.get("scanTime").toString();
            ChemicalFiberLabelDTO chemicalFiberLabelDTO = getChemicalFiberLabelDTOByLabelNumber(labelNumber);
            if (chemicalFiberLabelDTO == null && uploadDataDto.getStatus() != 7) {
                scanRecordService.delete(scanRecord.getId());
                return Result.error(ResultEnum.DATA_NOT_EXISTS);
            }

            if (uploadDataDto.getIsCheckLabel()) {
                if (uploadDataDto.getStatus() != 6) {
                    // 标签信息
                    String checkLabelStatusStr = checkLabelStatus(chemicalFiberLabelDTO, uploadDataDto.getStatus());
                    if (!"".equals(checkLabelStatusStr)) {
                        scanRecordService.delete(scanRecord.getId());
                        return Result.error(ResultEnum.NOT_SCHEDULED_ERROR.getCode(), checkLabelStatusStr);
                    }
                }
            }

            ChemicalFiberLabelDTO newChemicalFiberLabelDTO = getNewChemicalFiberLabelDTO(chemicalFiberLabelDTO, uploadDataDto.getStatus(), uploadDataDto.getIsAdd(), uploadDataDto.getScanNumber());
            ChemicalFiberLabel chemicalFiberLabel = new ChemicalFiberLabel();
            ObjectTransfer.transValue(newChemicalFiberLabelDTO, chemicalFiberLabel);
            chemicalFiberLabels.add(chemicalFiberLabel);

            /*if (modelAndName.equals("")) {
                modelAndName = newChemicalFiberLabelDTO.getColor() + "-" + newChemicalFiberLabelDTO.getFineness();
            } else {
                if (uploadDataDto.getStatus() == 9) {
                   String model = newChemicalFiberLabelDTO.getColor() + "-" + newChemicalFiberLabelDTO.getFineness();
                   if (!modelAndName.equals(model)) {
                       //throw new BadRequestException("请统一数据");
                       TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                       return Result.serverError("请统一数据");
                   }
                }
            }*/

            ChemicalFiberPalletDetail chemicalFiberPalletDetail = new ChemicalFiberPalletDetail();
            ObjectTransfer.transValue(newChemicalFiberLabelDTO, chemicalFiberPalletDetail);
            chemicalFiberPalletDetail.setId(null);
            chemicalFiberPalletDetails.add(chemicalFiberPalletDetail);

            ChemicalFiberProduction production = chemicalFiberProductionRepository.findById(newChemicalFiberLabelDTO.getProductionId()).orElseGet(ChemicalFiberProduction::new);
            ObjectTransfer.transValue(production, viewScanRecord);
            ObjectTransfer.transValue(newChemicalFiberLabelDTO, viewScanRecord);
            viewScanRecord.setId(null);
            if (uploadDataDto.getStatus() == 10) {

                if (uploadDataDto.getIsAdd()) {
                    viewScanRecord.setType("TZ+");
                } else {
                    viewScanRecord.setType("TZ-");
                }
            } else if (uploadDataDto.getStatus() == 7){
                if (uploadDataDto.getIsAdd()) {
                    viewScanRecord.setType("SH+");
                } else {
                    viewScanRecord.setType("RK-");
                }
            }
            viewScanRecordService.create(viewScanRecord);

            // 计算所有的数量和重量
            sumWeight = sumWeight.add(newChemicalFiberLabelDTO.getNetWeight());
            sumTare = sumTare.add(newChemicalFiberLabelDTO.getTare());
            sumGrossWeight = sumGrossWeight.add(newChemicalFiberLabelDTO.getGrossWeight());
            sumNumber = sumNumber.intValue() + newChemicalFiberLabelDTO.getFactPerBagNumber();
            sumBag += 1;


            if (uploadDataDto.getIsAdd()) {
                Timestamp tempTimestamp = new Timestamp(Long.parseLong(scanTime));
                ScanRecordLabel scanRecordLabel = new ScanRecordLabel();
                scanRecordLabel.setLabelId(chemicalFiberLabel.getId());
                scanRecordLabel.setScanRecordId(scanRecord.getId());
                scanRecordLabel.setScanTime(tempTimestamp);
                scanRecordLabels.add(scanRecordLabel);
            } else {
                if (!uploadDataDto.getIsAdd() && uploadDataDto.getStatus() == 7) {
                    ScanRecordLabelQueryCriteria scanRecordLabelQueryCriteria = new ScanRecordLabelQueryCriteria();
                    scanRecordLabelQueryCriteria.setScanRecordId(scanRecord.getId());
                    scanRecordLabelQueryCriteria.setLabelId(chemicalFiberLabelDTO.getId());
                    List<ScanRecordLabelDTO> scanRecordLabelDTOS = scanRecordLabelService.queryAll(scanRecordLabelQueryCriteria);
                    if (scanRecordLabelDTOS.size() <= 0) {
                        return Result.serverError("此标签不存在于 [" + uploadDataDto.getScanNumber() + "] 出库号内");
                    }
                    for(ScanRecordLabelDTO scanRecordLabelDTO : scanRecordLabelDTOS) {
                        scanRecordLabelService.delete(scanRecordLabelDTO.getId());
                    }
                }
                if (!uploadDataDto.getIsAdd() && uploadDataDto.getStatus() == 10) {
                    ChemicalFiberLabelQueryCriteria label = new ChemicalFiberLabelQueryCriteria();
                    label.setPalletId(uploadDataDto.getScanNumber());
                    label.setLabelNumber(labelNumber);
                    List<ChemicalFiberLabelDTO> labelList = chemicalFiberLabelService.queryAll(label);
                    if (labelList.size() <= 0) {
                        return Result.serverError("此标签不存在于 [" + uploadDataDto.getScanNumber() + "] 出库号内");
                    }
                    /*List<ChemicalFiberLabel> laberUpdateList = new ArrayList<>();
                    for (ChemicalFiberLabelDTO dto : labelList) {
                        ChemicalFiberLabel laberUpdate = new ChemicalFiberLabel();
                        dto.setPalletId(null);
                        dto.setStatus(0);
                        ObjectTransfer.transValue(dto, laberUpdate);
                        laberUpdateList.add(laberUpdate);
                    }
                    chemicalFiberLabelRepository.saveAll(laberUpdateList);*/
                    chemicalFiberPalletDetailService.delectDetail(uploadDataDto.getScanNumber(), labelNumber);
                }

            }


        }

        if (uploadDataDto.getStatus() != 6) {
            // 修改标签
            if (uploadDataDto.getStatus() != 7 || uploadDataDto.getStatus() != 10) {
                // 修改托板标签记录
                chemicalFiberPalletDetailService.update(chemicalFiberLabels);
            }
            // 修改标签记录
            chemicalFiberLabelService.update(chemicalFiberLabels);
        }

        if (uploadDataDto.getIsAdd()) {
            // 新增标签扫描记录
            scanRecordLabelService.create(scanRecordLabels);
        }

        if (uploadDataDto.getStatus() == 1) {
            // 新增库存
            saveProdctionReport(chemicalFiberLabels);
            saveStock(chemicalFiberLabels);
        }

        if (uploadDataDto.getStatus() == 10) {
            if (!uploadDataDto.getIsAdd()) {
                updatePallet(chemicalFiberLabels);
            } else {
                List<ChemicalFiberPalletDetail> palletList = chemicalFiberPalletDetailService.createPallet(chemicalFiberPalletDetails, uploadDataDto.getScanNumber());
                /*List<ChemicalFiberPallet> PalletList = chemicalFiberPalletRepository.findAll();
                for (ChemicalFiberPalletDetail dto : palletList) {
                    if (dto.getPalletId() != null) {
                        ChemicalFiberPallet pallet = isPalletId(dto.getPalletId(), PalletList);
                        BigDecimal netWeight = pallet.getNetWeight();
                        BigDecimal tare = pallet.getTare();
                        BigDecimal grossWeight = pallet.getGrossWeight();
                        Integer factPerBagNumber = pallet.getTotalNumber();
                        Integer totalBag = pallet.getTotalBag();

                        netWeight = netWeight.add(dto.getNetWeight());
                        tare = tare.add(dto.getTare());
                        grossWeight = grossWeight.add(dto.getGrossWeight());
                        factPerBagNumber = factPerBagNumber + dto.getFactPerBagNumber();
                        totalBag = totalBag + 1;

                        pallet.setNetWeight(netWeight);
                        pallet.setTare(tare);
                        pallet.setGrossWeight(grossWeight);
                        pallet.setTotalNumber(factPerBagNumber);
                        pallet.setTotalBag(totalBag);


                        pallet.setCreateDate(new Timestamp(System.currentTimeMillis()));
                        chemicalFiberPalletRepository.save(pallet);*/
                    /*}*/

                /*}*/
            }
        }

        if (uploadDataDto.getStatus() == 9) {
            ChemicalFiberPallet Pallet = new ChemicalFiberPallet();
            List<ChemicalFiberLabel> labelList = new ArrayList<>();
            Pallet.setPalletNumber(getPalletScanNumber());
            Pallet.setProdModel(chemicalFiberLabels.get(0).getColor() + "-" + chemicalFiberLabels.get(0).getFineness());
            Pallet.setProdName(chemicalFiberLabels.get(0).getColor() + "-" + chemicalFiberLabels.get(0).getFineness());
            Pallet.setProdColor(chemicalFiberLabels.get(0).getColor());
            Pallet.setProdFineness(chemicalFiberLabels.get(0).getFineness());
            Pallet.setNetWeight(sumWeight);
            Pallet.setPacker(chemicalFiberLabels.get(0).getPacker());
            Pallet.setPrintStatus(0);
            Pallet.setTare(sumTare);
            Pallet.setGrossWeight(sumGrossWeight);
            Pallet.setTotalNumber(sumNumber);
            Pallet.setTotalBag(sumBag);
            Pallet.setCreateDate(new Timestamp(System.currentTimeMillis()));
            Pallet.setWarehousingNumber(0);
            Pallet.setWarehousingBag(0);
            Pallet = chemicalFiberPalletRepository.save(Pallet);
            for (ChemicalFiberLabel dto : chemicalFiberLabels) {
                dto.setPalletId(Pallet.getPalletNumber());
                labelList.add(dto);
            }
            chemicalFiberLabelRepository.saveAll(labelList);
            chemicalFiberPalletDetailService.create(chemicalFiberPalletDetails, Pallet.getPalletNumber());
            saveProdctionReport(chemicalFiberLabels);
            saveStock(chemicalFiberLabels);
        }

        if (uploadDataDto.getStatus() == 2) {
            ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = new ChemicalFiberDeliveryNote();
            String scan = scanNumber.toUpperCase();
            chemicalFiberDeliveryNote.setScanNumber(scan);
            chemicalFiberDeliveryNote.setTotalCost(new BigDecimal(0));
            chemicalFiberDeliveryNote.setTotalPrice(new BigDecimal(0));
            chemicalFiberDeliveryNote.setCreateDate(new Timestamp(System.currentTimeMillis()));
            chemicalFiberDeliveryNoteService.create(chemicalFiberDeliveryNote);
            chemicalFiberDeliveryNoteService.deliveryNoteStoredProcedure(scan);
            // 更新托板记录
            updatePallet(chemicalFiberLabels);
        }

        if (uploadDataDto.getStatus() == 7) {
            ChemicalFiberDeliveryDetailQueryCriteria chemicalFiberDeliveryDetailQueryCriteria = new ChemicalFiberDeliveryDetailQueryCriteria();
            List<String> scanNumbers = new ArrayList<>();
            String scan = scanNumber.toUpperCase();
            scanNumbers.add(scan);
            chemicalFiberDeliveryDetailQueryCriteria.setScanNumbers(scanNumbers);
            List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailDTOS = chemicalFiberDeliveryDetailService.queryAll(chemicalFiberDeliveryDetailQueryCriteria);
            for(ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
                chemicalFiberDeliveryDetailService.delete(chemicalFiberDeliveryDetailDTO.getId());
            }
            // 更新送货单
            chemicalFiberDeliveryNoteService.deliveryNoteStoredProcedure(scan);
        }

        return Result.success("上传成功");
    }

    public Result getConfigs() {
        ConfigClassifyQueryCriteria configClassifyQueryCriteria = new ConfigClassifyQueryCriteria();
        configClassifyQueryCriteria.setAlias("PDA_Summary");
        List<ConfigClassifyDTO> configClassifyDTOS = configClassifyService.queryAll(configClassifyQueryCriteria);
        ConfigQueryCriteria configQueryCriteria = new ConfigQueryCriteria();
        configQueryCriteria.setClassifyId(configClassifyDTOS.get(0).getId());
        List<ConfigDTO> configDTOS = configService.queryAll(configQueryCriteria);
        return Result.success(configDTOS);
    }

    /**
     *
     * @param chemicalFiberLabelDTO
     * @param status 入库：RK 出库：SH 退库：TK 退货：TH 盘点：PD 托板入库：TB
     *               便签状态 0：待入库 1：入库 2：出库 3：作废 4：退库 5：退货 6：盘点 9：托板入库
     * @return
     */
    private ChemicalFiberLabelDTO getNewChemicalFiberLabelDTO(ChemicalFiberLabelDTO chemicalFiberLabelDTO, Integer status, boolean isAdd, String palletId) {
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
            case 5:
                chemicalFiberLabelDTO.setStatus(5);
                break;
            case 9:
                chemicalFiberLabelDTO.setStatus(9);
                break;
            case 10:
                if (isAdd) {
                    chemicalFiberLabelDTO.setStatus(11);
                    chemicalFiberLabelDTO.setPalletId(palletId);
                } else {

                    chemicalFiberLabelDTO.setStatus(10);
                }
                break;
            case 7:
                if (isAdd) {
                    chemicalFiberLabelDTO.setStatus(2);
                } else {
                    chemicalFiberLabelDTO.setStatus(1);
                }
                break;
            default:
                chemicalFiberLabelDTO.setStatus(6);
        }
        return chemicalFiberLabelDTO;
    }

    /**
     *
     * @param status 入库：RK 出库：SH 退库：TK 退货：TH
     * @return
     */
    public String getScanNumber (Integer status) {
        String scanNumber;
        String type = getTypeStr(status);
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        ScanRecordQueryCriteria scanRecordQueryCriteria = new ScanRecordQueryCriteria();
        scanRecordQueryCriteria.setStartTime(new Timestamp(Long.parseLong(timeMap.get("time").toString())));
        scanRecordQueryCriteria.setEndTime(new Timestamp(System.currentTimeMillis()));
        scanRecordQueryCriteria.setType(type);
        //List<ScanRecordDTO> scanRecordDTOS = scanRecordService.queryAll(scanRecordQueryCriteria);
        List<ScanRecord> scanRecordDTOS = scanRecordRepository.getTime(year + "-" + month, type);

        if (scanRecordDTOS.size() == 0) {
            scanNumber = type + year + month + "001";
        } else {
            ScanRecord scanRecordDTO = scanRecordDTOS.get(scanRecordDTOS.size() - 1);
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
            case 5:
                type = "TH";
                break;
            case 9:
                type = "TB";
                break;
            case 10:
                type = "TZ";
                break;
            default:
                type = "PD";
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
     * 根据标签号获取对象List
     * @param labelNumber
     * @return
     */
    private List<ChemicalFiberLabelDTO> getLabelNumbers(String labelNumber) {
        ChemicalFiberLabelQueryCriteria chemicalFiberLabelQueryCriteria = new ChemicalFiberLabelQueryCriteria();
        chemicalFiberLabelQueryCriteria.setLabelNumber(labelNumber);
        List<ChemicalFiberLabelDTO> chemicalFiberLabelDTOList = chemicalFiberLabelService.queryAll(chemicalFiberLabelQueryCriteria);
        if (chemicalFiberLabelDTOList.size() == 0) {
            return null;
        }
        return chemicalFiberLabelDTOList;
    }

    /**
     * 根据托板号获取对象List
     * @param labelNumber
     * @return
     */
    private List<ChemicalFiberLabelDTO> getPalletNumber(String labelNumber) {
        ChemicalFiberLabelQueryCriteria chemicalFiberLabelQueryCriteria = new ChemicalFiberLabelQueryCriteria();
        chemicalFiberLabelQueryCriteria.setPalletId(labelNumber);
        List<ChemicalFiberLabelDTO> chemicalFiberLabelDTOList = chemicalFiberLabelService.queryAll(chemicalFiberLabelQueryCriteria);
        if (chemicalFiberLabelDTOList.size() == 0) {
            return null;
        }
        return chemicalFiberLabelDTOList;
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
                checkLabelStatusStr = status != 1 && status != 9 && status != 10? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【待入仓】，请先入仓" : "";
                break;
            case 1:
                checkLabelStatusStr = status != 2 && status != 7 && status != 10 ? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已入仓】，请出仓" : "";
                break;
            case 2:
                checkLabelStatusStr = status != 4 && status != 5 ? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已出仓】，请返仓或退货" : "";
                break;
            case 3:
                checkLabelStatusStr =  chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已作废】";
                break;
            case 4:
                checkLabelStatusStr = status != 2 && status != 7 && status != 10? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已返仓】，请出仓" : "";
                break;
            case 9:
                checkLabelStatusStr = status != 2 && status != 7 && status != 10 ? chemicalFiberLabelDTO.getLabelNumber() + "当前状态【已托板入仓】，请出仓" : "";
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
    public Map monthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        //calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("month", month < 10 ? "0" + month : month);
        map.put("year", year);
        map.put("day", day < 10 ? "0" + day : day);
        return map;
    }

    /**
     * 判断库存里面是否有这个产品
     * @param loadProdId 产品id
     * @param stockList 新增的产品数据
     * @return
     */
    public ChemicalFiberStock isProdId(Integer loadProdId, List<ChemicalFiberStock> stockList) {
        ChemicalFiberStock stock = new ChemicalFiberStock();
        for (int i = 0; i < stockList.size(); i++) {
            if (stockList.get(i).getProdId().equals(loadProdId)) {
                stock = stockList.get(i);
                return stock;
            }
        }
        return stock;
    }

    /**
     * 更新托板记录数据
     * @param chemicalFiberLabels 标签数据
     */
    public void updatePallet(List<ChemicalFiberLabel> chemicalFiberLabels) {
        /*for (ChemicalFiberLabel dto : chemicalFiberLabels) {
            List<ChemicalFiberPallet> PalletList = chemicalFiberPalletRepository.findAll();
            if (dto.getPalletId() != null) {
                ChemicalFiberPallet pallet = isPalletId(dto.getPalletId(), PalletList);
                BigDecimal netWeight = pallet.getNetWeight();
                BigDecimal tare = pallet.getTare();
                BigDecimal grossWeight = pallet.getGrossWeight();
                Integer factPerBagNumber = pallet.getTotalNumber();
                Integer totalBag = pallet.getTotalBag();

                Integer warehousingNumber = pallet.getWarehousingNumber();
                Integer warehousingBag = pallet.getWarehousingBag();

                netWeight = netWeight.subtract(dto.getNetWeight());
                tare = tare.subtract(dto.getTare());
                grossWeight = grossWeight.subtract(dto.getGrossWeight());
                factPerBagNumber = factPerBagNumber - dto.getFactPerBagNumber();
                totalBag = totalBag - 1;

                warehousingNumber = warehousingNumber + dto.getFactPerBagNumber();
                warehousingBag = warehousingNumber + 1;

                pallet.setNetWeight(netWeight);
                pallet.setTare(tare);
                pallet.setGrossWeight(grossWeight);

                *//*if (dto.getStatus() == 2) {
                    pallet.setWarehousingNumber(warehousingNumber);
                    pallet.setWarehousingBag(warehousingBag);
                }*//*
                chemicalFiberPalletRepository.save(pallet);*/
        /*    }
        }*/
    }

    /**
     * 判断托板记录详细信息是否有这个记录
     * @param palletId 托板记录id
     * @param palletList 托板记录详细信息
     * @return
     */
    public ChemicalFiberPallet isPalletId(String palletId, List<ChemicalFiberPallet> palletList) {
        ChemicalFiberPallet pallet = new ChemicalFiberPallet();
        for (int i = 0; i < palletList.size(); i++) {
            if (palletList.get(i).getPalletNumber().equals(palletId)) {
                pallet = palletList.get(i);
                return pallet;
            }
        }
        return pallet;
    }

    /**
     * 新增库存
     * @param chemicalFiberLabels 标签信息
     * @param stockList 库存列表
     */
    public void saveStock(List<ChemicalFiberLabel> chemicalFiberLabels) {
        for (ChemicalFiberLabel newChemicalFiberLabelDTO : chemicalFiberLabels ) {
            ChemicalFiberStock stock = new ChemicalFiberStock();
            List<ChemicalFiberStock> stockList = chemicalFiberStockRepository.findAll();
            stock = isProdId(newChemicalFiberLabelDTO.getProductId(), stockList);
            BigDecimal Weight = new BigDecimal(0);
            BigDecimal tare = new BigDecimal(0);
            BigDecimal grossWeight = new BigDecimal(0);
            Integer number = 0;
            Integer Bag = 0;
            if (stock.getId() != null) {
                if (stock.getTotalNetWeight() != null && stock.getTotalTare() != null && stock.getTotalNumber() != null
                && stock.getTotalBag() != null) {
                    Weight = stock.getTotalNetWeight();
                    tare = stock.getTotalTare();
                    grossWeight = stock.getTotalGrossWeight();
                    number = stock.getTotalNumber();
                    Bag = stock.getTotalBag();
                }
                stock.setTotalNetWeight(Weight.add(newChemicalFiberLabelDTO.getNetWeight()));
                stock.setTotalTare(tare.add(newChemicalFiberLabelDTO.getTare()));
                stock.setTotalGrossWeight(grossWeight.add(newChemicalFiberLabelDTO.getGrossWeight()));
                stock.setTotalNumber(number.intValue() + newChemicalFiberLabelDTO.getFactPerBagNumber());
                stock.setTotalBag(++Bag);
                chemicalFiberStockRepository.save(stock);
            } else {
                String modelAndName = newChemicalFiberLabelDTO.getColor() + "-" + newChemicalFiberLabelDTO.getFineness();
                stock.setProdId(newChemicalFiberLabelDTO.getProductId());
                stock.setProdModel(modelAndName);
                stock.setProdName(modelAndName);
                stock.setProdColor(newChemicalFiberLabelDTO.getColor());
                stock.setProdFineness(newChemicalFiberLabelDTO.getFineness());
                stock.setTotalNetWeight(newChemicalFiberLabelDTO.getNetWeight());
                stock.setTotalTare(newChemicalFiberLabelDTO.getTare());
                stock.setTotalGrossWeight(newChemicalFiberLabelDTO.getGrossWeight());
                stock.setTotalNumber(newChemicalFiberLabelDTO.getFactPerBagNumber());
                stock.setTotalBag(++Bag);
                chemicalFiberStockRepository.save(stock);
            }
        }
    }

    /**
     * 生产报表记录
     * @param chemicalFiberLabels
     */
    public void saveProdctionReport(List<ChemicalFiberLabel> chemicalFiberLabels) {
        for (ChemicalFiberLabel dto : chemicalFiberLabels) {
            /*DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time = sdf.format(dto.getPrintTime());*/
            ChemicalFiberProductionReport report = productionReportService.getReport(dto.getPrintTime(),dto.getShifts(), dto.getMachine(), dto.getProductionId());
            if (report != null) {
                BigDecimal warehousingPacketNumber = report.getWarehousingPacketNumber();
                BigDecimal warehousingFactPerBagNumber = report.getWarehousingFactPerBagNumber();
                BigDecimal warehousingNetWeight = report.getWarehousingNetWeight();
                BigDecimal warehousingGrossWeight = report.getWarehousingGrossWeight();

                report.setWarehousingPacketNumber(warehousingPacketNumber.add(new BigDecimal(1)));
                report.setWarehousingFactPerBagNumber(warehousingFactPerBagNumber.add(new BigDecimal(dto.getFactPerBagNumber())));
                report.setWarehousingNetWeight(warehousingNetWeight.add(dto.getNetWeight()));
                report.setWarehousingGrossWeight(warehousingGrossWeight.add(dto.getGrossWeight()));
                productionReportService.update(report);

            } else {
                ChemicalFiberProductionReportDTO reportDTO = new ChemicalFiberProductionReportDTO();
                reportDTO.setProductionPacketNumber(new BigDecimal(1));
                reportDTO.setProductionFactPerBagNumber(new BigDecimal(dto.getFactPerBagNumber()));
                reportDTO.setProductionNetWeight(dto.getNetWeight());
                reportDTO.setProductionGrossWeight(dto.getGrossWeight());

                ChemicalFiberProduction production = chemicalFiberProductionRepository.findById(dto.getProductionId()).orElseGet(ChemicalFiberProduction::new);
                reportDTO.setFineness(dto.getFineness());
                reportDTO.setColor(dto.getColor());
                reportDTO.setProductionId(dto.getProductionId());
                reportDTO.setProductionNumber(production.getNumber());
                reportDTO.setProdId(dto.getProductId());
                reportDTO.setShifts(dto.getShifts());
                reportDTO.setMachine(dto.getMachine());
                reportDTO.setTime(dto.getPrintTime());
                productionReportService.create(reportDTO);
            }
        }
    }

    /**
     *生成托板记录的单号
     * @return
     */
    public String getPalletScanNumber () {
        String scanNumber;
        String type = "2";
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();
        String day = timeMap.get("day").toString();

        Integer size = chemicalFiberPalletRepository.getSize("2" + year + month + day);


        if (size == 0) {
            scanNumber = type + year + month + day + "000001";
        } else {
            size += 1;
            String tempNumberStr = String.format("%6d", size).replace(" ", "0");
            scanNumber = type + year + month + day + tempNumberStr;
        }
        return scanNumber;
    }

    public void errorTextSave(ErrorText Date) {
        Date.setCreateDate(new Timestamp(System.currentTimeMillis()));
        errorTextRepository.save(Date);
    }

}
