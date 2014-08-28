/**
 * 日志类型列表
 */
Ext.define('Log.model.LogList', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'operatorName',
        'operationName',
        'resourceName',
        {name: 'createTime', type: 'date', dateFormat: 'time'},
        {name: 'executionTime', type: 'date', dateFormat: 'time'},
        'params',
        'requestUrl',
        'description',//描述
        'operationException',//操作异常描述
        'operationResult',//操作结果
        'ip',//ip
        'executionTime',//执行毫秒数
    ],
    idProperty: 'id'
});