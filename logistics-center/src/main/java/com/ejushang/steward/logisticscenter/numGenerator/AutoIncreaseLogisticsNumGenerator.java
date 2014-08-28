package com.ejushang.steward.logisticscenter.numGenerator;


import java.util.ArrayList;
import java.util.List;

/**
 * 支持批量自增的快递物流单号生成器
 * User: JBOSS.wu
 * Date: 14-4-23
 * Time: 下午3:02
 */
public class AutoIncreaseLogisticsNumGenerator implements LogisticsNumGenerator {


    @Override
    public List<String> generateNumList(String initNo, int law, int count) throws GenerateException {

        List<String> initNos = new ArrayList<String>();
        initNo = initNo.trim();
        String prefix = "";
        if (initNo.matches("^[^\\d]+\\d+$")) {   // 匹配 V0000001 之类的单号
            prefix = initNo.replaceAll("\\d", "");
            initNo = initNo.replaceAll("[^\\d]", "");
        }
        try {
            boolean hasMax = false;  //判断叠加之后是否大于9
            initNos.add(prefix + initNo);
            for (int j = 0; j < count - 1; j++) {
                String waybill = "";
                for (int i = initNo.length() - 1; i >= 0; i--) {
                    char way = initNo.charAt(i);
                    int num = Integer.valueOf(String.valueOf(way));
                    if (hasMax) {
                        num = num + 1;
                    }

                    if (i == initNo.length() - 1) {
                        num = num + law;
                    }

                    if (num > 9 && i == 0) {              //当运行到最后一位叠加还大于9就增长一位
                        waybill = "1" + String.valueOf(num % 10) + waybill;
                    } else if (num > 9) {
                        hasMax = true;
                        waybill = String.valueOf(num % 10) + waybill;
                    } else {
                        hasMax = false;
                        waybill = num + waybill;
                    }
                }
                initNos.add(prefix + waybill);
                initNo = waybill;
            }
        } catch (Exception e) {
            throw new GenerateException("物流单号格式错误，必须是数字串或者字符串加数字串的格式", e);
        }
        return initNos;
    }

}
