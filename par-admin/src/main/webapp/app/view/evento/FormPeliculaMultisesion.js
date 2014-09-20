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
   }]
});