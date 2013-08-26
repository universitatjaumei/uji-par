Ext.define('Paranimf.view.compra.PanelCompras', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelCompras',
  autoScroll: true,
  border: false,
  frame: false,

  items: [{
    xtype: 'gridCompras'
  }]
});