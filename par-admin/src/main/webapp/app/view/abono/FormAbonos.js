Ext.define('Paranimf.view.abono.FormAbonos', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formAbonos',
   
   url : urlPrefix + 'abono',
   
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
    }, {
        fieldLabel: UI.i18n.field.nameMulti,
        name: 'nombre'
    }, {
      fieldLabel: UI.i18n.field.plantillapreciosSala,
      name: 'plantillaPrecios',
      xtype: 'combobox',
      forceSelection: true,
      displayField: 'nombre',
      valueField: 'id',
      store: 'PlantillasPreciosCombo',
      tpl: '<tpl for="."><div class="x-boundlist-item" >{nombre} ({nombreSala})</div></tpl>',
      queryMode: 'local',
      typeAhead: true,
      allowBlank: false
    }, {
      xtype: 'gridPreciosAbono'
    }, {
      xtype: 'gridSesionesAbonos',
      style : 'margin-top: 1em;'
    }, {
      xtype: 'hiddenfield',
      name: 'sesiones'
    }]
});