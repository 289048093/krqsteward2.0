package com.ejushang.steward.ordercenter.service.transportation;

import com.ejushang.steward.common.exception.StewardBusinessException;
import com.ejushang.steward.common.genericdao.dao.hibernate.GeneralDAO;
import com.ejushang.steward.common.genericdao.search.Search;
import com.ejushang.steward.common.page.Page;
import com.ejushang.steward.ordercenter.constant.PlatformType;
import com.ejushang.steward.ordercenter.domain.BrandPlatform;
import com.ejushang.steward.ordercenter.domain.Platform;
import com.ejushang.steward.ordercenter.service.ProductPlatformService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Shiro
 * Date: 14-4-12
 * Time: 下午1:15
 */
@Service
public class PlatformService {

    private static final Logger logger = Logger.getLogger(PlatformService.class);

    @Autowired
    private GeneralDAO generalDAO;
    @Autowired
    private ProductPlatformService productPlatformService;

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Platform getById(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("PlatformService类中的getById方法,id类型为Integer[{}]", id));
        }
        return generalDAO.get(Platform.class, id);
    }

    /**
     * 分页查询正在运营的平台
     *
     * @param platform
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<Platform> getByKey(Platform platform, Page page) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("PlatformService类中的getByKey方法参数Platform[%s]", platform));
        }
        Search search = new Search(Platform.class);
        //启用查询缓存
        search.setCacheable(true);
        if (platform != null) {
            if (StringUtils.isNotBlank(platform.getName())) {
                search.addFilterLike("name", "%" + platform.getName().trim()+ "%");
            }
        }
        search.addPagination(page);
        return generalDAO.search(search);
    }

    /**
     * 正在运营的平台
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<Platform> findPlatform() {
        Search search = new Search(Platform.class);
        return generalDAO.search(search);
    }

    /**
     * 修改、添加平台信息
     *
     * @param platform
     */
    @Transactional
    public void savePlatform(Platform platform) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("PlatformService类中的platformUpdate方法参数Platform[%s]", platform.toString()));
        }
        if (platform.getId() == null && isPlatformExist(platform)) {
            throw new StewardBusinessException("该平台已存在");
        }
        generalDAO.saveOrUpdate(platform);
    }

    /**
     * 根据ID删除平台信息
     */
    @Transactional
    public void deleteById(Integer id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("PlatformService类中的deleteById方法参数id[%s]", id));
        }
        if (productPlatformService.listByShopId(id).size()>0) {
            throw new StewardBusinessException("该平台还有商品正在运营，不能进行此操作！");
        }
        generalDAO.removeById(Platform.class, id);
    }

    /**
     * 判断平台是否存在
     *
     * @param platform
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isPlatformExist(Platform platform) {
        Search search = new Search(Platform.class).addFilterEqual("name", platform.getName());
        //启用查询缓存
        search.setCacheable(true);
        int count = generalDAO.count(search);
        return count > 0;
    }

    /**
     * 判断平台是否有运营的品牌
     *
     * @param id
     * @return
     */
    public boolean isPlatformIdExist(Integer id) {
        Search search = new Search(BrandPlatform.class).addFilterEqual("platformId", id);
        //启用查询缓存
        search.setCacheable(true);
        int count = generalDAO.count(search);
        return count > 0;
    }

    /**
     * 通过类型查找平台
     *
     * @param type
     * @return
     */
    public Platform findByType(PlatformType type) {
        if (type == null) {
            return null;
        }
        Search search = new Search(Platform.class).addFilterEqual("type", type);
        //启用查询缓存
        search.setCacheable(true);
        return (Platform) generalDAO.searchUnique(search);
    }
}
