/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 库房管理
 */

Ext.define('EBDesktop.Payment', {
    extend: 'Ext.ux.desktop.Module',

    id:'payment-win',

    init : function(){
        this.launcher = {
            text: '预收款分配',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('payment.html');
    },

    statics: {
    }
});

