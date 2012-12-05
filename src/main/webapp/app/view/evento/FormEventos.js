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

  buttons: [{
    xtype: 'button',
    text: UI.i18n.button.save,
    action: 'save'
  }, {
    xtype: 'button',
    text: UI.i18n.button.cancel,
    handler: function() {
      this.up('window').close();
    }
  }, {
    xtype: 'button',
    text: UI.i18n.button.eliminarImagen,
    action: 'deleteImage'
  }],

   items: [{
      name: 'id',
      hidden: true,
      allowBlank: true
   }, {
    xtype: 'fieldset',
     flex: 1,
    title: UI.i18n.field.tituloMulti,
     defaults: {
         xtype: 'textfield',
         anchor: '100%'
     },
    items: [{
      fieldLabel: UI.i18n.field.title,
      name: 'tituloEs'
      }, {
        fieldLabel: UI.i18n.field.title_va,
        name: 'tituloVa',
      }]
   }, 
   
   {
     xtype: 'fieldset',
     flex: 1,
     title: UI.i18n.field.descripcionMulti,
     defaults: {
         xtype: 'textarea',
         anchor: '100%'
     },
     items: [{
         fieldLabel: UI.i18n.field.description,
         name: 'descripcionEs',
         allowBlank: true
      }, {
         fieldLabel: UI.i18n.field.description_va,
         name: 'descripcionVa',
         allowBlank: true
      }]
   },
   
   
   {
     xtype: 'fieldset',
     flex: 1,
     title: UI.i18n.field.companyMulti,
     defaults: {
         xtype: 'textfield',
         anchor: '100%'
     },
     items: [{
         fieldLabel: UI.i18n.field.company,
         name: 'companyiaEs',
         allowBlank: true
      }, {
       fieldLabel: UI.i18n.field.company_va,
       name: 'companyiaVa',
       allowBlank: true
      }]
   },
   
   
   {
     xtype: 'fieldset',
     flex: 1,
     title: UI.i18n.field.staffMulti,
     defaults: {
         xtype: 'textfield',
         anchor: '100%'
     },
     items: [{
       fieldLabel: UI.i18n.field.staff,
       name: 'interpretesEs',
         allowBlank: true
      }, {
       fieldLabel: UI.i18n.field.staff_va,
       name: 'interpretesVa',
         allowBlank: true
      }]
   },
   
   
   {
     xtype: 'fieldset',
     flex: 1,
     title: UI.i18n.field.duracionMulti,
     defaults: {
         xtype: 'textfield',
         anchor: '100%'
     },
     items: [{
       fieldLabel: UI.i18n.field.duration,
       name: 'duracionEs',
         allowBlank: true
      }, {
       fieldLabel: UI.i18n.field.duration_va,
       name: 'duracionVa',
         allowBlank: true
      }]
   },
      
   
   {
     xtype: 'fieldset',
     flex: 1,
     title: UI.i18n.field.awardsMulti,
     defaults: {
         xtype: 'textfield',
         anchor: '100%'
     },
     items: [{
       fieldLabel: UI.i18n.field.awards,
       name: 'premiosEs',
         allowBlank: true
      }, {
       fieldLabel: UI.i18n.field.awards_va,
       name: 'premiosVa',
         allowBlank: true
      }]
   },
   
   
   {
     xtype: 'fieldset',
     flex: 1,
     title: UI.i18n.field.characteristicsMulti,
     defaults: {
         xtype: 'textfield',
         anchor: '100%'
     },
     items: [{
       fieldLabel: UI.i18n.field.characteristics,
       name: 'caracteristicasEs',
         allowBlank: true
      }, {
       fieldLabel: UI.i18n.field.characteristics_va,
       name: 'caracteristicasVa',
         allowBlank: true
      }]
   },
   
   
   {
     xtype: 'fieldset',
     flex: 1,
     title: UI.i18n.field.commentsMulti,
     defaults: {
         xtype: 'textfield',
         anchor: '100%'
     },
     items: [{
       fieldLabel: UI.i18n.field.comments,
       name: 'comentariosEs',
         allowBlank: true
      }, {
       fieldLabel: UI.i18n.field.comments_va,
       name: 'comentariosVa',
       allowBlank: true
      }]
   },
     
  {
    fieldLabel: UI.i18n.field.type,
    name: 'tipoEvento',
    xtype: 'combobox',
    displayField: 'nombreEs',
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
          id: 'imagenInsertada'
      }
    ]
  }]
});