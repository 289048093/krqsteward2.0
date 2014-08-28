/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 品牌管理
 */

Ext.define('EBDesktop.Brand', {
    extend: 'Ext.ux.desktop.Module',

    id:'brand-win',

    init : function(){
        this.launcher = {
            text: '品牌管理',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('brand.html');
    },

    statics: {
    }
});

