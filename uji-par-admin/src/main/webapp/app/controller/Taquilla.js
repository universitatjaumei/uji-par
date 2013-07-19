Ext.define('Paranimf.controller.Taquilla', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'taquilla.PanelTaquilla', 'taquilla.GridEventosTaquilla', 'taquilla.GridSesionesTaquilla', "taquilla.FormComprar"],
   stores: ['EventosTaquilla', 'SesionesTaquilla'],
   models: ['Evento'],

   refs: [
      {
   	   	ref: 'panelTaquilla',
   	   	selector: 'panelTaquilla'
      },
      {
    	ref: 'gridEventosTaquilla',
    	selector: 'gridEventosTaquilla'
      },
      {
      	ref: 'gridSesionesTaquilla',
      	selector: 'gridSesionesTaquilla'
      }],

   init: function() {
	   
      this.control({
    	  
    	 'panelTaquilla': {
    		beforeactivate: this.recargaStore
         },
         'gridEventosTaquilla': {
             selectionchange: this.loadSesiones
         },
         'gridSesionesTaquilla button[action=comprar]': {
             click: this.comprar
         }  
         
         /*
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
    	   */
      });
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE EVENTOS TAQUILLA");
      this.getGridEventosTaquilla().recargaStore();
   },

   loadSesiones: function(selectionModel, record) {
      if (record[0]) {
         var storeSesiones = this.getGridSesionesTaquilla().getStore();
         var eventoId = record[0].get("id");

         storeSesiones.getProxy().url = urlPrefix + 'evento/' + eventoId + '/sesiones?activos=true';
         storeSesiones.load();
      }
   },

   comprar: function(button, event, opts) {
	 this.getGridSesionesTaquilla().showComprarWindow();
   }
   
   /*
   recargaStores: function(comp, opts) {
      var localizacionId = undefined;
      if (this.getGridPrecios().getSelectedColumnId() != undefined)
         localizacionId = this.getGridPrecios().getSelectedRecord(this.getGridPrecios()).data.localizacion_id;

      this.getFormPrecios().cargaComboStore('localizacion', localizacionId);
      this.getFormPrecios().cargaComboStore('taquilla', this.getGridPlantillas().getSelectedColumnId());
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
		   alert(UI.i18n.message.taquilla);
   },
   
   savePrecioFormData: function(button, event, opts) {
	   var plantillaId = this.getGridPlantillas().getSelectedColumnId();

	   var grid = this.getGridPrecios();
	   var form = this.getFormPrecios();
	   form.saveFormData(grid, urlPrefix + 'plantillaprecios/' + plantillaId + '/precios');
   },
   
   removePrecio: function(button, event, opts) {
      this.getGridPrecios().remove();
   },
   
   editPrecio: function(button, event, opts) {
      this.getGridPrecios().edit('formPrecios', undefined, 600, 300);
   }
   */
});