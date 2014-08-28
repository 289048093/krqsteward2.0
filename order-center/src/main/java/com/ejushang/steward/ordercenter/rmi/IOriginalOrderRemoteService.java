package com.ejushang.steward.ordercenter.rmi;

import java.util.List;
import java.util.Map;

/**
 * User: liubin
 * Date: 14-7-9
 */
public interface IOriginalOrderRemoteService {

    boolean tryAnalyzeOriginalOrders(List<Integer> ids);

    boolean getRemoteFetchDataParamMap(Map<String,String> paramMap);

    List<Map<String,String>> getQueueFetchDataParamMaps();
}
