package com.ejushang.steward.openapicenter.zy.api.aceess.request;

import com.ejushang.steward.openapicenter.zy.api.aceess.response.ZiYouResponse;
import com.ejushang.steward.openapicenter.zy.api.aceess.util.FileItem;

import java.util.Map;

/**
 * 上传请求接口，支持同时上传多个文件。
 * User: Baron.Zhang
 * Date: 2014/8/7
 * Time: 16:11
 */
public interface ZiYouUploadRequest<T extends ZiYouResponse> extends ZiYouRequest<T> {
    /**
     * 获取所有的Key-Value形式的文件请求参数集合。其中：
     * <ul>
     * <li>Key: 请求参数名</li>
     * <li>Value: 请求参数文件元数据</li>
     * </ul>
     *
     * @return 文件请求参数集合
     */
    public Map<String, FileItem> getFileParams();
}
