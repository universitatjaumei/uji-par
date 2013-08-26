Ext.define('Paranimf.controller.Compras', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'compra.GridCompras', 'compra.FormCompras', 'compra.PanelCompras'],
   stores: ['Compras'],
   models: ['Compra'],

   refs: [{
	  ref: 'panelCompras',
	  selector: 'panelCompras'
   }, {
      ref: 'gridCompras',
      selector: 'gridCompras'
   }, {
      ref: 'formCompras',
      selector: 'formCompras'
   }],

   init: function() {
      this.control({

         'gridCompras button[action=add]': {
            click: this.addCompra
         },

         'gridCompras button[action=edit]': {
            click: this.editCompra
         },

         'gridCompras button[action=del]': {
            click: this.removeCompra
         },

         'panelCompras': {
			   beforeactivate: this.recargaStore
         },

         'formCompras button[action=save]': {
            click: this.saveComprasFormData
         }
      });
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE COMPRAS");
      this.getGridCompras().recargaStore();
   },

   addCompra: function(button, event, opts) {
      this.getGridCompras().showAddCompraWindow();
   },

   editCompra: function(button, event, opts) {
      this.getGridCompras().edit('formCompras');
   },

   removeCompra: function(button, event, opts) {
      this.getGridCompras().remove();
   },

   saveComprasFormData: function(button, event, opts) {
      var grid = this.getGridCompras();
      var form = this.getFormCompras();
      form.saveFormData(grid, urlPrefix + 'compra');
   }
});