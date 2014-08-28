/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 发货管理
 */

Ext.define('EBDesktop.ShopProduct', {
    extend: 'Ext.ux.desktop.Module',

    id:'shopProduct-win',

    init : function(){

        this.launcher = {
            text: '产品同步',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('shopProduct.html');
    },

    statics: {
    }
});

