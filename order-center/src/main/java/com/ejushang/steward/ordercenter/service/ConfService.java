package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Conf;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.common.util.NumberUtil;
import com.ejushang.steward.common.util.PoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User:moon
 * Date: 14-4-14
 * Time: 下午7:55
 */
@Service
@Transactional
public class ConfService {

    private static final Logger log = LoggerFactory.getLogger(ConfService.class);

    @Autowired
    private GeneralDAO generalDAO;

    /**
     * 根据id查询conf
     * @param id
     * @return
     */
    public Conf findConf(Integer id){
        return generalDAO.get(Conf.class,id);
    }

    /**
     * 保存conf
     * @param conf
     */
    public void save(Conf conf){
        if(conf.getValue().length()>128){
            throw new StewardBusinessException("value长度过长!");
        }
        if(conf.getDescription().length()>512){
            throw new StewardBusinessException("配置描述长度过长!");
        }
        generalDAO.saveOrUpdate(conf);

    }

    /**
     * 查询conf列表
     * @param page
     * @return
     */
    public List<Conf> findAllConf(Page page,Conf conf){
        Search search=new Search(Conf.class).setCacheable(true);
        if(conf!=null){
            if(NumberUtil.isNullOrZero(conf.getId())){
                search.addFilterEqual("id",conf.getId());
            }
            if(!StringUtils.isBlank(conf.getName())){
                search.addFilterEqual("name",conf.getName());
            }
        }
        search.addSortDesc("createTime").addPagination(page);
        return generalDAO.search(search);
    }

    /**
     * 查询conf列表
     * @return
     */
    public List<Conf> findAllC(){
        Search search=new Search(Conf.class).setCacheable(true);

        search.addSortDesc("createTime");
        return generalDAO.search(search);
    }

    /**
     * 删除conf
     * @param id
     */
    public void delConfById(Integer id){
        generalDAO.remove(findConf(id));
    }

    /**
     * 根据name来查询记录
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public Conf getConfByName(String name) {
        if(StringUtils.isBlank(name)) return null;
        Search search=new Search(Conf.class).setCacheable(true);
        return (Conf)generalDAO.searchUnique(search.addFilterEqual("name", name));

    }

    /**
     * 根据name来查询记录
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public String getConfValue(String name) {
        Conf conf = this.getConfByName(name);
        if(conf == null) return null;
        return conf.getValue();
    }


    @Transactional(readOnly = true)
    public Integer getConfIntegerValue(String name) {
        Integer result = null;
        String value = this.getConfValue(name);
        if(value == null) return result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("配置项的value在转成Integer的时候报错,name[{}],value[{}]", name, value);
        }
        return result;
    }

    public Workbook testPoi(){

        List<Conf>  confList=findAllC();

        Workbook workbook=new HSSFWorkbook();
        Sheet sheet=workbook.createSheet();

        createExcelTitle(sheet);

        int rowIndex = 2;//从第三行开始，一二行放title
        for (Conf conf : confList) {

            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            renderOrder2Excel(row, cellIndex, conf);
        }
        return workbook;
    }

    private void renderOrder2Excel(Row itemRow, int startCellIndex, Conf conf) {
        PoiUtil.createCell(itemRow, startCellIndex++, conf.getName());
        PoiUtil.createCell(itemRow, startCellIndex++, conf.getValue());
        PoiUtil.createCell(itemRow, startCellIndex++, conf.getDescription());

    }

    private void createExcelTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        PoiUtil.createCell(titleRow, 0, "系统配置");
        int cellIndex = 0;
        Row row = sheet.createRow(1);
        PoiUtil.createCell(row, cellIndex++, "配置key");
        PoiUtil.createCell(row, cellIndex++, "配置value");
        PoiUtil.createCell(row, cellIndex++, "商品详情");
        int orderCellIndex = cellIndex - 1;
        sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, orderCellIndex));  //合并订单标题的单元格

    }
}
