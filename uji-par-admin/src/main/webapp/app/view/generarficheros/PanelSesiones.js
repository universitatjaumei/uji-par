Ext.define('Paranimf.view.generarficheros.PanelSesiones', {
  extend: 'Ext.panel.Panel',
  alias: 'widget.panelSesionesFicheros',
  frame: false,
  border: false,
  layout: 'border',

  items: [{
    region: 'north',
    height: 50,
    border: 0,
    xtype: 'panel',
    layout: 'hbox',
    defaults: {
      margin: '0 10px 0 0',
      labelWidth: 80
    },
    items: [{
      fieldLabel: UI.i18n.field.startDate,
      name: 'fechaInicio',
      xtype: 'datefield',
      startDay: 1,
      value: new Date()
    }, {
      fieldLabel: UI.i18n.field.endDate,
      name: 'fechaFin',
      xtype: 'datefield',
      startDay: 1,
      value: new Date(),
      maxValue: new Date()
    }, {
      xtype: 'button',
      name: 'filtrar',
      text: UI.i18n.button.buscar
    }]
  }, {
    flex: 1,
    region: 'center',
    autoScroll: true,
    xtype: 'gridSesionesCompleto'
  }]
});