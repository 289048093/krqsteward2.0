package com.ejushang.steward.ordercenter.service;

import com.ejushang.steward.common.domain.Area;
import com.ejushang.steward.common.domain.City;
import com.ejushang.steward.common.domain.Province;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-8-27
 * Time: 下午2:54
 * To change this template use File | Settings | File Templates.
 */
@Service
public class LocationService {

    @Autowired
    private GeneralDAO generalDAO;

    @Transactional(readOnly = true)
    public Province findProvinceByName(String name) {
        Search search = new Search(Province.class);
        search.addFilterEqual("name",name);
        return (Province)generalDAO.searchUnique(search);
    }

    @Transactional(readOnly = true)
    public City findCityByName(String name,String provinceId) {
        Search search = new Search(City.class);
        search.addFilterEqual("name",name);
        search.addFilterEqual("provinceId",provinceId);
        return (City)generalDAO.searchUnique(search);
    }

    @Transactional(readOnly = true)
    public Area findAreaByName(String name,String cityId) {
        Search search = new Search(Area.class);
        search.addFilterEqual("name",name);
        search.addFilterEqual("cityId",cityId);
        return (Area)generalDAO.searchUnique(search);
    }

}
