package com.ejushang.steward.ordercenter.service.transportation;

import com.ejushang.steward.common.domain.Area;
import com.ejushang.steward.common.domain.City;
import com.ejushang.steward.common.domain.Province;
import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.ordercenter.domain.Repository;
import com.ejushang.steward.ordercenter.domain.Storage;
import com.ejushang.steward.ordercenter.service.LocationService;
import com.ejushang.steward.ordercenter.service.RepositoryService;
import com.ejushang.steward.ordercenter.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joyce.qu
 * Date: 14-8-27
 * Time: 下午2:19
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class RepositoryLoadService {

    @Autowired
    private GeneralDAO generalDAO;

    @Autowired
    private LocationService locationService;

    public void save(com.ejushang.steward.openapicenter.zy.api.aceess.domain.Repository repository) {
        Repository newRepository = new Repository();
        BeanUtils.copyProperties(repository,newRepository);
        newRepository = setLocation(repository,newRepository);
        generalDAO.saveOrUpdate(newRepository);
    }

    private Repository setLocation(com.ejushang.steward.openapicenter.zy.api.aceess.domain.Repository repository,Repository newRepository) {
        String provinceName = repository.getProvinceName();
        String cityName = repository.getCityName();
        String areaName = repository.getAreaName();
        Province province = locationService.findProvinceByName(provinceName);
        City city = locationService.findCityByName(cityName, province.getId());
        Area area = locationService.findAreaByName(areaName, city.getId());
        newRepository.setProvinceId(province.getId());
        newRepository.setCityId(city.getId());
        newRepository.setAreaId(area.getId());
        return newRepository;
    }

    public void update(com.ejushang.steward.openapicenter.zy.api.aceess.domain.Repository repository) {
        Repository oldRepository = findByCode(repository.getCode());
        Integer oldId = oldRepository.getId();
        BeanUtils.copyProperties(repository,oldId);
        oldRepository = setLocation(repository,oldRepository);
        oldRepository.setId(oldId);
        generalDAO.saveOrUpdate(oldRepository);
    }

    public void delete(String[] codes) {
        for (String code : codes) {
            Repository repository = findByCode(code);
            checkStorage(repository);
            generalDAO.remove(repository);
        }
//        String hqlStart = "delete Repository r where r.code in (";
//        String parameter = buildParameter(codes);
//        String hqlEnd = ")";
//        String hql = new StringBuilder("").append(hqlStart).append(parameter).append(hqlEnd).toString();
//        generalDAO.update(hql,null);
    }

    public void delete(String code) {
        Repository repository = findByCode(code);
        checkStorage(repository);
        generalDAO.remove(repository);
    }


//    private String buildParameter(String[] skus) {
//        StringBuilder parameter = new StringBuilder("");
//        for (int i=0;i<skus.length;i++) {
//            if (i!=skus.length-1) {
//                parameter.append("'"+skus[i]+"',");
//            }else {
//                parameter.append("'"+skus[i]+"'");
//            }
//        }
//        return parameter.toString();
//    }

    /**
     * 查询指定 仓库编码 的仓库
     *
     * @param code 仓库编码
     * @return 返回仓库实体
     */
    public Repository findByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return (Repository) generalDAO.searchUnique(new Search(Repository.class).setCacheable(true).addFilterEqual("code", code));
    }

    private void checkStorage(Repository repository) {
        Search search = new Search(Storage.class);
        search.addFilterEqual("repositoryId", repository.getId());
        List<Storage> storageList = generalDAO.search(search);//当库存有记录时，不能删除仓库
        if (storageList.size() > 0) {
            throw new StewardBusinessException("仓库:{"+repository.getName() +"}存在库存，不可删除操作");
        }
    }
}
