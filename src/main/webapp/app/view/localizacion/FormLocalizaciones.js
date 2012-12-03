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
		fieldLabel: UI.i18n.field.nameMulti,
		name: 'nombre'
	}, {
		fieldLabel: UI.i18n.field.totalEntradas,
		name: 'totalEntradas'
	}]
});