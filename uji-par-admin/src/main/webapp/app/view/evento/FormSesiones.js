Ext.define('Paranimf.view.evento.FormSesiones', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formSesiones',

   url : urlPrefix + 'sesiones',
   
   defaults: {
      anchor: '100%',
      xtype: 'textfield',
      labelWidth: 190,
   },

   items: [{
      name: 'id',
      hidden: true,
      allowBlank: true
   }, {
      name: 'preciosSesion',
      hidden: true,
      allowBlank: true
   }, {
      xtype: 'fieldset',
      title: UI.i18n.field.dadesGenerals,
      defaults: {
         anchor: '100%',
         labelWidth: 190,
         allowBlank: false
      },
      items: [{
         fieldLabel: UI.i18n.field.eventDate,
         name: 'fechaCelebracion',
         xtype: 'datefield',
         startDay: 1,
         value: new Date()
      }, {
         name: 'horaCelebracion',
         xtype: 'timefield',
         fieldLabel: UI.i18n.field.sessionTime,
         minValue: '8:00 AM',
         maxValue: '23:30 PM',
         format: 'H:i',
         increment: 30
      }, {
         name: 'horaApertura',
         xtype: 'timefield',
         fieldLabel: UI.i18n.field.opening,
         minValue: '8:00 AM',
         maxValue: '23:30 PM',
         format: 'H:i',
         increment: 30,
         allowBlank: true
      }]
   }, {
      xtype: 'fieldset',
      title: UI.i18n.field.reservesOnline,
      defaults: {
         anchor: '100%',
         labelWidth: 190,
         allowBlank: false
      },
      items: [{
         fieldLabel: UI.i18n.field.startOnlineSelling,
         name: 'fechaInicioVentaOnline',
         xtype: 'datefield',
         startDay: 1,
         value: new Date()
      }, {
         name: 'horaInicioVentaOnline',
         xtype: 'timefield',
         fieldLabel: UI.i18n.field.horaInicioVentaOnline,
         minValue: '0:00 AM',
         maxValue: '23:00 PM',
         format: 'H:i'
      }, {
         fieldLabel: UI.i18n.field.endOnlineSelling,
         name: 'fechaFinVentaOnline',
         xtype: 'datefield',
         startDay: 1,
         value: new Date()
      }, {
         name: 'horaFinVentaOnline',
         xtype: 'timefield',
         fieldLabel: UI.i18n.field.horaFinVentaOnline,
         minValue: '0:00 AM',
         maxValue: '23:00 PM',
         format: 'H:i',
         increment: 60
      }]
   }, {
      fieldLabel: UI.i18n.field.plantillaprecios,
      name: 'plantillaPrecios',
      xtype: 'combobox',
      displayField: 'nombre',
      valueField: 'id',
      store: 'PlantillasPrecios',
      queryMode: 'local',
      typeAhead: true,
      allowBlank: false
   }, {
      xtype: 'gridPreciosSesion'
   }],
    
    areDatesValid: function() {
        var dateIni = this.getForm().findField('fechaInicioVentaOnline').getValue();
        var dateEnd = this.getForm().findField('fechaFinVentaOnline').getValue();
        
        if (dateIni.valueOf() > dateEnd.valueOf())
           return false;

        return true;
     }
});