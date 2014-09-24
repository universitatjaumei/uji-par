Ext.define('Paranimf.view.evento.FormPeliculaMultisesion', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formPeliculaMultisesion',
   
   defaults: {
      allowBlank: false,
      msgTarget: 'side',
      labelWidth: 90,
      anchor: '100%',
      flex: 1
   },

   items: [{
      fieldLabel: UI.i18n.field.selectPelicula,
      name: 'peliculas',
      xtype: 'combobox',
      forceSelection: true,
      displayField: 'tituloEs',
      valueField: 'id',
      store: 'Peliculas'
   }, {
      fieldLabel: UI.i18n.field.versionLinguistica,
      name: 'versionLinguistica',
      xtype: 'combobox',
      displayField: 'name',
      valueField : 'value',
      queryMode: 'local',
      forceSelection:true,
      store: new Ext.data.SimpleStore({
        fields: ['value', 'name'],
          data: [
            ['S', UI.i18n.versionLinguistica.S],
            ['B', UI.i18n.versionLinguistica.B],
            ['R', UI.i18n.versionLinguistica.R],
            ['Q', UI.i18n.versionLinguistica.Q],
            ['P', UI.i18n.versionLinguistica.P],
            ['Z', UI.i18n.versionLinguistica.Z],
            ['W', UI.i18n.versionLinguistica.W],
            ['Y', UI.i18n.versionLinguistica.Y],
            ['V', UI.i18n.versionLinguistica.V],
            ['U', UI.i18n.versionLinguistica.U],
            ['T', UI.i18n.versionLinguistica.T],
            ['F', UI.i18n.versionLinguistica.F],
            ['X', UI.i18n.versionLinguistica.X]
          ]
      })
   }]
});