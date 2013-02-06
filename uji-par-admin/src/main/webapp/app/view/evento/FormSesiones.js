Ext.define('Paranimf.view.evento.FormSesiones', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formSesiones',

   url : urlPrefix + 'sesiones',
   
   defaults: {
      allowBlank: true,
      msgTarget: 'side',
      labelWidth: 120,
      anchor: '100%',
      xtype: 'textfield'
   },

   items: [{
      name: 'id',
      hidden: true
   }, {
      fieldLabel: UI.i18n.field.eventDate,
      name: 'fechaCelebracion',
      xtype: 'datefield',
      startDay: 1,
      value: new Date(),
      allowBlank: false
   }, {
	  name: 'horaCelebracion',
	  xtype: 'timefield',
	  fieldLabel: UI.i18n.field.sessionTime,
	  minValue: '8:00 AM',
	  maxValue: '23:30 PM',
	  format: 'H:i',
	  increment: 30,
	  allowBlank: false
   }, {
      fieldLabel: UI.i18n.field.startOnlineSelling,
      name: 'fechaInicioVentaOnline',
      xtype: 'datefield',
      startDay: 1,
      value: new Date(),
      allowBlank: false
   }, {
      fieldLabel: UI.i18n.field.endOnlineSelling,
      name: 'fechaFinVentaOnline',
      xtype: 'datefield',
      startDay: 1,
      value: new Date(),
      allowBlank: false
   }, {
      name: 'horaAperturaPuertas',
      xtype: 'timefield',
      fieldLabel: UI.i18n.field.opening,
      minValue: '8:00 AM',
      maxValue: '23:30 PM',
      format: 'H:i',
      increment: 30
   }, {
      fieldLabel: UI.i18n.field.online,
      name: 'canalInternet',
      xtype: 'checkbox'
   }, {
      fieldLabel: UI.i18n.field.taquilla,
      name: 'canalTaquilla',
      xtype: 'checkbox'
   }],
    
    areDatesValid: function() {
        var dateIni = this.getForm().findField('fechaInicioVentaOnline').getValue();
        var dateEnd = this.getForm().findField('fechaFinVentaOnline').getValue();
        
        if (dateIni.valueOf() > dateEnd.valueOf())
           return false;

        return true;
     }
});