Ext.define('Paranimf.controller.TiposEventos', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'tipoevento.GridTiposEventos', 'tipoevento.FormTiposEventos'],
   stores: ['TiposEventos', 'SiNo'],
   models: ['TipoEvento', 'HoraMinuto'],

   refs: [{
      ref: 'gridTiposEventos',
      selector: 'gridTiposEventos'
   }, {
      ref: 'formTiposEventos',
      selector: 'formTiposEventos'
   }],

   init: function() {
      this.control({
         
         'gridTiposEventos button[action=add]': {
            click: this.addTipo
         },

         'gridTiposEventos button[action=edit]': {
            click: this.editTipo
         },

         'gridTiposEventos button[action=del]': {
            click: this.removeTipo
         },

         'gridTiposEventos': {
			beforeactivate: this.recargaStore,
			 itemdblclick: this.editTipo
         },

         'formTiposEventos button[action=save]': {
            click: this.saveTiposFormData
         }
      });
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE TIPOS EVENTOS");
      this.getGridTiposEventos().recargaStore();
   },

   addTipo: function(button, event, opts) {
      this.getGridTiposEventos().showAddTipoEventoWindow();
   },

   editTipo: function(button, event, opts) {
      this.getGridTiposEventos().edit('formTiposEventos');
   },

   removeTipo: function(button, event, opts) {
      this.getGridTiposEventos().remove();
   },

   saveTiposFormData: function(button, event, opts) {
      var grid = this.getGridTiposEventos();
      var form = this.getFormTiposEventos();
      form.saveFormData(grid, urlPrefix + 'tipoevento');
   }
});