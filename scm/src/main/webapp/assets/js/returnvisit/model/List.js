/*
 * Created by king on 13-12-19
 */

Ext.define('Returnvisit.model.List',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields: [
            {name: 'id', type: 'int'},
            'status',
            'visitorRealname',
            'lastVisitTime',
            'platformOrderNo',
            {name: 'lastVisitBeginTime', type: 'date', dateFormat: 'time'},
            {name: 'lastVisitEndTime', type: 'date', dateFormat: 'time'},
            'used',
            'platformType',
            'shopId',
            'blandId',
            'type',
            'buyerId',
            'receiverName',
            'receiverMobile',
            'receiverPhone',
        ],
        idProperty: 'id'
    }
)
