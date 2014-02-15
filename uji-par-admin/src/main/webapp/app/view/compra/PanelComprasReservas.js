Ext.define('Paranimf.view.compra.PanelComprasReservas', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelComprasReservas',
  border: false,
  frame: false,
  layout: 'border',

  items: [{
    flex: 1,
    region: 'north',
    autoScroll: true,
    xtype: 'gridEventosComprasReservas',
    split: true,
    collapsible: true
  },{
    region: 'center',
    flex: 1,
    autoScroll: true,
	  xtype: 'gridSesionesComprasReservas',
	  style : 'margin-top: 1em;'
  }]
});