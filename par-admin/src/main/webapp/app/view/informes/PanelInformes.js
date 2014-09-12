Ext.define('Paranimf.view.informes.PanelInformes', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelInformes',
  frame: false,
  border: false,
  layout: 'border',

  items: [{
    border: false,
    region: 'north',
    autoScroll: false,
    xtype: 'panel',
    height: 70,
    defaults: {
      border: 0,
      xtype: 'panel',
      layout: 'hbox',
      margin: '0px 0px 10px 0px'
    },
    items: [{
      items: [{
        xtype: 'datefield',
        name: 'fechaInicio',
        startDay: 1,
        fieldLabel: UI.i18n.field.startDate,
        margin: '0px 10px 0px 0px'
      }, {
        xtype: 'datefield',
        name: 'fechaFin',
        startDay: 1,
        fieldLabel: UI.i18n.field.endDate,
        margin: '0px 10px 0px 0px'
      }, {
        xtype: 'button',
        action: 'filtrarSessions',
        text: UI.i18n.button.filtrarSessions
      }], 
    }, {
      items: [{
        border: 0,
        html: UI.i18n.message.informesGenerals,
        margin: '0px 10px 0px 0px'
      }, {
        xtype: 'button',
        action: 'generateExcelTaquilla',
        text: UI.i18n.button.generateExcelTaquilla
      }, {
        style: 'margin-left: 10px',
        xtype: 'button',
        action: 'generateExcelEvento',
        text: UI.i18n.button.generateExcelEvento
      }, {
        style: 'margin-left: 10px',
        xtype: 'button',
        action: 'generatePdfTaquilla',
        text: UI.i18n.button.generatePdfTaquilla
      }, {
        style: 'margin-left: 10px',
        xtype: 'button',
        action: 'generatePdfEfectivo',
        text: UI.i18n.button.generatePdfEfectivo
      }, {
        style: 'margin-left: 10px',
        xtype: 'button',
        action: 'generatePdfTpv',
        text: UI.i18n.button.generatePdfTpv
      }, {
        style: 'margin-left: 10px',
        xtype: 'button',
        action: 'generatePdfEventos',
        text: UI.i18n.button.generatePdfEventos
      }]
    }
  ]}, {
      region: 'center',
      xtype: 'gridSesionesInformes'
  }]
});