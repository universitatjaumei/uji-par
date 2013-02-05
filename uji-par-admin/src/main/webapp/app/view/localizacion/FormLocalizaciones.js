Ext.define('Paranimf.view.localizacion.FormLocalizaciones', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formLocalizaciones',
   
   url : urlPrefix + 'localizacion',
   
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
		fieldLabel: UI.i18n.field.name,
		name: 'nombreEs'
	}, {
      fieldLabel: UI.i18n.field.name_va,
      name: 'nombreVa'
   }, {
		fieldLabel: UI.i18n.field.totalEntradas,
		name: 'totalEntradas'
	}]
});