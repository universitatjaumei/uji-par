Ext.define('Paranimf.controller.Localizaciones', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'localizacion.GridLocalizaciones', 'localizacion.FormLocalizaciones'],
   stores: ['Localizaciones'],
   models: ['Localizacion'],

   refs: [{
      ref: 'gridLocalizaciones',
      selector: 'gridLocalizaciones'
   }, {
      ref: 'formLocalizaciones',
      selector: 'formLocalizaciones'
   }],

   init: function() {
      this.control({

         'gridLocalizaciones button[action=add]': {
            click: this.addLocalizacion
         },

         'gridLocalizaciones button[action=edit]': {
            click: this.editLocalizacion
         },

         'gridLocalizaciones button[action=del]': {
            click: this.removeLocalizacion
         },

         'gridLocalizaciones': {
			   beforeactivate: this.recargaStore
         },

         'formLocalizaciones button[action=save]': {
            click: this.saveLocalizacionesFormData
         }
      });
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE LOCALIZACIONES");
      this.getGridLocalizaciones().recargaStore();
   },

   addLocalizacion: function(button, event, opts) {
      this.getGridLocalizaciones().showAddLocalizacionWindow();
   },

   editLocalizacion: function(button, event, opts) {
      this.getGridLocalizaciones().edit('formLocalizaciones');
   },

   removeLocalizacion: function(button, event, opts) {
      this.getGridLocalizaciones().remove();
   },

   saveLocalizacionesFormData: function(button, event, opts) {
      var grid = this.getGridLocalizaciones();
      var form = this.getFormLocalizaciones();
      form.saveFormData(grid, urlPrefix + 'localizacion');
   }
});