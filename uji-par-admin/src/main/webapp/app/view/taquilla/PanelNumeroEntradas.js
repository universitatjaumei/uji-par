Ext.define('Paranimf.view.taquilla.PanelNumeroEntradas', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelNumeroEntradas',
  autoScroll: true,
  border: 0,
  frame: false,
  padding: '5 5 5 5',
  height: 200,
  
  defaults: {
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
    name: 'panelTarifas',
    autoScroll: true,
    height: 70,
    border: 0,
    layout: 'column',
    items: []
  }]
});