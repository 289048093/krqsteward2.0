package com.ejushang.steward.ordercenter.service.transportation;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.EJSDateUtils;
import com.ejushang.steward.common.util.Money;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.common.util.PoiUtil;
import com.ejushang.steward.ordercenter.domain.Contract;
import com.ejushang.steward.ordercenter.domain.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Shiro
 * Date: 14-4-12
 * Time: 下午1:58
 */
@Service
public class SupplierService {

    static final Logger logger = Logger.getLogger(SupplierService.class);

    @Autowired
    private GeneralDAO generalDAO;
    @Autowired
    private BrandService brandService;

    /**
     * 分页查询供应商
     *
     * @param name
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<Supplier> listPageByName(String name, Page page) {
        Search search = new Search(Supplier.class).addFilterEqual("deleted", false);
        if (StringUtils.isNotBlank(name)) {
            search.addFilterLike("name", "%" + name.trim() + "%");
        }
        search.addPagination(page);
        //noinspection unchecked
        return generalDAO.search(search);
    }

    /**
     * 添加供应商
     *
     * @param supplier
     */
    @Transactional
    public void saveSupplier(Supplier supplier) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("SupplierService类中的saveSupplier方法参数Supplier[%s]", supplier));
        }
        if (NumberUtil.isNullOrZero(supplier.getId()) && isSupplierExist(supplier)) {
            throw new StewardBusinessException("该供应商已存在");
        }
        generalDAO.saveOrUpdate(supplier);

    }

    /**
     * 根据ID删除供应商
     *
     * @param idArray
     */
    @Transactional
    public Supplier deleteById(Integer idArray) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("PlatformService类中的deleteById方法参数array数组[%d]", idArray));
        }

        if (brandService.countBySupplierId(idArray) > 0) {
            throw new StewardBusinessException("该供应商下还有诸多品牌不能删除，请检查后重新操作");
        }
        Supplier supplier = findSupplierById(idArray);
        if (supplier != null) {
            supplier.setDeleted(true);
            generalDAO.saveOrUpdate(supplier);
        }

        return supplier;
    }

    /**
     * 查找供应商
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Supplier findSupplierById(Integer id) {
        return (Supplier) generalDAO.searchUnique(
                new Search(Supplier.class)
                        .addFilterEqual("id", id)
                        .addFilterEqual("deleted", false));
    }

    /**
     * 判断供应商是否存在
     *
     * @param supplier
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isSupplierExist(Supplier supplier) {
        Search search = new Search(Supplier.class).addFilterEqual("name", supplier.getName()).addFilterEqual("deleted", false);
        int count = generalDAO.count(search);
        return count > 0;
    }


    /**
     * 根据name获得供应商
     *
     * @param name
     * @return
     */
    public Supplier getByName(String name) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByName方法的参数为name[%s]", name));
        }
        Search search = new Search(Supplier.class);
        search.addFilterEqual("name", name);
        List<Supplier> suppliers = generalDAO.search(search);
        if (suppliers == null || suppliers.size() == 0) {
            return null;
        }
        return suppliers.get(0);
    }

    /****************************合同模块****************************/
    /**
     * 通过ID获得合同
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Contract get(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("通过合同的id[%d]获得合同信息", id));
        }
        return generalDAO.get(Contract.class, id);
    }


    /**
     * 通过查询条件或者合同集合
     *
     * @param supplierId
     * @param code
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<Contract> findContractByAll(Integer supplierId, String code, Page page) {
        Search search = new Search(Contract.class);
        if (!NumberUtil.isNullOrZero(supplierId)) {
            search.addFilterEqual("supplierId", supplierId);
        }
        if (!StringUtils.isBlank(code)) {
            search.addFilterLike("code", "%" + code + "%");
        }
        search.addSortDesc("createTime").addPagination(page);
        //noinspection unchecked
        return generalDAO.search(search);
    }

    /**
     * 保存
     *
     * @param contract
     */
    @Transactional
    public void saveContract(Contract contract) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("合同信息 contract[%s]", contract));
        }
        generalDAO.saveOrUpdate(contract);
    }

    /**
     * 通过数组删除合同
     * 暂时提供给运营部使用。
     *
     * @param array
     */
    @Transactional
    public List<Contract> deleteContract(int[] array) {
        List<Contract> contracts=new ArrayList<Contract>(array.length);
        for (int i = 0; i < array.length; i++) {
            Contract contract = this.get(array[i]);
            generalDAO.remove(contract);
            if(contract!=null) {
                contracts.add(contract);
            }
        }
        return contracts;
    }

    /**
     * 通过合作商ID判断合同是否过期
     *
     * @param supplierId
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isContractExpire(Integer supplierId) {
        Search search = new Search(Contract.class).addFilterEqual("supplierId", supplierId).addSortDesc("createTime");
        List<Contract> contractList = generalDAO.search(search);
        Contract contract = contractList.get(0);
        if (contract.getRealEndTime() == null) {
            return false;
        }
        /**将合同终结日期转换成可以比较的格式***/
        Date date = EJSDateUtils.parseDate(contract.getRealEndTime().toString(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
        return EJSDateUtils.getCurrentDate().after(date);
    }


    /**
     *还要写一个终止合同的方法  参数和供应商id和endReason
     */

    /**
     * 导入合同excel
     *
     * @param sheet
     * @throws ParseException
     */
    @Transactional
    public void leadInContract(Sheet sheet) throws ParseException {
        if (sheet.getLastRowNum() < 2) {
            throw new StewardBusinessException("没有数据!");
        }
        //删除标题
        sheet.removeRow(sheet.getRow(0));
        sheet.removeRow(sheet.getRow(1));
        for (Row row : sheet) {
            /** 将单元格设置成string型，不然纯数字就不能转换*/
            if (row == null) {
                return;
            }
            for (Cell cell : row) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }
            this.validateExcel(row);
        }
    }

    /**
     * 导单前验证excel表单是否合法     暂时未完成
     *
     * @param row
     * @return
     */
    @Transactional
    public void validateExcel(Row row) throws ParseException {
        /**供应商数据注入*/
        Contract contract = new Contract();
        contract.setCode(PoiUtil.getStringCellValue(row, 0));             //bao合同编号
        int cellIndex = 3;
        contract.setEndReason(PoiUtil.getStringCellValue(row, cellIndex++));       //终止原因
        contract.setOverdueFine(PoiUtil.getStringCellValue(row, cellIndex++));      //滞纳金情况
        String deposit = PoiUtil.getStringCellValue(row, cellIndex++);
        if (!StringUtils.isBlank(deposit) && !deposit.matches("\\d+(\\.\\d+)?")) {
            throw new StewardBusinessException("滞纳金必须为数字");
        }
        contract.setDeposit(Money.valueOf(deposit));          //保证金
        String serviceFee = PoiUtil.getStringCellValue(row, cellIndex++);
        if (!StringUtils.isBlank(serviceFee) && !serviceFee.matches("\\d+(\\.\\d+)?")) {
            throw new StewardBusinessException("服务费必须为数字");
        }
        contract.setServiceFee(Money.valueOf(serviceFee));       //服务费
        contract.setInvoiceEJSTitle(PoiUtil.getStringCellValue(row, cellIndex++));  //给客户开发票时，易居尚平台的抬头
        contract.setInvoiceOtherTitle(PoiUtil.getStringCellValue(row, cellIndex++));//给客户开发票时，非易居尚平台的抬头
        contract.setInvoiceToEJS(PoiUtil.getStringCellValue(row, cellIndex++));     //第三方平台销售是否补开发票给易居尚
        contract.setOtherRule(PoiUtil.getStringCellValue(row, cellIndex++));        //其它条款
        contract.setRemark(PoiUtil.getStringCellValue(row, cellIndex++));           //补充协议
        String supplierName = PoiUtil.getStringCellValue(row, cellIndex++);
        Supplier supplier = this.getByName(supplierName);//供应商
        if (supplier == null) {
            throw new StewardBusinessException(String.format("供应商[%s]不存在，行号：%d", supplierName, row.getRowNum() + 1));
        }
        contract.setEjsCompName(PoiUtil.getStringCellValue(row, cellIndex++));//采购商
        contract.setSupplierId(supplier.getId());
        String paymentType = PoiUtil.getStringCellValue(row, cellIndex++);        //结算方式
        String paymentRule = PoiUtil.getStringCellValue(row, cellIndex++);   //结算规则
        String shippingFee = PoiUtil.getStringCellValue(row, cellIndex++);           //物流补贴
        String shotFee = PoiUtil.getStringCellValue(row, cellIndex++);           //拍摄费用
        String commission = PoiUtil.getStringCellValue(row, cellIndex);           //佣金
        contract.setShippingFeeType(shippingFee);
//        contract.setBoxFeeType(boxFee);
//        contract.setThirdPlatformFeeType(thirdPlatformFee);
//        contract.setToEJSFeeType(toEJSFee);
        contract.setShotFeeType(shotFee);
        contract.setPaymentType(paymentType);
        contract.setPaymentRule(paymentRule);
        contract.setCommission(commission);


        /**日期统一处理**/
        String allTime = row.getCell(1).getStringCellValue();
        String[] temp = allTime.split("-");
        SimpleDateFormat simFormat = new SimpleDateFormat("yyyy.MM.dd");
        String realEndTimeStr = PoiUtil.getStringCellValue(row, 2);

        try {
            contract.setRealEndTime(StringUtils.isBlank(realEndTimeStr) ? null : simFormat.parse(realEndTimeStr)); //实际中止时间
            /**
             * 判断是否写入了日期
             */
            if (temp.length > 0) {
                contract.setBeginTime(simFormat.parse(temp[0]));               //合同开始时间
                contract.setEndTime(simFormat.parse(temp[1]));                 //合同结束时间
            }
        } catch (ParseException e) {
            throw new StewardBusinessException("日期格式有误~！ 日期格式：YYYY.MM.DD!");
        }
        /**存入数据库*/
        this.saveContract(contract);
    }
}
