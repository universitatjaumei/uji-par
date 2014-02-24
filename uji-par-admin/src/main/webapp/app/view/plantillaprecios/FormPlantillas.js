Ext.define('Paranimf.view.plantillaprecios.FormPlantillas', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formPlantillas',

   url : urlPrefix + 'plantillaprecios',
   
   defaults: {
      allowBlank: false,
      msgTarget: 'side',
      labelWidth: 90,
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
        fieldLabel: UI.i18n.field.sala,
        name: 'sala',
        xtype: 'combobox',
        displayField: 'nombre',
        valueField: 'id',
        store: 'Salas',
        queryMode: 'local',
        typeAhead: true
   }]
});