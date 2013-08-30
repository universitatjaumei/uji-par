Ext.define('Paranimf.view.compra.PanelCompras', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelCompras',
  border: false,
  frame: false,
  layout: 'border',
  autoScroll: false,

  bbar: ['->',
  {
    action: 'close',
    text: UI.i18n.button.close
  }],
  
  items: [{
    flex: 2,
    autoScroll: true,
    region: 'north',
    xtype: 'gridCompras'
  }, {
    flex: 1,
    autoScroll: true,
    region: 'center',
    xtype: 'gridDetalleCompras'
  }]
});