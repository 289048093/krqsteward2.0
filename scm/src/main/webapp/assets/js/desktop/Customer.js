/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 发货管理
 */

Ext.define('EBDesktop.Customer', {
    extend: 'Ext.ux.desktop.Module',

    id:'customer-win',

    init : function(){

        this.launcher = {
            text: '会员中心',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('customer.html');
    },

    statics: {
    }
});

