/**
 * Created with JetBrains WebStorm.
 * Company: Ejs
 * Author: King
 * Date: 13-9-2
 * Time: 下午5:50
 * Information: 发货管理
 */

Ext.define('EBDesktop.Comment', {
    extend: 'Ext.ux.desktop.Module',

    id:'comment-win',

    init : function(){

        this.launcher = {
            text: '评价管理',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        window.open('comment.html');
    },

    statics: {
    }
});

