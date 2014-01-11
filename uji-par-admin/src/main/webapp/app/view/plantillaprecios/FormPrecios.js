Ext.define('Paranimf.view.plantillaprecios.FormPrecios', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formPrecios',

   defaults: {
      allowBlank: false,
      msgTarget: 'side',
      labelWidth: 160,
      anchor: '100%',
      xtype: 'textfield'
   },

   items: [{
    name: 'id',
    hidden: true,
    allowBlank: true
   }, {
    fieldLabel: UI.i18n.field.plantillaprecios,
    name: 'plantillaPrecios',
    xtype: 'combobox',
    displayField: 'nombre',
    valueField: 'id',
    store: 'PlantillasPrecios',
    readOnly: true,
    queryMode: 'local',
    typeAhead: true
   }, {
    fieldLabel: UI.i18n.field.localizacion,
    name: 'localizacion',
    xtype: 'combobox',
    displayField: 'nombreVa',
    valueField: 'id',
    store: 'Localizaciones',
    disabled: true,
    queryMode: 'local',
    typeAhead: true
   }, {
    fieldLabel: UI.i18n.field.precio,
    name: 'precio',
    spinUpEnabled: false,
    spinDownEnabled: false,
    minValue: 0,
    xtype: 'numericfield',
    decimalPrecision: 2,
    alwaysDisplayDecimals: true
   },{
    fieldLabel: UI.i18n.field.descuento,
    name: 'descuento',
    spinUpEnabled: false,
    spinDownEnabled: false,
    minValue: 0,
    xtype: 'numericfield',
    decimalPrecision: 2,
    alwaysDisplayDecimals: true
   },{
    fieldLabel: UI.i18n.field.invitacion,
    name: 'invitacion',
    spinUpEnabled: false,
    spinDownEnabled: false,
    minValue: 0,
    xtype: 'numericfield',
    decimalPrecision: 2,
    alwaysDisplayDecimals: true
   },{
    fieldLabel: UI.i18n.field.aulaTeatro,
    name: 'aulaTeatro',
    spinUpEnabled: false,
    spinDownEnabled: false,
    minValue: 0,
    xtype: 'numericfield',
    decimalPrecision: 2,
    alwaysDisplayDecimals: true
   }]
});