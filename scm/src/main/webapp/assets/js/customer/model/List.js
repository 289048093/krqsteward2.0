/*
 * Created by king on 13-12-19
 */

Ext.define('Customer.model.List',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields: [
            {name: 'id', type: 'int'},
            'platformType ',
            'realName',
            'platformUsers',
            {name:'platformType',mapping:'platformUsers.platformType'},
            'address',
            'phone',
            'email',
            'grade',
            'tradeCount',
            'bonusPoint',
            'shops',
            'tags',
            'mobiles',
            {name:'mobile',type:'auto',mapping:'mobiles[0].mobile'},
            'totalTradeFee',
            'tradeCount',
            {name:'platform',
                convert:function(v,record){
                    var platformUsers = record.data.platformUsers,platform = [];
                    Ext.each(platformUsers,function(users,i){
                        platform.push(users.platformType.value);
                    });
                    return platform.join(',');
                }
            },
            {name:'buyerId',
                convert:function(v,record){
                    var platformUsers = record.data.platformUsers,buyerIds = [],userItem = '';
                    Ext.each(platformUsers,function(users,i){
                        usersItem = users.platformType.value+':'+users.buyerId;
                        console.log(userItem);
                        buyerIds.push(usersItem);
                    });
                    return buyerIds.join('<br/>');

                }
            },
            {name:'avgTradeFee',
                convert:function(v,record){
                    return (record.data.totalTradeFee/record.data.tradeCount).toFixed(2);
                }
            },
            {name: 'lastTradeTime', type: 'date', dateFormat: 'time'},
            {name: 'birthday', type: 'date', dateFormat: 'time'},
        ],
        idProperty: 'id'
    }
)
