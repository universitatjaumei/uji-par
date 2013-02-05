Ext.define('Paranimf.view.evento.PanelEventos', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelEventos',
  autoScroll: true,

  items: [{
    xtype: 'gridEventos'
  }, {
    xtype: 'gridSesiones',
    style : 'margin-top: 1em;'
  }]
});