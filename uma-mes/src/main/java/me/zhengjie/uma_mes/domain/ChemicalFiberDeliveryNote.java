package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.List;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Entity
@Data
@Table(name="uma_chemical_fiber_delivery_note")
@NamedStoredProcedureQuery(name = "DeliveryNote.task", procedureName = "proc_generate_delivery_detail")
public class ChemicalFiberDeliveryNote implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 出库单号
    @Column(name = "scan_number",nullable = false)
    private String scanNumber;

    // 客户id
    @Column(name = "customer_id")
    private Integer customerId;

    // 客户名称
    @Column(name = "customer_name")
    private String customerName;

    // 客户编号
    @Column(name = "customer_code")
    private String customerCode;

    // 客户地址
    @Column(name = "customer_address")
    private String customerAddress;

    // 联系人
    @Column(name = "contacts")
    private String contacts;

    // 联系电话
    @Column(name = "contact_phone")
    private String contactPhone;

    // 总成本
    @Column(name = "total_cost")
    private BigDecimal totalCost;

    // 总价
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 业务员
    @Column(name = "seller")
    private String seller;

    // 仓管员
    @Column(name = "store_keeper")
    private String storeKeeper;

    // 制单日期
    @Column(name = "create_date")
    private Timestamp createDate;

    // 制单人
    @Column(name = "create_user")
    private String createUser;

    // 制单人
    @Column(name = "fax")
    private String fax;

    @OneToMany(mappedBy = "deliveryNoteId",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<ChemicalFiberDeliveryDetail> chemicalFiberDeliveryDetails;

    public void copy(ChemicalFiberDeliveryNote source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
