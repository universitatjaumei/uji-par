Ext.define('Paranimf.controller.PlantillasPrecios', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'plantillaprecios.PanelPlantillasPrecios', 'plantillaprecios.GridPlantillas', 'plantillaprecios.FormPlantillas', 'plantillaprecios.GridPrecios', 'plantillaprecios.FormPrecios'],
   stores: ['PlantillasPrecios', 'Precios', 'Localizaciones', 'PlantillasPreciosEditables'],
   models: ['PlantillaPrecios', 'Precio', 'Localizacion'],

   refs: [{
      ref: 'gridPlantillas',
      selector: 'gridPlantillas'
   }, {
      ref: 'formPlantillas',
      selector: 'formPlantillas'
   }, {
	   ref: 'panelPlantillas',
	   selector: 'panelPlantillas'
   }, {
	   ref: 'gridPrecios',
	   selector: 'gridPrecios'
   }, {
	   ref: 'formPrecios',
	   selector: 'formPrecios'
   }],

   init: function() {
      this.control({

         'gridPlantillas button[action=add]': {
            click: this.addPlantilla
         },

         'gridPlantillas button[action=edit]': {
            click: this.editPlantilla
         },

         'gridPlantillas button[action=del]': {
            click: this.removePlantilla
         },

         'panelPlantillas': {
			   beforeactivate: this.recargaStore
         },

         'formPlantillas button[action=save]': {
            click: this.savePlantillaFormData
         },

         'gridPlantillas': {
             selectionchange: this.loadPrecios
         },
         
         'gridPrecios button[action=add]': {
             click: this.addPrecio
         },

         'gridPrecios button[action=edit]': {
             click: this.editPrecio
         },

         'gridPrecios button[action=del]': {
            click: this.removePrecio
         },

         'formPrecios': {
            afterrender: this.recargaStores
         },
         
         'formPrecios button[action=save]': {
            click: this.savePrecioFormData
         }
      });
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE PLANTILLASPRECIOS");
      this.getGridPlantillas().recargaStore();
   },

   recargaStores: function(comp, opts) {
      var localizacionId = undefined;
      if (this.getGridPrecios().getSelectedColumnId() != undefined)
         localizacionId = this.getGridPrecios().getSelectedRecord(this.getGridPrecios()).data.localizacion_id;

      this.getFormPrecios().cargaComboStore('localizacion', localizacionId);
      this.getFormPrecios().cargaComboStore('plantillaPrecios', this.getGridPlantillas().getSelectedColumnId());
   },

   addPlantilla: function(button, event, opts) {
      this.getGridPlantillas().showAddPlantillaWindow();
   },

   editPlantilla: function(button, event, opts) {
      this.getGridPlantillas().edit('formPlantillas', undefined, 600, 100);
   },

   removePlantilla: function(button, event, opts) {
      var gridPrecios = this.getGridPrecios();
      this.getGridPlantillas().remove(function (borradoCorrectamente) {
         if (borradoCorrectamente)
            gridPrecios.getStore().removeAll();
      });
   },

   savePlantillaFormData: function(button, event, opts) {
      var grid = this.getGridPlantillas();
      var form = this.getFormPlantillas();
      form.saveFormData(grid, urlPrefix + 'plantillaprecios');
   },
   
   loadPrecios: function(selectionModel, record) {
      if (record[0]) {
         var storePrecios = this.getGridPrecios().getStore();
         var plantillaId = record[0].get("id");

         storePrecios.getProxy().url = urlPrefix + 'plantillaprecios/' + plantillaId + '/precios';
         storePrecios.load();
      }
   },
   
   addPrecio: function(button, event, opts) {
	   if (this.getGridPlantillas().getSelectedColumnId())
		   this.getGridPrecios().showAddPrecioWindow();
	   else
		   alert(UI.i18n.message.plantillaPrecios);
   },
   
   savePrecioFormData: function(button, event, opts) {
	   var plantillaId = this.getGridPlantillas().getSelectedColumnId();

	   var grid = this.getGridPrecios();
	   var form = this.getFormPrecios();
	   
	   var precio = this.getFormPrecios().getForm().findField('precio');
	   var descuento = this.getFormPrecios().getForm().findField('descuento');
	   var invitacion = this.getFormPrecios().getForm().findField('invitacion');
	   
	   // Pasar precios a float para que se envíen bien al REST
	   precio.setRawValue(precio.value);
	   descuento.setRawValue(descuento.value);
	   invitacion.setRawValue(invitacion.value);
	   
	   form.saveFormData(grid, urlPrefix + 'plantillaprecios/' + plantillaId + '/precios');
   },
   
   removePrecio: function(button, event, opts) {
      this.getGridPrecios().remove();
   },
   
   editPrecio: function(button, event, opts) {
      this.getGridPrecios().edit('formPrecios', undefined, 600, 300);
   }
});