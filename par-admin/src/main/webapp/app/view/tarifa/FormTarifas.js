Ext.define('Paranimf.view.tarifa.FormTarifas', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formTarifas',
   
   url : urlPrefix + 'tarifa',
   
   defaults: {
      allowBlank: false,
      msgTarget: 'side',
      labelWidth: 120,
      anchor: '100%',
      xtype: 'textfield',
      flex: 1
   },

   items: [{
      name: 'id',
      hidden: true,
      allowBlank: true
   }, {
		fieldLabel: UI.i18n.field.name_va,
		name: 'nombre'
	}, {
      fieldLabel: UI.i18n.field.isPublico,
      xtype: 'checkbox',
      name: 'isPublico'
   }, {
      fieldLabel: UI.i18n.field.defecto,
      xtype: 'checkbox',
      name: 'defecto'
   }]
});