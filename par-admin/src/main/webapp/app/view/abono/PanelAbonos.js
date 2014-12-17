Ext.define('Paranimf.view.abono.PanelAbonos', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelAbonos',
  frame: false,
  border: false,
  layout: 'border',

  items: [{
    flex: 1,
    region: 'north',
    autoScroll: true,
    xtype: 'gridAbonos',
    split: true,
    collapsible: true
  }, {
    flex: 1,
    region: 'center',
    autoScroll: true,
    xtype: 'gridAbonados',
    style : 'margin-top: 1em;'
  }]
});