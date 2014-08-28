/**
 * 日志类型列表
 */
Ext.define('Log.model.BusinessLog', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'businessLogId',
        'content',
        'operationType',
        {name: 'createTime', type: 'date', dateFormat: 'time'},
        'executionTime'
    ],
    idProperty:'id'
});