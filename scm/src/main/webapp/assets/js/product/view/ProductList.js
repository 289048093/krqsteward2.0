/*
 * Created by king on 13-12-17
 */

Ext.define('Product.view.ProductList', {
    extend: 'Ext.container.Container',
    id: 'ProductList',
    itemId: 'ProductList',
    alias: 'widget.productList',
    title: "产品管理",
    fixed: true,
    layout: "border",
    //selType: "checkboxmodel",
    initComponent: function () {

        /**
         *  data.list  生成store
         * @param url
         * @param insertAll
         * @param fields
         * @returns {Ext.data.Store}
         */
        function createComboStore(url, insertAll, fields) {
            var comboStore = new Ext.data.Store({
                    singleton: true,
                    proxy: {
                        type: 'ajax',
                        url: url,
                        actionMethods: 'get',
                        reader: {
                            type: 'json',
                            root: 'data.list'
                        }
                    },
                    fields: fields || [
                        'id',
                        'name',
                    ],
                    autoLoad: false,
                    pageSize: 300000
                }),
                options = {};

            if (insertAll) {
                options = {
                    callback: function () {
                        comboStore.insert(0, { name: '全部', id: null });
                    }
                };
            }
            comboStore.load(options);

            return comboStore;
        }

        var brandListStore = createComboStore('/brand/list_brand_id_name', true);

        var searchForm = Ext.create('Ext.form.Panel', {
            title: "产品管理",
            region: 'north',
            layout: {
                type: 'hbox',
                align: 'left'
            },
            height: 'auto',
            border: 0,
            bodyStyle: {
                padding: '6px 0 6px 8px'
            },
            id: 'search',
            defaultType: 'fieldcontainer',
            defaults: {
                labelWidth: 48,
                style: 'margin: 0 10px 0 0',
                xtype: 'textfield'
            },
            items: [
                {
                    xtype:'combo',
                    name: 'brandId',
                    itemId: 'brandId',
                    fieldLabel: '品牌',
                    editable: false,
                    queryMode: 'local',
                    valueField: 'id',
                    displayField: 'name',
                    emptyText: '不限',
                    store: brandListStore
                },
                {
                    xtype: 'combo',
                    name: 'searchType',
                    itemId: 'searchType',
                    editable: false,
                    queryMode: 'local',
                    value: 'productNo',
                    store: [
                        ["sku", "sku"],
                        ["name", "产品名称"],
                        ["productNo", "商品编号"]
                    ]
                },
                {
                    name: 'searchValue',
                    id: 'code',
                    emptyText:'请输入查询关键字',
                    labelWidth: 60,
                },
                {
                    xtype: 'button',
                    text: '查询',
                    width: 55,
                    itemId: 'searchBtn'
                }
            ]

        });

        var cmData;

        //创建角色数据表格容器
        Ext.Ajax.request({
            url: '/product/getHead',
            async: false,
            success: function (response, opts) {
                var obj = Ext.decode(response.responseText);
                cmData = obj.data.list;
            },
            failure: function (response, opts) {
                console.log('server-side failure with status code ' + response.status);
            }
        });



        Ext.each(cmData,function(cmData,index){
           if(Ext.isArray(cmData['columns'])){
               Ext.each(cmData['columns'],function(columns,index){
                   if(columns['dataIndex'].indexOf('PlatformUrl')>0){
                        columns['renderer'] = function(value){
                            return '<a target="_blank" href="'+value+'" title="'+value+'">'+value+'</a>';
                        }
                   }
               })
           }
        });



       //生成store fields 字段

        var storeArray =[];

        Ext.each(cmData,function(cmData,index){
            if(cmData.dataIndex !=undefined){
                storeArray.push(cmData.dataIndex);
            }else if(cmData.text!=""){
                Ext.each(cmData.columns,function(columns,index){
                    storeArray.push(columns.dataIndex);
                })
            }
        });

       storeArray.push('brandId');
       storeArray.push('repositoryId');
       storeArray.push('prodCategoryId');
       storeArray.push('outerProductNo');

       storeArray.push({name:'location',type:'string',mapping:'location.value'});
       storeArray.push({name:'style',type:'string',mapping:'style.value'});


        //产品store 动态生成
        var productStore = Ext.create('Ext.data.Store', {
            singleton: true,
            extend: 'Ext.data.Store',
            fields:storeArray,
            proxy: {
                type: 'ajax',
                extraParams: {
                    orders: 0
                },
                api: {
                    read: '/product/list'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data.obj.result',
                    messageProperty: 'message',
                    totalProperty: 'data.obj.totalCount'
                }
            },
            listeners: {
                exception: function (proxy, response, operation) {
                    var data = Ext.decode(response.responseText);
                    Ext.MessageBox.show({
                        title: '警告',
                        msg: data.msg,
                        icon: Ext.MessageBox.ERROR,
                        button: Ext.Msg.OK
                    });
                }
            },
            autoSync: true,
            autoLoad: true,
            pageSize: 33
        });


        var productListGrid = Ext.create("Ext.grid.Panel", {
            region: 'center',
            id: 'productListGrid',
            itemId:'productListGrid',
            foreFit: true,
            selType: 'checkboxmodel',
            store: productStore,
            viewConfig: {
                enableTextSelection: true,
                emptyText: '<div style="text-align:center; padding:10px;color:#F00">没有数据</div>',
                markDirty: false
            },
            columns: cmData,
//            tbar: [
//                {
//                    xtype: 'button',
//                    text: '删除',
//                    iconCls: 'icon-remove',
//                    id: "del"
//                },
//                '-',
//                {
//                    xtype: 'button',
//                    text: '添加',
//                    iconCls: 'icon-add',
//                    id: "add"
//                },
//                '-',
//                {
//                    xtype: 'button',
//                    text: '导入产品',
//                    iconCls: 'icon-import',
//                    id: "include"
//                }
//            ],
            plugins: [
                Ext.create('Ext.grid.plugin.CellEditing', {
                    clicksToEdit: 1 //设置单击单元格编辑
                })
            ],
            bbar: new Ext.PagingToolbar({
                pageSize: 10,
                displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条记录",
                store: productStore,
                displayInfo: true,
                emptyMsg: '没有记录'
            })
        });

        this.items = [searchForm, productListGrid];
        this.callParent(arguments);

    }
});