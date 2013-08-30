Ext.define('Paranimf.controller.ComprasReservas', {
  extend: 'Ext.app.Controller',

  views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'compra.PanelComprasReservas', 'compra.GridEventosComprasReservas', 'compra.GridSesionesComprasReservas', 'compra.PanelCompras', 'compra.GridCompras', 'compra.GridDetalleCompras'],
  stores: ['Compras', 'EventosTaquillaAll', 'SesionesTaquillaAll'],
  models: ['Compra', 'Evento', 'Sesion'],

  refs: [
    {
      ref: 'gridCompras',
      selector: 'gridCompras'
    },      
    {
     	ref: 'panelCompras',
      selector: 'panelCompras'
    },
    {
  	  ref: 'botonVerCompras',
      selector: 'formComprar #verCompras'
    },
    {
      ref: 'gridEventosComprasReservas',
      selector: 'gridEventosComprasReservas'
    },
    {
      ref: 'gridSesionesComprasReservas',
      selector: 'gridSesionesComprasReservas'
    }
  ],

  init: function() {
    this.control({
      'panelComprasReservas': {
        afterrender: this.loadEventos
      },

      'gridEventosComprasReservas': {
        selectionchange: this.loadSesiones
      },
      
      'gridSesionesComprasReservas button[action=verCompras]': {
        click: this.verCompras
      },

      'panelCompras': {
        afterrender: this.loadCompras
      },                     

      'panelCompras button[action=close]': {
        click: this.cerrarVerCompras
      }
    });     
  },

  verCompras: function(button, event, opts) {
    if (this.getGridEventosComprasReservas().hasRowSelected() && this.getGridSesionesComprasReservas().hasRowSelected()) {
      var evento = this.getGridEventosComprasReservas().getSelectedRecord();
      var sesion = this.getGridSesionesComprasReservas().getSelectedRecord();
      this.getGridSesionesComprasReservas().showVerComprasWindow();
    } else
      alert(UI.i18n.message.selectRow);
  },

  loadEventos: function() {
    if (this.getGridEventosComprasReservas().getStore().count() == 0)
      this.getGridEventosComprasReservas().getStore().load();
  },

  loadSesiones: function(selectionModel, record) {
    if (record[0]) {
      var storeSesiones = this.getGridSesionesComprasReservas().getStore();
      var eventoId = record[0].get('id');

      storeSesiones.getProxy().url = urlPrefix + 'evento/' + eventoId + '/sesiones';
      storeSesiones.load();
    }
  },

  loadCompras: function() {
    var storeCompras = this.getGridCompras().getStore();
    var sesion = this.getGridSesionesComprasReservas().getSelectedRecord();

    storeCompras.getProxy().url = urlPrefix + 'compra/' + sesion.data['id'];
    storeCompras.load();
  },

  cerrarVerCompras: function() {
    this.getPanelCompras().up('window').close();  
  }
});