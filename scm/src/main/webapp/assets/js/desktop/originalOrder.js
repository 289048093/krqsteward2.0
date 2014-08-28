/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 库房管理
 */

Ext.define('EBDesktop.originalOrder', {
    extend: 'Ext.ux.desktop.Module',

    id:'originalOrder-win',

    init : function(){
        this.launcher = {
            text: '原始订单管理',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('originalOrder.html');
    },

    statics: {
    }
});

