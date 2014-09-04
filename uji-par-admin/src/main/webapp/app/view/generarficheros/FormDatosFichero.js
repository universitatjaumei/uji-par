Ext.define('Paranimf.view.generarficheros.FormDatosFichero', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formDatosFichero',
   
   defaults: {
      allowBlank: false,
      msgTarget: 'side',
      labelWidth: 210,
      anchor: '100%'
   },

   items: [{
      fieldLabel: UI.i18n.field.fechaUltimoEnvioHabitual,
      xtype: 'datefield',
      name: 'fechaUltimoEnvioHabitual',
      maxValue: new Date()
   }, {
      xtype: 'combobox',
      forceSelection: true,
      queryMode: 'local',
      name: 'tipoEnvio',
      valueField: 'id',
      displayField: 'label',
      fieldLabel: UI.i18n.field.tipoEnvio,
      store: 'TipoEnvio'
   }]
});