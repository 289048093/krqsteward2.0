/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 库房管理
 */

Ext.define('EBDesktop.Refund', {
    extend: 'Ext.ux.desktop.Module',

    id:'refund-win',

    init : function(){
        this.launcher = {
            text: '退货管理',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('refund.html');
    },

    statics: {
    }
});

