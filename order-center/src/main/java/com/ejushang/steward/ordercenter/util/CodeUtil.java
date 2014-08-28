package com.ejushang.steward.ordercenter.util;

import com.ejushang.steward.common.Application;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-4-12
 * Time: 下午2:59
 * To change this template use File | Settings | File Templates.
 */
public class CodeUtil {

    public static final String LEVEL_PATTERN = "___";


    private static GeneralDAO generalDAO = Application.getBean(GeneralDAO.class);

    private CodeUtil() {

    }

    /**
     * 生成产品类别的code
     * <p/>
     * 从00开始，同级依次递增，下级增加两位;
     *
     * @param parentCode 父级的code，不对父级的code做正确性校验
     * @return
     */
    public static String createCode(String parentCode) {
        if (parentCode == null) {
            parentCode = "";
        }
        synchronized (CodeUtil.class) {
            String pattern = parentCode + LEVEL_PATTERN;
            @SuppressWarnings("JpaQlInspection") String hql = "select max(code) from ProductCategory cat where code like :code";
            Query query = generalDAO.getSession().createQuery(hql);
            query.setString("code", pattern);
            Object res = query.uniqueResult();
            if (res == null) {
                if (StringUtils.isBlank(parentCode)) {
                    return "000";
                } else {
                    return parentCode + "000";
                }
            }
            int newIndex = Integer.parseInt(res.toString()) + 1;
            return String.format("%0" + pattern.length() + "d", newIndex);
        }
    }

    public static String updateCode(String code) {
        char end = code.charAt(code.length() - 1);
        char newEnd = (char) (end - 1);
        return code.substring(0, code.length() - 1) + newEnd;
    }

}
