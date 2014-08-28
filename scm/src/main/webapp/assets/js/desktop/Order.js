/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 库房管理
 */

Ext.define('EBDesktop.Order', {
    extend: 'Ext.ux.desktop.Module',

    id:'order-win',

    init : function(){
        this.launcher = {
            text: '订单管理',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('order.html');
    },

    statics: {
    }
});

