Ext.define('Paranimf.view.compra.FormCambiaButaca', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formCambiaButaca',
   
   url : urlPrefix + 'compra/edita/butaca',
   
   defaults: {
      msgTarget: 'side',
      labelWidth: 120,
      anchor: '100%',
      readOnly: true,
      xtype: 'textfield',
      flex: 1
   },

   items: [{
      name: 'id',
      hidden: true,
      allowBlank: true
    }, {
      fieldLabel: UI.i18n.field.localizacion,
      name: 'localizacionNombre'
    }, {
      fieldLabel: UI.i18n.field.tipusEntrada,
      name: 'tipo'      
    }, {
      fieldLabel: UI.i18n.field.fila,
      name: 'fila'
    }, {
      fieldLabel: UI.i18n.field.numeroSeient,
      name: 'numero'
    }, {
        allowBlank: false,
        readOnly: false,
        fieldLabel: UI.i18n.field.butacaLibre,
        name: 'butaca',
        xtype: 'combobox',
        forceSelection: false,
        displayField: 'texto',
        valueField: 'uuid',
        store: 'ButacasLibres',
        queryMode: 'remote',
        typeAhead: true
   }]
});