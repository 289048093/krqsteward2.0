package com.ejushang.steward.common.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;

import java.util.*;

/**
 * POI操作Office
 * User: Sed.Li(李朝)
 * Date: 14-4-12
 * Time: 下午4:12
 */
public class PoiUtil {

    /**
     * 创建一个新单元格
     * 当<code>value==null</code>则默认填入空串""到单元格
     *
     * @param row   在指定行创建
     * @param index 单元格序号
     * @param value 单元格值
     * @return 创建的单元格
     */
    public static Cell createCell(Row row, int index, Date value) {
        Cell cell = row.createCell(index);
        if (value == null) {
            cell.setCellValue("");
            return cell;
        }
        cell.setCellValue(EJSDateUtils.formatDate(value, EJSDateUtils.DateFormatType.DATE_FORMAT_STR_CHINA));
        return cell;
    }

    public static Cell createCell(Row row, int index, Calendar value) {
        Cell cell = row.createCell(index);
        if (value == null) {
            cell.setCellValue("");
            return cell;
        }
        cell.setCellValue(EJSDateUtils.formatDate(value.getTime(), EJSDateUtils.DateFormatType.DATE_FORMAT_STR_CHINA));
        return cell;
    }

    public static Cell createCell(Row row, int index, RichTextString value) {
        Cell cell = row.createCell(index);
        if (value == null) {
            cell.setCellValue("");
            return cell;
        }
        cell.setCellValue(value);
        return cell;
    }

    public static Cell createCell(Row row, int index, String value) {
        Cell cell = row.createCell(index);
        if (value == null) {
            cell.setCellValue("");
            return cell;
        }
        cell.setCellValue(value);
        return cell;
    }
    public static Cell createCell(Row row, int index, String value,CellStyle hssfCellStyle) {

        Cell cell = row.createCell(index);
        if (value == null) {
            cell.setCellValue("");
            return cell;
        }
        hssfCellStyle.setFillPattern(HSSFCellStyle.FINE_DOTS );
        hssfCellStyle.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
        hssfCellStyle.setFillBackgroundColor(new HSSFColor.YELLOW().getIndex());
        cell.setCellStyle(hssfCellStyle);
        cell.setCellValue(value);
        return cell;
    }
    public static Cell createCell(Row row, int index, Boolean value) {
        Cell cell = row.createCell(index);
        if (value == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(value);
        }
        return cell;
    }

    public static Cell createCell(Row row, int index, Double value) {
        Cell cell = row.createCell(index);
        if (value == null) {
            cell.setCellValue("");
            return cell;
        }
        cell.setCellValue(value);
        return cell;
    }

    public static Cell createCell(Row row, int index, Integer value) {
        Cell cell = row.createCell(index);
        if (value == null) {
            cell.setCellValue("");
            return cell;
        }
        cell.setCellValue(value);
        return cell;
    }

    /**
     * 获取指定行的指定序号的单元格字符串值
     * 当获取的是数字时自动转换为字符串，如果是其他类型，返回""
     *
     * @param row   行
     * @param index 单元格序号
     * @return 单元格值或者""
     */
    public static String getStringCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return "";
        }
        try {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return cell.getStringCellValue().trim();
        } catch (Exception e) {
            try {
                double val = cell.getNumericCellValue();
                return val - (int) val == 0 ? String.valueOf((int) val) : String.valueOf(val); //如果小数部分为0则取整数
            } catch (Exception e1) {
                return "";
            }
        }
    }


    /**
     * 获取指定行的指定序号的单元格数值
     * 当获取的是字符串时自动转换为double，如果是其他类型，返回0
     *
     * @param row   行
     * @param index 单元格序号
     * @return 单元格值或者null
     */
    public static Double getNumericCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return null;
        }
        try {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            return cell.getNumericCellValue();
        } catch (Exception e) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (Exception e1) {
                return null;
            }
        }
    }

    public static Integer getIntegerCellValue(Row row, int index) {
        Double dv = getNumericCellValue(row, index);
        return dv == null ? null : dv.intValue();
    }

    /**
     * 获取指定时间类型的单元格时间
     *
     * @param row
     * @param index
     * @param type
     * @return
     */
    public static Date getDateCellValue(Row row, int index, EJSDateUtils.DateFormatType type) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return null;
        }
        try {
            return cell.getDateCellValue();
        } catch (Exception e) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String str = getStringCellValue(row, index);
            try {
                return EJSDateUtils.parseDate(str, type);
            } catch (Exception e1) {
                return null;
            }
        }
    }

    /**
     * 获取指定单元格时间，时间格式“yyyy-MM-dd"
     *
     * @param row
     * @param index
     * @return
     */
    public static Date getDateCellValue(Row row, int index) {
        return getDateCellValue(row, index, EJSDateUtils.DateFormatType.DATE_FORMAT_STR);
    }
}
