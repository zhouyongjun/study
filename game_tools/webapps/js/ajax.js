//�Լ�д��ajax�ύ�����磺
Ext.Ajax.request({
    method: 'POST',
    type: 'ajax',
    params: {
        userID: _form.getForm().findField('UserID').getValue(),
        pwd: _form.getForm().findField('Pwd').getValue()
    },
    url: 'XXX/Login', // ����ķ����ַ
    success: function (response) {
        var rs = Ext.JSON.decode(response.responseText);
        if (rs.success == false) { // ����ʧ��
            if (rs.msg) {
                Ext.Msg.alert('ϵͳ��ʾ', rs.msg);
            } else {
                Ext.Msg.alert('ϵͳ��ʾ', '����ʧ��');
            }
        }
        else { // �����ɹ�
             
        }
        me.setLoading(false);
    },
    failure: function (response) {
        Ext.Msg.alert('ϵͳ��ʾ', '����ʧ��');
        me.setLoading(false);
    }
});