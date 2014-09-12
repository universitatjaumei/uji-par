Ext.define('Paranimf.view.tarifa.PanelTarifas', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelTarifas',
  layout: 'border',
  border: false,

  items: [{
    region: 'center',
    autoScroll: true,
    xtype: 'gridTarifas'
  }]
});