Ext.define('Paranimf.view.plantillaprecios.PanelPlantillasPrecios', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelPlantillas',
  autoScroll: true,

  items: [{
    xtype: 'gridPlantillas'
  }, {
    xtype: 'gridPrecios',
    style : 'margin-top: 1em;'
  }]
});