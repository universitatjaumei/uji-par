Ext.define('Paranimf.view.taquilla.PanelTaquilla', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelTaquilla',
  layout: 'border',
  border: false,
  frame: false,

  items: [{
    flex: 1,
    region: 'north',
    autoScroll: true,
    xtype: 'gridEventosTaquilla'
  },{
    flex: 1,
    region: 'center',
    autoScroll: true,
    xtype: 'gridSesionesTaquilla',
    style : 'margin-top: 1em;'
  }]
});