Ext.define('Paranimf.view.compra.PanelCompras', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelCompras',
  autoScroll: true,
  border: false,
  frame: false,

  bbar: ['->',
  {
	   name: 'verComprasCerrar',
	   text: UI.i18n.button.close
  }],
  
  items: [{
    xtype: 'gridCompras'
  }]
});