/**
 ****************************************** Ext扩展
 */

Ext.ns("Ext.ux.renderer");

Ext.ux.renderer.ComboRenderer = function (combo) {
    return function (value) {
        var record = combo.findRecord(combo.valueField, value);
        return record ? record.get(combo.displayField) : combo.valueNotFoundText;
    };
};

var EB = window.EB || {};
EB.required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';

/**
 * 中文排序问题
 * @param sorters
 * @returns {Function}
 */
Ext.data.Store.prototype.createComparator = function (sorters) {
    return function (r1, r2) {
        var s = sorters[0], f = s.property;
        var v1 = r1.data[f], v2 = r2.data[f];
        //假如是枚举
        if (Ext.isObject(v1)) {
            v1 = v1.value;
            v2 = v2.value;
        }
        var result = 0;
        if (typeof(v1) == "string") {
            if (Ext.String.trim(v1) == '' && Ext.String.trim(v2)  == '') {
                return 0;
            }

            result = v1.localeCompare(v2);

            if (s.direction == 'DESC') {
                result *= -1;
            }
        } else {
            result = sorters[0].sort(r1, r2);
        }


        var length = sorters.length;

        for (var i = 1; i < length; i++) {
            s = sorters[i];
            f = s.property;
            v1 = r1.data[f];
            v2 = r2.data[f];
            if (typeof(v1) == "string") {
                result = result || v1.localeCompare(v2);
                if (s.direction == 'DESC') {
                    result *= -1;
                }
            } else {
                result = result || s.sort.call(this, r1, r2);
            }
        }
        return result;
    };
};


// 自定义vtype
// 验证数字 vtype: 'Number'
Ext.apply(Ext.form.field.VTypes, {
    Number: function (v) {
        return /^[1-9]\d*$/.test(v);
    },
    NumberText: '数字格式不对',
    NumberMask: /[\d]/i
});

// 验证电话号码 vtype: 'Phone'
Ext.apply(Ext.form.field.VTypes, {
    Phone: function (v) {
        return /^(\d{3,4}-)?\d{7,8}(-\d{1,4})?$/.test(v);
    },
    PhoneText: '电话号码格式不对',
    PhoneMask: /[\d-]/i
});

// 验证手机号码 vtype: 'Mobile'
Ext.apply(Ext.form.field.VTypes, {
    Mobile: function (v) {
        return /^0?(1[3458][0-9]{9})$/.test(v);
    },
    MobileText: '手机号码格式不对',
    MobileMask: /[\d]/i
});

// 验证中文，数字，字母下划线 vtype: 'RegularString'
Ext.apply(Ext.form.field.VTypes, {
    RegularString: function (v) {
        return /^([\u4e00-\u9fa5]|\w)*$/.test(v);
    },
    RegularStringText: '不能含有特殊字符！',
});
/**
 * 处理Grid重新加载过后selectionModel中的记录不更新的问题
 * me.selected中存放的是选中的记录的集合
 */
Ext.override(Ext.selection.Model, {
    onStoreLoad: function (store, records, successful, eOpts) {
        var me = this,
            length = me.selected.getCount();

        //如果没有选中的记录，则不需要进行任何的操作
        if (length === 0)return;

        //遍历selected并更新其中的记录
        me.selected.eachKey(function (key, item) {

            var model = store.getById(key);

            //如果获取到了model就更新，否则从selected中移除
            if (model) {
                me.selected.add(model);//add时会覆盖掉原来的值
            } else {
                me.selected.removeAtKey(key);
            }
        })

    }
});




/**
 ****************************************** Espide通用函数、方法和类
 */

var Espide = window.Espide || {};

/**
 * 静态数据共用类
 */

Espide.localData = function (obj) {
    this.obj = obj;
};

Espide.localData.prototype = {
    getData: function (k) {
        var data = '',
            store = [],
            obj = this.obj;

        if (k) {
            data = obj[k] ? obj[k] : data;
        } else {
            for (var p in obj) {
                store.push([p, obj[p]]);
            }
            data = store;
        }
        return data;
    },
    getStore: function (insertAll) {
        var arr = this.getData();
        if(insertAll){
            arr.unshift(["null", "全部"]);
        }else{
            arr.unshift(["", "全部"]);
        }
        //insertAll && arr.unshift(["null", "全部"]);
        return arr;
    }
};


/**
 * 通用函数
 */

Espide.Common = {

    /*
     ****************************
     * 1.基础逻辑、算法通用组件的定义
     *****************************
     */
    /**
     * 根据数据 渲染不同数据  null 事 返回空  whiteSpace是 换行显示 其他就是取枚举
     * @param value
     * @returns {*}
     */
    rendererfn: function (value) {
        switch (value) {
            case null:
                return "";
                break;
            case 'whiteSpace':
                return '<div style="white-space:normal;">' + value + '</div>';
                break;
            default :
                return value['value'];
        }
    },

    /**
     * 将store 转化为json字符串
     * @param jsondata          store
     * @returns {sting}
     */
    storeToJson: function (jsondata) {
        var listRecord;
        if (jsondata instanceof Ext.data.Store) {

            listRecord = new Array();
            jsondata.each(function (record) {
                listRecord.push(record.data);
            });
        } else if (jsondata instanceof Array) {

            listRecord = new Array();
            Ext.each(jsondata, function (record) {
                listRecord.push(record.data);
            });
        }
        return eval(Ext.JSON.encode(listRecord));
    },

    /**
     * 判断所给数据是否为没有意义的”空“数据
     * @param obj
     * @returns {boolean}
     */
    isEmptyData: function (obj) {
        return obj === "null" || obj === "0" || obj === " " || obj === "" || obj === 0 || obj === null;
    },

    /**
     * 数组移位算法
     * @param arr {Array} 要计算的数组
     * @param form {Number}
     * @param to {Number}
     */
    changeArrPos: function (arr, form, to) {
        var temp, i,
            step = form - to;
        if (step === 1 || step === -1 || step === 0) {
            return;
        }
        if (step < 0) {
            temp = arr[to - 1];
            arr[to - 1] = arr[form];
            for (i = form; i < to - 2; i++) {
                arr[i] = arr[i + 1]
            }
            arr[to - 2] = temp;

        } else {
            temp = arr[form];
            for (i = form; i > to; i--) {
                arr[i] = arr[i - 1];
            }
            arr[to] = temp;
        }
    },

    /**
     * 根据一个数组某一字段的值来排序别一数组
     * @param sortArr {Array} 要排序的数组
     * @param byArr {Array} 依据的数组
     * @param type {String} 依据的字段
     * @returns {Array}
     */
    sortArrByArr: function (sortArr, byArr, type) {
        var newArr = [];
        for (var i = 0, j = byArr.length; i < j; i++) {
            for (var m = 0, n = sortArr.length; m < n; m++) {
                if (byArr[i] === sortArr[m][type]) {
                    newArr.push(sortArr[m]);
                }
            }
        }
        return newArr;
    },

    /**
     * 判断一个数组内的元素是否全相等
     * @param arr {Array}
     * @returns {boolean}
     */
    isArrAllEqual: function (arr) {
        var len = arr.length,
            first = arr[0];
        for (var i = 1; i < len; i++) {
            if(first.name){//假如字段是枚举
                if (first.name != arr[i].name) {
                    return false;
                }
            }else{
                if (first != arr[i]) {
                    return false;
                }
            }

        }
        return true;
    },

    /**
     * 判断数据中是包含有空数据
     * @param arr {Array}
     * @returns {boolean}
     */
    hasEmpytData: function (arr) {
        var len = arr.length;
        for (var i = 0; i < len; i++) {
            if (this.isEmptyData(arr[i])) {
                return false
            }
        }
        return true;
    },

    /**
     * 判断对象的属性是否都为没有意思的空数据
     * @param obj {Object}
     * @returns {boolean}
     */
    isEmptyObj: function (obj) {
        var flag = true;
        for (var key in obj) {
            if (obj.hasOwnProperty(key) && !this.isEmptyData(obj[key])) {
                flag = false;
            }
        }
        return flag;
    },

    /**
     * 格式批对象成可查询的字符串
     * @param obj
     * @returns {string}
     */
    parseParam: function (obj) {
        var str = '';
        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
                str += '&' + key + '=' + encodeURIComponent(obj[key]);
            }
        }
        str = str.substr(1);
        return str;
    },

    /**
     * 操作提示信息 tip message
     * @param title {String}
     * @param format
     */
    tipMsg: function (title, format) {
        var msgCt;
        if (!msgCt) {
            msgCt = Ext.DomHelper.insertFirst(document.body, {id: 'msg-div'}, true);
        }
        var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
        var m = Ext.DomHelper.append(msgCt, '<div class="msg"><h3>' + title + '</h3><p>' + s + '</p></div>', true);
        m.hide();
        m.slideIn('t').ghost("t", { delay: 3000, remove: true});
    },
    /**
     *
     * @param data              返回数据data对象
     * @param win               要关闭窗口变量
     * @param gird  {String}    刷新表格id
     * @param flag              是否要关闭窗口
     */
    tipMsgIsCloseWindow: function (data, win, gird, flag) {
        Espide.Common.tipMsg('操作成功', data.msg);
        flag ? win.close() : console.log('不关闭窗口');
        this.getGridObj(gird).getStore().load();
    },

    /**
     * 通用操作前提示
     * @param options
     */
    commonMsg: function (options) {
        var defaults = {
            title: '操作确认?',
            msg: '你确定要处理订单吗，处理后不可复原?',
            fn: function () {
            }
        };

        options = Ext.apply(defaults, options);

        Ext.Msg.show(
            {
                title: options.title,
                msg: options.msg,
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                fn: options.fn
            }
        );
    },

    /**
     * 通用表格toolbar操作响应
     * @param options {Object} 传参
     *  options.url {String} 响应路径
     *  options.params {Object} post数据
     *  options.successCall {Function} 返回成功后要制行的代码
     *  options.successTipMsg {String} 返回成功后的提示信息
     * @returns {Function}
     */
    doAction: function (options) {
        var root = this,
            defaults = {
                url: '/assets/js/order/data/orderList.json',
                params: {},
                successCall: function () {
                },
                failureCall: function () {
                },
                commonCall: function () {
                },
                successTipMsg: '订单操作成功'
            };
        //参数合并
        options = Ext.apply(defaults, options);

        return function (btn) {
            //点击”取消“，则返回，不做任何处理
            if (btn != 'yes') return;

            Ext.Ajax.request({
                url: options.url,
                params: options.params,
                success: function (response) {
                    var data = Ext.decode(response.responseText);
                    if (data.success) {
                        root.tipMsg('操作成功', options.successTipMsg);
                        options.successCall(data);
                    } else {
                        options.failureCall();
                        Ext.Msg.show({
                            title: '错误',
                            msg: data.msg,
                            buttons: Ext.Msg.YES,
                            icon: Ext.Msg.WARNING
                        });
                    }
                    options.commonCall();
                }
//                failure: function () {
//                    options.failureCall();
//                    Ext.Msg.show({
//                        title: '错误',
//                        msg: '服务器错误，请重新提交!',
//                        buttons: Ext.Msg.YES,
//                        icon: Ext.Msg.WARNING
//                    });
//                    options.commonCall();
//                }
            })
        }
    },

    /**
     * 弹出至少选择一项警告
     * @param msg {String} 弹出的消息
     */
    showGridSelErr: function (msg,fn) {
        Ext.Msg.alert({
            title: '警告！',
            msg: msg,
            icon: Ext.Msg.WARNING,
            buttons: Ext.Msg.YES,
            fn:fn
        });
    },

    /**
     * 通用表格默认无数据时的显示内容
     * @param text {String}
     * @returns {{}}
     */
    getEmptyText: function (text) {
        var obj = {};
        text = text || "没有数据";
        obj['emptyText'] = '<div style="text-align:center;padding:10px;color:#F00">' + text + '</div>';
        obj['enableTextSelection'] = true;
        return obj;
    },

    /*
     **************************************************************
     * 2.表格操作通用接口,不涉及具体的表格操作，只做抽象
     **************************************************************
     */

    /**
     * 根据表格id获取表格对象
     * @param gridId {String/Object} 表格id或表格对象
     * @returns {Object}
     */
    getGridObj: function (gridId) {
        gridId = typeof gridId === 'object' ? gridId : Ext.getCmp(gridId);
        return gridId;
    },

    /**
     * 跟据表格id选出所有选中项的id字段，组成数组，并返回
     * @param gridId {String/Object} 表格id或表格对象
     * @returns {Array}  表格选中项的唯一字段组成的数组，如[1,2,3,4]
     */
    getGridSelsId: function (gridId) {
        return this.getGridSels(gridId, "id");
    },

    /**
     * 跟据表格id选出所有选中项的某一字段组成数组，并返回
     * @param gridId {String/Object} 表格id或表格对象
     * @param type {String} 字段名
     * @returns {Array}
     */
    getGridSels: function (gridId, type) {
        var arr = [],
            sels = this.getGridObj(gridId).getSelectionModel().selected.items;


        Ext.each(sels, function (sel, index, sels) {
            arr.push(sel.get(type));
        });
        return arr;
    },

    /**
     * 根据表格id取消表格选中的项
     * @param gridId {String/Object} 表格id或表格对象
     */
    removeGridSel: function (gridId) {
        this.getGridObj(gridId).getSelectionModel().clearSelections();
    },

    /**
     * 判断表格的选中项是否为单项
     * @param gridId {String/Object} 表格id或表格对象
     * @returns {boolean}
     */
    isGridSingleSel: function (gridId) {
        return this.getGridObj(gridId).getSelectionModel().getSelection().length === 1;
    },

    /**
     * 判断选取表格是否有选择项
     * @param gridId {String/Object} 表格id或表格对象
     * @returns {boolean}
     */
    isGridSel: function (gridId) {
        return this.getGridObj(gridId).getSelectionModel().getSelection().length > 0;
    },

    /**
     * 做操作前，检查表格是否有选择项，并弹出提示
     * @param gridId {String/Object} 表格id或表格对象
     * @param msg {String} 没有选中项时的提示信息
     * @returns {boolean}
     */

    checkGridSel: function (gridId, msg) {
        msg = msg || '请至少在表格中选择一项';
        if (!this.isGridSel(gridId)) {
            this.showGridSelErr(msg);
            return false;
        }
        return true;
    },

    /**
     * 重载表格数据,可跟据某一表单搜索数据来重载
     * @param gridId {String/Object} 表格id或表格对象
     * @param formId {String} 表单id,若无，则可传入false
     * @param isRetSel {boolean} 是否取消之前gird中的选择项
     * @param fn {fn} reload 回调函数
     */
    reLoadGird: function (gridId, formId, isRetSel,fn) {
        if (!!formId) {
//            this.getGridObj(gridId).getStore().loadPage(1, {
//                params: Ext.getCmp(formId).getValues()
//            });
            this.getGridObj(gridId).getStore().reload({
                params: Ext.getCmp(formId).getValues(),
                callback:fn||""
            });
        } else {
            this.getGridObj(gridId).getStore().reload({callback:fn||""});
        }

        isRetSel && this.removeGridSel(gridId);
    },

    /**
     * 表格表头根据dataIndex字段排序
     * @param gridColumns {String/Object} 表头数组或表格对象
     * @param byArr {Array} 排序依据的数组
     * @returns {Array}
     */
    sortGridHead: function (gridColumns, byArr) {
        if (!Ext.isArray(gridColumns)) {
            gridColumns = gridColumns.columns;
            for (var i = 0, j = gridColumns.length; i < j; i++) {
                gridColumns[i] = gridColumns[i]['initialConfig'];
            }
        }
        return this.sortArrByArr(gridColumns, byArr, 'dataIndex');
    },

    /**
     * 根据表头的详细数组生成某一字段的新简单数组
     * @param columns {Array} 详数组
     * @param type {String} 要截取的字段
     * @returns {Array} 返回的简单数组
     * @param newArray {String} 显示字段名称
     */
    getGridHead: function (columns, type,newArray) {
        var arr = [];
        Ext.each(columns, function (col, index, cols) {
            Ext.Array.contains(newArray,col['text'])?arr.push(col[type]):null;
        });

        return arr;
    },
    /**
     * 拖动时候 触发函数
     * @param ct     Ext.grid.header.Container
     * @returns {Array}   返回显示文本数据
     */
    getGridHeadText:function(ct){
        //获取显示的字段
        var newArray = [];
        Ext.each(ct.getMenuItems()[3].menu,function(MenuArray){
            if(MenuArray.checked){
                newArray.push(MenuArray['text']);
            }
        });
        return newArray;
    },

    /**
     * 跟据html5 localStorage 或服务器获取表格列的顺序的数组
     * @param gridId {String/Object} 表格id或表格对象
     * @param dataName {String} 名字
     * @param url {String} 数组路径
     * @returns {Array}
     */
    getGridColumnData: function (gridId, url, dataName) {
        var localColumns,
            modelFields = null,
            columns = null;
        dataName = dataName || 'pending';

        if (typeof gridId === 'object') {
            gridId = gridId.getId() || gridId.getItemId();
        }

        if (window.localStorage) {
            //localColumns = window.localStorage[gridId];
            localColumns = localStorage.getItem(gridId + dataName);
        }

        if (localColumns && localColumns.length > 0) {
            //尝试读localStorage的表列排序列数据
            columns = localColumns;
        }

        columns = Ext.JSON.decode(columns);

        //还没有表列排序列数据，就返回false
        if (!columns || columns.length < 1) {
            columns = false;
        }
        return columns;
    },

    /**
     * 保存表格列的顺序
     * @param columns {Array} 表头数组
     * @param fromIdx {Number}
     * @param toIdx {Number}
     * @param url {String}
     * @param gridId {String/Object} 表格id或对象，用来做为存储时的名字
     * @param newArray {String} 显示字段名称
     * @param dataName {String} 用来做为存储时的名字
     */
    saveGridColumnsData: function (columns, fromIdx, toIdx, url, gridId, newArray,dataName) {
        dataName = dataName || 'pending';
        newArray = newArray || '';
        //获取表格对应的列排序数据
        columns = this.getGridHead(columns, 'dataIndex',newArray);

        if (typeof gridId === 'object') {
            gridId = gridId.getId() || gridId.getItemId();
        }

        //本地localStorage存一份列排序数组
        if (window.localStorage) {
            columns = Ext.JSON.encode(columns);
            localStorage.removeItem(gridId + dataName);
            localStorage.setItem(gridId + dataName, columns);
        }
    },

    deleteGridColumnsData:function(dataName){
        if (window.localStorage) {
            localStorage.removeItem(dataName);
        }else{
            this.showGridSelErr('浏览器不支持本地储存')
        }
    },

    /**
     * 通用修改store地址
     * @param reg {Object} 正则对象
     * @returns {Function}
     */
    changeStoreUrl: function (reg) {
        return function (store, fld) {
            store.getProxy().url = store.getProxy().url.replace(reg, '/' + fld);
        }
    },

    /**
     * 返回物流信息
     * 1.如何传入参数为空，则返回所有物流信息的store
     * 2.只传一个字符串参数，或传入两个参数，第二个为对象时，则返回对应物流公司的title
     * 3.传入两个字符串参数，返回相应物流公司的field字段
     * @param key {String}
     * @param field {String}
     * @returns {*}
     */
    getExpress: function (key, field) {
        var data,
            arr = [],
            num = 0;

        data = {
            "shunfeng": {
                name: 'shunfeng',
                title: '顺丰',
                reg: '^[0-9]{12}$'
            },
            "zhongtong": {
                name: 'zhongtong',
                title: '中通',
                reg: '^((618|680|688|618|828|988|118|888|571|518|010|628|205|880|717|718|728|761|762|701|757)[0-9]{9})$|^((2008|2010|8050|7518)[0-9]{8})$'
            },
            "yunda": {
                name: 'yunda',
                title: '韵达',
                reg: '^[0-9]{13}$'
            },
            "zhaijisong": {
                name: 'zhaijisong',
                title: '宅急送',
                reg: '^[0-9]{10}$'
            },
            "ems": {
                name: 'ems',
                title: 'ems',
                reg: '^[A-Z]{2}[0-9]{9}[A-Z]{2}$|[0-9]{13}'
            },
            "yuantong": {
                name: 'yuantong',
                title: '圆通',
                reg: '^(0|1|2|3|5|6|8|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$'
            },
            "shentong": {
                name: 'shentong',
                title: '申通',
                reg: '^(268|888|588|688|368|468|568|668|768|868|968)[0-9]{9}$|^(268|888|588|688|368|468|568|668|768|868|968)[0-9]{10}$|^(STO)[0-9]{10}$'
            },
            "quanritongkuaidi": {
                name: 'quanritongkuaidi',
                title: '全日通',
                reg: ''
            },
            "kuaijiesudi": {
                name: 'kuaijiesudi',
                title: '快捷',
                reg: ''
            },
            "huitongkuaidi": {
                name: 'huitongkuaidi',
                title: '汇通',
                reg: '^(A|B|C|D|E|H|0)(D|X|[0-9])(A|[0-9])[0-9]{10}$|^(21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39)[0-9]{10}$'
            },
            "guotongkuaidi": {
                name: 'guotongkuaidi',
                title: '国通',
                reg: '^(1|2)[0-9]{9}$'
            },
            "lianbangkuaidi": {
                name: 'lianbangkuaidi',
                title: '联邦',
                reg: ''
            },
            "quanfengkuaidi": {
                name: 'quanfengkuaidi',
                title: '全峰',
                reg: '^[0-9]{12}$'
            },
            "suer": {
                name: 'suer',
                title: '速尔',
                reg: ''
            },
            "tiantian": {
                name: 'tiantian',
                title: '天天',
                reg: '^[0-9]{12}$'
            },
            "youshuwuliu": {
                name: 'youshuwuliu',
                title: '优速',
                reg: ''
            },
            "youzhengguonei": {
                name: 'youzhengguonei',
                title: '邮政国内小包',
                reg: '^[GA]{2}[0-9]{9}([2-5][0-9]|[1][1-9]|[6][0-5])$|^[99]{2}[0-9]{11}$'
            },
            "debangwuliu": {
                name: 'debangwuliu',
                title: '德邦物流',
                reg: ''
            },
            "unknown": {
                name: 'unknown',
                title: '未知',
                reg: ''
            }
        };

        //当没有传入参数时，返回store结构的所有物流数据
        if (arguments.length === 0) {
            for (var k in data) {
                var cur = data[k];
                arr[num] = [];
                arr[num].push(cur['name'], cur['title']);
                num++;
            }
            return arr;
        }

        //当第一项参数为null时，返回null
        if (key === null) {
            return ''
        }

        //没有指定第二个参数时及传了一个对象，则理解为取title
        field = typeof field === 'object' ? undefined : field;
        field = field || 'title';

        if (data[key]) {
            return data[key][field];
        }

        return '';
    },

    /**
     * 返回物流公司store
     * @insertAll {Boolean} 是否插入”全部“选择项
     * @returns {Array}
     */
    expressStore: function (insertAll) {
        var arr = this.getExpress();
        insertAll && arr.unshift([null, "全部"]);
        return arr;
    },

    /**
     * 实例化订单抓取对象
     */
    fetchOrderType: new Espide.localData({
        "FETCH_ORDER": "订单抓取",
        "FETCH_REFUND": "退款单抓取",
        "FETCH_RETURN": "退货单抓取",
    }),
    /**
     * 实例化订单状态对象
     */
    orderState: new Espide.localData({
        "null": "全部",
        "WAIT_APPROVE": "待审核",
        "INVALID": "已作废",
        "WAIT_PROCESS": "待处理",
        "CONFIRMED": "已确认",
        "PRINTED": "已打印",
        "EXAMINED": "已验货",
        "INVOICED": "已发货",
        "SIGNED": "已签收",
    }),

    /**
     * 实例化预收款状态对象
     */
    paymentType: new Espide.localData({
        "POST_COVER": "邮费补差",
        "SERVICE_COVER": "服务补差",
        "ORDER_POST_FEE": "订单邮费"
    }),
    /**
     * 实例化预收款分配状态对象
     */
    allocateStatus: new Espide.localData({
        "WAIT_ALLOCATE": "待分配",
        "ALLOCATED": "已分配",
        "REFUND_ONSALE": "售前分配"
    }),


    /**
     * 实例化订单项状态对象
     */
    orderItemStatus: new Espide.localData({
        "null": "全部",
        "NOT_SIGNED": "未签收",
        "SIGNED": "已签收"
    }),
    /**
     * 实例化订单项线上退货状态对象
     */
    orderItemReturnStatus: new Espide.localData({
        "NORMAL": "正常",
        "RETURNED": "已退货"
    }),
    /**
     * 实例化订单项线下退货状态对象
     */
    offlineOrderItemReturnStatus: new Espide.localData({
        "NORMAL": "正常",
        "RETURNED": "已退货"
    }),

    /**
     * 实例化订单自动生成状态
     */
    generateType: new Espide.localData({
        "MANUAL_CREATE": "手动创建",
        "MANUAL_SPLIT": "手动拆分",
        "MANUAL_MERGE": "手动合并",
        "AUTO_CREATE": "自动抓单",
        "AUTO_SPLIT": "自动拆分",
        "AUTO_MERGE": "自动合并"
    }),
    /**
     * 实例化邮费承担方生成状态
     */
    postPlayer: new Espide.localData({
        "BUYER": "顾客",
        "SELLER": "商家",
        "SUPPLIER": "供应商"

    }),
    /**
     * 实例化订单退货状态
     */
    orderReturnStatus: new Espide.localData({
        "NORMAL": "正常",
        "PART_RETURN": "部分退货",
        "RETURNED": "已退货",
    }),
    /**
     * 实例化订单类型对象
     */
    orderType: new Espide.localData({
        "NORMAL": "正常订单",
        "REPLENISHMENT": "补货订单",
        "EXCHANGE": "换货订单",
        "CHEAT": "刷单订单",
    }),
    /**
     * 实例化订单项类型对象
     */
    orderItemType: new Espide.localData({
        "PRODUCT": "商品",
        "MEALSET": "套餐商品",
        "GIFT": "赠品",
        //"EXCHANGE_ONSALE": "售前换货",
        "REPLENISHMENT": "补货",
        "EXCHANGE_AFTERSALE": "售后换货"
    }),
    /**
     * 实例化商品类型对象
     */
    goodType: new Espide.localData({
        "PRODUCT": "商品",
        "GIFT": "礼品"
    }),
    /**
     * 实例化平台类型
     */
    platformType: new Espide.localData({
        "TAO_BAO": "天猫",
        //"STEWARD": "智库城",
        "JING_DONG": "京东",
        "EJS": "易居尚",
    }),
    /**
     * 实例化退款类型
     */
    refundType: new Espide.localData({
        "ORDER": "订单退款",
        "PAYMENT": "预收款退款"
    }),

    /**
     * 通用插入“全部”
     */
    comInsertNull: function (store) {
        store.insert(0, { name: '全部', id: null });
    },
    /**
     * 黑名单分类
     */
    backListType: new Espide.localData({
        "SMS": "短信黑名单",
        "MAIL": "邮件黑名单",
        "PHONE": "电话黑名单",
    }),
    /**
     * 提交表单
     * @param form {Form}
     * @param options {Object} 选择项
     *  options.url {String} 接收表单的URL
     *  options.successMsg {String} 表单提交成功后的提示
     * @param callback {Function} 提交成功后回调方法
     */
    submitForm: function (form, options, callback) {

        var defaults = {
                url: '',
                successMsg: '保存成功'
            },
            root = this;

        options = Ext.apply(defaults, options);

        if (form.isValid()) {
            form.submit({
                url: options.url,
                submitEmptyText: false,
                waitMsg: '保存中...',
                success: function (form, action) {
                    var data = Ext.JSON.decode(action.response.responseText);
                    if (data.success) {
                        root.tipMsg('系统提示', options.successMsg);
                        callback();
                    } else {
                        Ext.MessageBox.show({
                            title: '系统提示',
                            msg: data.msg,
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-warning'
                        });
                    }
                },
                failure: function (form, action) {
                    try {
                        var data = Ext.JSON.decode(action.response.responseText);
                        Ext.MessageBox.show({
                            title: '系统提示',
                            msg: data.msg,
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-warning'
                        });
                    } catch (e) {
                        Ext.MessageBox.show({
                            title: '系统提示',
                            msg: '服务器错误',
                            buttons: Ext.MessageBox.OK,
                            icon: 'x-message-box-error'
                        });
                    }
                }
            });
        }
    },
    /**
     * 创建下拉列表框Store
     * @param url
     * @param insertAll {Boolean} 是否插入”全部“选择项 可选 默认值为false
     * @param fields {Array} 模型的字段 可选 默认为 ['id', 'name']
     * @returns store
     */
    createComboStore: function (url, insertAll, fields) {
        var comboStore = new Ext.data.Store({
                singleton: true,
                proxy: {
                    type: 'ajax',
                    url: url,
                    actionMethods: 'get',
                    reader: {
                        type: 'json',
                        root: 'data.obj.result'
                    }
                },
                fields: fields || [
                    'id',
                    'name',
                ],
                autoLoad: false,
                pageSize: 300000
            }),
            options = {};

        if (insertAll) {
            options = {
                callback: function () {
                    comboStore.insert(0, { name: '全部', id: null });
                }
            };
        }
        comboStore.load(options);
        return comboStore;
    },

    /**
     * 在提交表单前对表单进行栓正，不通过则弹出题示信息
     * @param formId {String/Object} 表单对象或id
     * @param callback {Function} 回调函数
     * @param msg {String} 题示内容
     */
    doFormCheck: function (formId, callback, msg) {
        msg = msg || '请在表单中输入正确的信息';
        var form = formId.isComponent ? formId : Ext.getCmp(formId);
        if (formId.isValid()) {
            callback && callback();
        } else {
            Ext.MessageBox.show({
                title: '警告',
                msg: msg,
                buttons: Ext.MessageBox.OK,
                icon: 'x-message-box-error'
            });
        }
    },
    /**
     * 公共查询
     * @param gridId {String} 表格id
     * @param formId {String} 表单id
     * @param isRetSel {boolean} 是否取消之前gird中的选择项
     */
    doSearch: function (gridId, formId, isRetSel) {
        var store = Ext.getCmp(gridId).getStore();
        store.getProxy().extraParams = Ext.getCmp(formId).getValues()
        store.loadPage(1);
        isRetSel && this.removeGridSel(gridId);
    }
};


Ext.Ajax.on('requestexception', function (conn, response, options) {

    var data, msg;
    if (!!response.responseText) {
        data = Ext.decode(response.responseText);
    }
    if (response.status == "999") {
        Ext.Msg.alert('提示', '会话超时，请重新登录!', function () {
            window.location.href = '/login.html';
        });
    } else if (response.status == "998") {
        msg = data.msg || '您没有权限!';
        Ext.Msg.alert('提示', msg);
    } else if (response.status == "997") {
        msg = data.msg || '服务器错误!';
        Ext.Msg.alert('提示', msg);
    }
});


/**
 * 分页插件设置第页数量扩展
 * @author: Mask
 * @last modify: 2012-11-03
 * @memo: a plugin for setting the 'pageSize' of a paging toolbar
 */
Ext.define('Ext.ux.PageSizePlugin', {
    alias: 'plugin.pagesizeplugin',
    maximumSize: 1001,
    beforeText: '每页显示',
    afterText: '条记录',
    limitWarning: '不能超出设置的最大分页数：',
    constructor: function (config) {
        var me = this;
        Ext.apply(me, config);
    },
    init: function (paging) {
        var me = this;
        me.combo = me.getPageSizeCombo(paging);
        paging.add(' ', me.beforeText, me.combo, me.afterText, ' ');
        me.combo.on('select', me.onChangePageSize, me);
        me.combo.on('keypress', me.onKeyPress, me);
    },
    getPageSizeCombo: function (paging) {
        var me = this,
            defaultValue = paging.pageSize || paging.store.pageSize || 25;
        return Ext.create('Ext.form.field.ComboBox', {
            store: new Ext.data.SimpleStore({
                fields: ['text', 'value'],
                data: me.sizeList || [
                    ['50', 50],
                    ['100', 100],
                    ['300', 300],
                    ['500', 500],
                    ['1000', 1000]
                ]
            }),
            mode: 'local',
            displayField: 'text',
            valueField: 'value',
            allowBlank: true,
            triggerAction: 'all',
            width: 50,
            maskRe: /[0-9]/,
            enableKeyEvents: true,
            value: defaultValue
        });
    },
    onChangePageSize: function (combo) {
        var paging = combo.up('standardpaging') || combo.up('pagingtoolbar'),
            store = paging.store,
            comboSize = combo.getValue();
        store.pageSize = comboSize;
        store.loadPage(1);
    },
    onKeyPress: function (field, e) {
        if (Ext.isEmpty(field.getValue())) {
            return;
        }
        var me = this,
            fieldValue = field.getValue(),
            paging = me.combo.up('standardpaging') || me.combo.up('pagingtoolbar'),
            store = paging.store;
        if (e.getKey() == e.ENTER) {
            if (fieldValue < me.maximumSize) {
                store.pageSize = fieldValue;
                store.loadPage(1);
            } else {
                Ext.MessageBox.alert('提示', me.limitWarning + me.maximumSize);
                field.setValue('');
            }
        }
    },
    destory: function () {
        var me = this;
        me.combo.clearListeners();
        Ext.destroy(me.combo);
        delete me.combo;
    }
});