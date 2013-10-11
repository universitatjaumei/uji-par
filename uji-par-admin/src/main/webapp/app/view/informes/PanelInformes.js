Ext.define('Paranimf.view.informes.PanelInformes', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelInformes',
  frame: false,
  border: false,
  layout: 'border',

  items: [{
    border: false,
    flex: 1,
    region: 'center',
    autoScroll: true,
    xtype: 'panel',
    items: [{
      xtype: 'datefield',
      name: 'fechaInicio',
      startDay: 1,
      fieldLabel: UI.i18n.field.startDate
    }, {
      xtype: 'datefield',
      name: 'fechaFin',
      startDay: 1,
      fieldLabel: UI.i18n.field.endDate
    }, {
      xtype: 'button',
      action: 'generateExcelTaquilla',
      text: UI.i18n.button.generateExcelTaquilla
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
      action: 'generateExcelEvento',
      text: UI.i18n.button.generateExcelEvento
    }]
  }]
});