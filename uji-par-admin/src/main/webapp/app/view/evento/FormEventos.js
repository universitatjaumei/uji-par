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
         readOnly: true,
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
         readOnly: true,
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
         readOnly: true,
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
     title: UI.i18n.field.duracionMulti,
     defaults: {
         xtype: 'textfield',
         readOnly: true,
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
         readOnly: true,
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
         readOnly: true,
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
         readOnly: true,
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
    xtype: 'fieldset',
    flex: 1,
    title: UI.i18n.field.retenciones,
    defaults: {
        xtype: 'numberfield',
        anchor: '100%',
        spinUpEnabled: false,
        spinDownEnabled: false,
        minValue: 0
    },
    items: [{
      fieldLabel: UI.i18n.field.porcentajeIVA,
      name: 'porcentajeIVA',
      allowBlank: true
     }, {
      fieldLabel: UI.i18n.field.ivaSGAE,
      name: 'ivaSGAE',
      allowBlank: true
     }, {
      fieldLabel: UI.i18n.field.retencionSGAE,
      name: 'retencionSGAE',
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
    readOnly: true,
    typeAhead: true
  }, {
    fieldLabel: UI.i18n.field.asientosNumerados,
    name: 'asientosNumerados',
    readOnly: true,
    xtype: 'combobox',
    displayField: 'name',
    valueField : 'value',
    queryMode: 'local',
    allowBlank: false,
    forceSelection:true,
    store: new Ext.data.SimpleStore({
      fields: ['value', 'name'],
        data: [
          [1, UI.i18n.message.si],
          [0, UI.i18n.message.no]
        ]
    })
  }, {
    xtype: 'label',
    id: 'imagenInsertada'
  }]
});