/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 供应商合同管理
 */

Ext.define('EBDesktop.Contract', {
    extend: 'Ext.ux.desktop.Module',

    id:'contract-win',

    init : function(){
        this.launcher = {
            text: '供应商合同管理',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('contract.html');
    },

    statics: {
    }
});

