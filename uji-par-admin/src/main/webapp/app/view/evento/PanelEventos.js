Ext.define('Paranimf.view.evento.PanelEventos', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelEventos',
  frame: false,
  border: false,
  layout: 'border',

  items: [{
    flex: 1,
    region: 'north',
    autoScroll: true,
    xtype: 'gridEventos'
  }, {
    flex: 1,
    region: 'center',
    autoScroll: true,
    xtype: 'gridSesiones',
    style : 'margin-top: 1em;'
  }]
});