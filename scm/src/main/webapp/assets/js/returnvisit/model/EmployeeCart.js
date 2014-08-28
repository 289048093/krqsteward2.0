Ext.define('Returnvisit.model.EmployeeCart',
    {
        //不要忘了继承
        extend:'Ext.data.Model',
        fields: [
            'employeeId',
            'username',
            'employeeName',
            'apportionCount',
        ],
        idProperty: 'id'
    }
)
