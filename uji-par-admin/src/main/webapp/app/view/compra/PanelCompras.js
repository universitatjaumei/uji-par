Ext.define('Paranimf.view.compra.PanelCompras', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelCompras',
  /*border: false,
  frame: false,*/
  layout: 'border',
  //autoScroll: false,

  buttons: [{
    xtype: 'button',
    text: UI.i18n.button.close,
    handler: function() {
      this.up('window').close();
    }
  }],

  items: [{
    region: 'north',
      xtype: 'gridCompras',
      collapsible: true,
      minHeight: 100,
      split: true,
      header: true,
      flex: 2
    }, {
      region: 'center',
      xtype: 'gridDetalleCompras',
      minHeight: 100,
      split: true,
      flex: 1
    }]
});