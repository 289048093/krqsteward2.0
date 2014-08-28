package com.ejushang.steward.logisticscenter.util;


import com.ejushang.steward.logisticscenter.domain.LogisticsPrintInfo;
import com.ejushang.steward.logisticscenter.numGenerator.AutoIncreaseLogisticsNumGenerator;
import com.ejushang.steward.logisticscenter.numGenerator.EMSLogisticsNumGenerator;
import com.ejushang.steward.logisticscenter.numGenerator.GenerateException;
import com.ejushang.steward.logisticscenter.numGenerator.SFLogisticsNumGenerator;

import java.util.List;
import java.util.regex.Pattern;

/**获取批量物流单号帮助类
 * User: tin
 * Date: 14-2-10
 * Time: 上午10:17
 */
public class NumGeneratorUtil {
    private NumGeneratorUtil() {
    }

    /**
     * 获取物流单号
     * @param logisticsPrintInfo 物流公司实体
     * @param intNo 基数
     * @param orderNos 订单ID数量
     * @return 返回联想号LIST
     */
    public static List<String> getShippingNums(LogisticsPrintInfo logisticsPrintInfo, String intNo, int orderNos) throws GenerateException {
        List<String> numList = null;
        if (logisticsPrintInfo.getName().equals("shunfeng")) {
            SFLogisticsNumGenerator sfLogisticsNumGenerator = new SFLogisticsNumGenerator();
            numList = sfLogisticsNumGenerator.generateNumList(intNo, logisticsPrintInfo.getLaw(), orderNos);
        } else if (logisticsPrintInfo.getName().equals("ems")) {
            EMSLogisticsNumGenerator emsLogisticsNumGenerator = new EMSLogisticsNumGenerator();
            numList = emsLogisticsNumGenerator.generateNumList(intNo, logisticsPrintInfo.getLaw(), orderNos);
        } else {
            AutoIncreaseLogisticsNumGenerator autoIncreaseLogisticsNumGenerator = new AutoIncreaseLogisticsNumGenerator();
            numList = autoIncreaseLogisticsNumGenerator.generateNumList(intNo, logisticsPrintInfo.getLaw(), orderNos);
        }
        return numList;
    }

    public static boolean isNumericByAscii(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        return true;
    }
    public static boolean isNumericByJava(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
    public static boolean isNumericByPattern(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
