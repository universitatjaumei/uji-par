Ext.define('Paranimf.view.taquilla.PanelNumeroEntradas', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelNumeroEntradas',
  autoScroll: true,
  border: 0,
  frame: false,
  padding: '5 5 5 5',
  height: 160,
  
  defaults: {
    //anchor: '100%',
    labelWidth: 100
  },
   
  items: [{
    name: 'disponibles',
    xtype: 'label',
    text: UI.i18n.field.entradesDisponibles + "...",
    style: 'font-weight:bold'
  }, {
    padding: '5 0 0 0',
    xtype: 'panel',
    border: 0,
    layout: 'column',
    defaults: [{
      
    }],
    items: [{
      columnWidth: 1/2,
      name: 'normal',
      fieldLabel: UI.i18n.field.normal,
      labelWidth: 100,
      xtype: 'numberfield',
      allowDecimals: false,
      value: 0,
      minValue: 0,
      disabled: true
    }, {
      columnWidth: 1/2,
      xtype: 'panel',
      name: 'preuNormal',
      html: UI.i18n.field.precioPorEntrada + "...",
      border: false,
      minHeight: 30,
      padding: '5 0 0 5'
    },
    {
      columnWidth: 1/2,
      name: 'descuento',	    	        	   
      fieldLabel: UI.i18n.field.descuento,
      labelWidth: 100,
      xtype: 'numberfield',
      allowDecimals: false,
      value: 0,
      minValue: 0,
      disabled: true
    },
    {
      columnWidth: 1/2,
      xtype: 'panel',
      name: 'preuDescuento',
      html: UI.i18n.field.precioPorEntrada + "...",
      border: false,
      minHeight: 30,
      padding: '5 0 0 5'
    },
    {
      columnWidth: 1/2,
      name: 'invitacion',	    	        	   
      fieldLabel: UI.i18n.field.invitacion,
      labelWidth: 100,
      xtype: 'numberfield',
      allowDecimals: false,
      value: 0,
      minValue: 0,
      disabled: true
    },
    {
      columnWidth: 1/2,
      name: 'preuInvitacion',
      xtype: 'panel',
      html: UI.i18n.field.precioPorEntrada + "...",
      border: false,
      minHeight: 30,
      padding: '5 0 0 5'
    },
    {
      columnWidth: 1/2,
      name: 'aulaTeatro',	    	        	   
      fieldLabel: UI.i18n.field.aulaTeatro,
      labelWidth: 100,
      xtype: 'numberfield',
      allowDecimals: false,
      value: 0,
      minValue: 0,
      disabled: true
    },
    {
      columnWidth: 1/2,
      name: 'preuAulaTeatro',
      xtype: 'panel',
      html: UI.i18n.field.precioPorEntrada + "...",
      border: false,
      minHeight: 30,
      padding: '5 0 0 5'
    }]
  }]
});