Ext.define('Paranimf.view.abono.FormAbonados', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formAbonados',
   
   url : urlPrefix + 'abonado',
   
   defaults: {
      allowBlank: false,
      msgTarget: 'side',
      labelWidth: 180,
      anchor: '100%',
      xtype: 'textfield',
      flex: 1
   },

    items: [{
        name: 'id',
        hidden: true,
        allowBlank: true
      },{
        fieldLabel : UI.i18n.field.nameMulti,
        name : 'nombre',
        xtype : 'textfield',
        allowBlank : false
      }, {
        fieldLabel : UI.i18n.field.surnameMulti,
        name : 'apellidos',
        xtype : 'textfield',
        allowBlank : false
      }, {
        fieldLabel : UI.i18n.field.address,
        name : 'direccion',
        xtype : 'textfield',
        allowBlank : false
      }, {
        fieldLabel : UI.i18n.field.poblacion,
        name : 'poblacion',
        xtype : 'textfield',
        allowBlank : false
      }, {
        fieldLabel : UI.i18n.field.codigoPostal,
        name : 'cp',
        xtype : 'textfield',
        allowBlank : false
      }, {
        fieldLabel : UI.i18n.field.provincia,
        name : 'provincia',
        xtype : 'textfield',
        allowBlank : true
      }, {
        fieldLabel : UI.i18n.field.phone,
        name : 'telefono',
        xtype : 'textfield',
        allowBlank : false
      }, {
        fieldLabel : UI.i18n.field.email,
        name : 'email',
        allowBlank : true,
        xtype : 'textfield'
      }, {
        xtype: 'checkbox',
        fieldLabel : UI.i18n.field.infoPeriodica,
        labelWidth : 220,
        inputValue: 1,
        name : 'infoPeriodica'
      }]
});