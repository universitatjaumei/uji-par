Ext.define('Paranimf.view.taquilla.PanelTaquilla', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelTaquilla',
  autoScroll: true,
  border: false,

  items: [{
    xtype: 'gridEventosTaquilla'
  },{
	xtype: 'gridSesionesTaquilla',
	style : 'margin-top: 1em;'
  }]
});