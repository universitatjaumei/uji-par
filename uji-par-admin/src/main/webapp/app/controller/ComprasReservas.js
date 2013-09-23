Ext.define('Paranimf.controller.ComprasReservas', {
  extend: 'Ext.app.Controller',

  views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'compra.PanelComprasReservas', 'compra.GridEventosComprasReservas', 'compra.GridSesionesComprasReservas', 'compra.PanelCompras', 'compra.GridCompras', 'compra.GridDetalleCompras'],
  stores: ['Compras', 'EventosTaquillaAll', 'SesionesTaquillaAll', 'ButacasCompra'],
  models: ['Compra', 'Evento', 'Sesion', 'Butaca'],

  refs: [
    {
      ref: 'gridCompras',
      selector: 'gridCompras'
    },
    {
      ref: 'gridDetalleCompras',
      selector: 'gridDetalleCompras'
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
    }, {
      ref: 'checkboxAnuladas',
      selector: 'gridCompras checkbox[action=showAnuladas]'
    }, {
      ref: 'checkboxOnline',
      selector: 'gridCompras checkbox[action=showOnline]'
    }, {
      ref: 'pagingToolbar',
      selector: 'gridCompras pagingtoolbar'
    }, {
      ref: 'buscadorCompras',
      selector: 'panelCompras textfield[name=buscadorCompras]'
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
      },

      'gridCompras button[action=anular]': {
        click: this.anularCompraReserva
      },

      'gridCompras': {
        selectionchange: this.loadButacas
      },

      'gridCompras checkbox[action=showAnuladas]': {
        change: this.showAnuladas
      },

      'gridCompras checkbox[action=showOnline]': {
        change: this.showOnline
      },

      'gridCompras pagingtoolbar': {
        beforechange: this.doRefresh
      },

      'gridDetalleCompras button[action=anular]': {
        click: this.anularButaca
      },

      'panelCompras button[action=search]': {
        click: this.buscarCompra
      }
    });     
  },

  buscarCompra: function() {
    var idSesion = this.getGridSesionesComprasReservas().getSelectedColumnId();
    this.getGridCompras().store.getProxy().url = urlPrefix + 'compra/' + idSesion + '?search=' + this.getBuscadorCompras().value,
    this.getGridCompras().store.load(function(records, operation, success) {

    })
  },

  doRefresh: function() {
    this.setStoreCompras();
  },

  showAnuladas: function(checkbox, newValue, oldValue) {
    this.setStoreCompras();
    console.log("showAnuladas");
    this.getPagingToolbar().moveFirst();
  },

  showOnline: function(checkbox, newValue, oldValue) {
    this.setStoreCompras();
    console.log("showOnline");
    this.getPagingToolbar().moveFirst();
  },

  anularCompraReserva: function() {
    if (!this.getGridCompras().hasRowSelected())
      alert(UI.i18n.message.selectRow);
    else {
      if (confirm(UI.i18n.message.sureAnular)) {
        var idSesion = this.getGridSesionesComprasReservas().getSelectedColumnId();
        var idCompra = this.getGridCompras().getSelectedColumnId();
        var me = this;
        this.getGridCompras().setLoading(UI.i18n.message.loading);

        Ext.Ajax.request({
          url : urlPrefix + 'compra/' + idSesion + '/' + idCompra,
          method: 'PUT',
          success: function (response) {
            me.setStoreCompras();
            me.getGridCompras().setLoading(false);
            me.getGridCompras().deseleccionar();
            me.getGridCompras().getStore().load();
          }, failure: function (response) {
            me.getGridCompras().setLoading(false);
            alert(UI.i18n.error.anularCompraReserva);
          }
        });
      }
    }
  },

  anularButaca: function() {
    if (!this.getGridDetalleCompras().hasRowSelected())
      alert(UI.i18n.message.selectRow);
    else {
      if (confirm(UI.i18n.message.sureAnular)) {
        var idSesion = this.getGridSesionesComprasReservas().getSelectedColumnId();
        var idCompra = this.getGridCompras().getSelectedColumnId();
        var idButaca = this.getGridDetalleCompras().getSelectedColumnId();
        var me = this;
        this.getGridDetalleCompras().setLoading(UI.i18n.message.loading);

        Ext.Ajax.request({
          url : urlPrefix + 'compra/' + idSesion + '/' + idCompra + '/' + idButaca,
          method: 'PUT',
          success: function (response) {
            me.getGridDetalleCompras().setLoading(false);
            me.getGridDetalleCompras().deseleccionar();
            me.getGridDetalleCompras().getStore().load();
          }, failure: function (response) {
            me.getGridDetalleCompras().setLoading(false);
            alert(UI.i18n.error.anularEntrada);
          }
        });
      }
    }
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
      
      this.getGridSesionesComprasReservas().setTitle(UI.i18n.gridTitle.sesionesCompras + ': ' + record[0].get('tituloVa'));

      storeSesiones.getProxy().url = urlPrefix + 'evento/' + eventoId + '/sesiones';
      storeSesiones.load();
    }
  },

  setStoreCompras: function() {
    var storeCompras = this.getGridCompras().getStore();
    var me = this;
    storeCompras.getProxy().url = urlPrefix + 'compra/' + this.getGridSesionesComprasReservas().getSelectedColumnId();
    storeCompras.getProxy().setExtraParam("showAnuladas", (this.getCheckboxAnuladas().value)?1:0);
    storeCompras.getProxy().setExtraParam("showOnline", (this.getCheckboxOnline().value)?1:0);
  },

  loadCompras: function() {
    this.getGridDetalleCompras().getStore().removeAll();
    this.setStoreCompras();
    this.getGridCompras().store.load();
  },

  loadButacas: function(selectionModel, record) {
    if (record[0]) {
      var storeButacas = this.getGridDetalleCompras().getStore();
      var compraid = record[0].get('id');

      storeButacas.getProxy().url = urlPrefix + 'compra/' + compraid + '/butacas';
      storeButacas.load();
    }
  }
});