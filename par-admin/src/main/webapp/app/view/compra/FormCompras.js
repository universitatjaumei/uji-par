Ext.define('Paranimf.view.compra.FormCompras', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formCompras',
   
   url : urlPrefix + 'compra',
   
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
		fieldLabel: UI.i18n.field.codigo,
		name: 'codigo'
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