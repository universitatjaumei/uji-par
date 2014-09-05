Ext.define('Paranimf.view.generarficheros.FormIncidencias', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formIncidencias',
   
   defaults: {
      allowBlank: false,
      msgTarget: 'side',
      labelWidth: 210,
      anchor: '100%'
   },

   items: [{
      xtype: 'combobox',
      queryMode: 'local',
      forceSelection: false,
      name: 'incidencias',
      valueField: 'id',
      displayField: 'label',
      fieldLabel: UI.i18n.field.selectIncidencia,
      store: 'Incidencias'
   }]
});