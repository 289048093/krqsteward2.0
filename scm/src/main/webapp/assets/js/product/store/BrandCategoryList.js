Ext.define('Product.store.BrandCategoryList', {

    extend: 'Ext.data.Store',
    model: 'Product.model.BrandCategoryList',
    proxy: {
        type: 'ajax',
        api: {
            read: '/productCategory/list'
            //read:"assets/js/desktop/goods/category.json"
        },
        reader: {
            type: 'json',
            successProperty: 'success',
            root: 'data.obj.result',
            messageProperty: 'message'
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
    autoLoad: false,
    pageSize: 300000000
});
