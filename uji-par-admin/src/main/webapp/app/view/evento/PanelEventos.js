Ext.define('Paranimf.view.evento.PanelEventos', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelEventos',
  autoScroll: true,
  border: false,

  items: [{
    xtype: 'gridEventos'
  }, {
    xtype: 'gridSesiones',
    style : 'margin-top: 1em;'
  }]
});