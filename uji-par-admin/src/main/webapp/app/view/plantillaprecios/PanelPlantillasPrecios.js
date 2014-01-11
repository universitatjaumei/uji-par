Ext.define('Paranimf.view.plantillaprecios.PanelPlantillasPrecios', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelPlantillas',
  layout: 'border',
  border: false,

  items: [{
    flex: 1,
    region: 'north',
    autoScroll: true,
    xtype: 'gridPlantillas',
  }, {
    flex: 2,
    region: 'center',
    autoScroll: true,
    xtype: 'gridPrecios',
    style : 'margin-top: 1em;'
  }]
});