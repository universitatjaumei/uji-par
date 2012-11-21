Ext.define('Paranimf.view.evento.FormEventos', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formEventos',
   fileUpload: true,
   enctype: 'multipart/form-data',

   url : urlPrefix + 'eventos',
   
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
      fieldLabel: UI.i18n.field.title,
      name: 'titulo'
   }, {
      fieldLabel: UI.i18n.field.description,
      name: 'descripcion',
      allowBlank: true
   }, {
      fieldLabel: UI.i18n.field.company,
      name: 'companyia',
      allowBlank: true
   }, {
	  fieldLabel: UI.i18n.field.staff,
	  name: 'interpretes',
      allowBlank: true
   }, {
	  fieldLabel: UI.i18n.field.duration,
	  name: 'duracion',
      allowBlank: true
   }, {
	  fieldLabel: UI.i18n.field.awards,
	  name: 'premios',
      allowBlank: true
   }, {
	  fieldLabel: UI.i18n.field.characteristics,
	  name: 'caracteristicas',
      allowBlank: true
   }, {
	  fieldLabel: UI.i18n.field.comments,
	  name: 'comentarios',
      allowBlank: true
   }, {
	   fieldLabel: UI.i18n.field.type,
	   name: 'tipoEvento',
	   xtype: 'combobox',
	   displayField: 'nombre',
	   valueField: 'id',
	   store: 'TiposEventosSinPaginar',
	   queryMode: 'local',
	   typeAhead: true
   }, {
	  xtype: 'fieldset',
	  title: UI.i18n.field.imagen,
	  items: [{
		  name: 'dataBinary',
		  anchor: '100%',
		  allowBlank: true,
		  fieldLabel: UI.i18n.field.uploadImagen,
	      labelWidth: 90,
	      msgTarget: 'side',
		  xtype: 'filefield',
		  buttonText: '...'
	  }, {
		  xtype: 'label',
		  text: UI.i18n.field.imagenInsertada
	  }, {
		  xtype: 'image',
		  id: 'imagenInsertada',
		  name: 'imagenInsertada'
	  }]
   }]
});