package com.ejushang.steward.common.service;

import com.ejushang.steward.common.domain.Employee;
import com.ejushang.steward.common.shiro.UrlPermission;
import com.ejushang.uams.api.dto.*;
import com.ejushang.uams.client.UamsClient;
import com.ejushang.uams.client.UamsClientContext;
import com.ejushang.uams.exception.UamsClientException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: liubin
 * Date: 14-4-8
 */
@Service
public class ResourceService {

    private static final Logger log = LoggerFactory.getLogger(ResourceService.class);

    private UamsClient uamsClient = UamsClientContext.createUamsClient();

    private List<ResourceDto> allResourceList = new ArrayList<ResourceDto>();
    private Map<String, OperationDto> operationMap = new HashMap<String, OperationDto>();
    private Map<String, ResourceDto> operationUrlToResourceMap = new HashMap<String, ResourceDto>();


    /**
     * 缓存所有权限
     * @throws UamsClientException
     */
    public synchronized void load() throws UamsClientException {
        List<ResourceDto> tmpAllResourceList = uamsClient.findStubResources();
        Map<String, OperationDto> tmpOperationMap = new HashMap<String, OperationDto>();
        Map<String, ResourceDto> tmpOperationUrlToResourceMap = new HashMap<String, ResourceDto>();

        for(ResourceDto resourceDto : tmpAllResourceList) {
            for(OperationDto operationDto : resourceDto.getOperationList()) {
                tmpOperationMap.put(operationDto.getUrl(), operationDto);
                tmpOperationUrlToResourceMap.put(operationDto.getUrl(), resourceDto);
            }
        }

        allResourceList = tmpAllResourceList;
        operationMap = tmpOperationMap;
        operationUrlToResourceMap = tmpOperationUrlToResourceMap;

    }

    /**
     * 查找系统所有权限
     * @return
     */
    public List<ResourceDto> findAllResource() {
        return allResourceList;
    }

    /**
     * 根据url查找Operation
     * @param url
     * @return
     */
    public OperationDto getOperationByUrl(String url) {
        return operationMap.get(url);
    }

    /**
     * 根据url查找Resource
     * @param url
     * @return
     */
    public ResourceDto getResourceByUrl(String url) {
        return operationUrlToResourceMap.get(url);
    }

}
