package com.ejushang.steward.common.springmvc;


import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.PropertyEditorSupport;

public class ModelAttributeEntityTypeEditor extends PropertyEditorSupport {

    @Autowired
    private GeneralDAO generalDAO;

    private Class<?> entityClass;

    public ModelAttributeEntityTypeEditor(GeneralDAO generalDAO, Class<?> entityClass) {
        this.generalDAO = generalDAO;
        this.entityClass = entityClass;
    }

    public void setAsText(String text) throws IllegalArgumentException {
		text = text.trim();

        try {
            if (!StringUtils.isNumeric(text)) {
                setValue(entityClass.newInstance());
                return;
            }

            setValue(generalDAO.get(entityClass, Integer.parseInt(text)));
		} catch (Exception ex) {
			IllegalArgumentException iae = new IllegalArgumentException(
					"Could not get entity from id: " + ex.getMessage());
			iae.initCause(ex);
			throw iae;
		}
	}
}