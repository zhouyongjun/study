//自己写个ajax提交，比如：
Ext.Ajax.request({
    method: 'POST',
    type: 'ajax',
    params: {
        userID: _form.getForm().findField('UserID').getValue(),
        pwd: _form.getForm().findField('Pwd').getValue()
    },
    url: 'XXX/Login', // 请求的服务地址
    success: function (response) {
        var rs = Ext.JSON.decode(response.responseText);
        if (rs.success == false) { // 操作失败
            if (rs.msg) {
                Ext.Msg.alert('系统提示', rs.msg);
            } else {
                Ext.Msg.alert('系统提示', '操作失败');
            }
        }
        else { // 操作成功
             
        }
        me.setLoading(false);
    },
    failure: function (response) {
        Ext.Msg.alert('系统提示', '操作失败');
        me.setLoading(false);
    }
});