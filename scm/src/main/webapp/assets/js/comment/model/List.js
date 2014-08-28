/*
 * Created by king on 13-12-19
 */

Ext.define('Comment.model.List',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields: [
            'comment',
            {name: 'id', type: 'int',mapping:'comment.id'},

            {name:'commenter',type:'auto',mapping:'comment.commenter'},
            {name:'buyerId',type:'auto',mapping:'comment.buyerId'},
            {name:'mobile',type:'auto',mapping:'comment.mobile'},
            {name:'platformType',type:'auto',mapping:'comment.platformType'},
            {name:'platformOrderNo',type:'auto',mapping:'comment.platformOrderNo'},
            {name:'platformSubOrderNo',type:'auto',mapping:'comment.platformSubOrderNo'},
            {name:'productName',type:'auto',mapping:'comment.productName'},
            {name:'productSku',type:'auto',mapping:'comment.productSku'},
            {name:'result',type:'auto',mapping:'comment.result'},
            {name:'categories',type:'auto',mapping:'comment.categories'},
            {name:'contents',type:'auto',mapping:'comment.contents'},
            {name:'shopName',type:'auto',mapping:'comment.shopName'},
            {name:'categories',type:'auto',mapping:'comment.categories'},
            'customerTags'

        ],
        idProperty: 'id'
    }
)

