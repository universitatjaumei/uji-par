Ext.define('Paranimf.controller.Taquilla', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'taquilla.PanelTaquilla', 'taquilla.GridEventosTaquilla', 'taquilla.GridSesionesTaquilla', 'taquilla.FormComprar'],
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
      },
      {
       	ref: 'formComprar',
        selector: 'formComprar'
      },
      {
       	ref: 'formComprarCards',
        selector: '#formComprarCards'
      }      
   ],

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
         },
         'formComprar #comprarAnterior': {
        	 click: this.comprarAnterior
         },
         'formComprar #comprarSiguiente': {
        	 click: this.comprarSiguiente
         },
         'formComprar #comprarCancelar': {
        	 click: this.cerrarComprar
         },         
         'formComprar': {
             afterrender: this.cambiarEstadoBotonesComprar
         },    
         'formComprar #pagar': {
        	 click: this.pagar
         },           
         
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
      
	  var me = this;
	  
	  pm.bind('respuestaButacas', function(butacas){
		   console.log('Respuesta:', butacas);
		   
		   var layout = me.getFormComprarCards().getLayout();
		   layout.setActiveItem(layout.getNext());
		   
		   me.rellenaDatosPasoPagar(butacas);
		   
		   me.cambiarEstadoBotonesComprar();
	  });      
   },
   
   pagar: function() {
	   var tipoPago = Ext.getCmp('tipoPago').value;
	   
	   if (tipoPago == 'metalico')
		   alert('Pagado en metálico');
	   else
		   alert('Pagado con tarjeta');
	   
	   this.getFormComprar().up('window').close();
   },
   
   sumaImportes: function(butacas) {
	   var total = 0.0;
	   
	   for(var i=0; i<butacas.length; i++)
		   total += butacas[i].precio;
	   
	   return total;
   },
   
   rellenaDatosPasoPagar: function(butacas) {
	 Ext.getCmp('total').setText(UI.i18n.field.total + ": " + this.sumaImportes(butacas).toFixed(2) + " €");  
   },
   
   cerrarComprar: function() {
	   this.getFormComprar().up('window').close();  
   },
   
   comprarAnterior: function() {
	   console.log('Anterior');
	   
	   var layout = this.getFormComprarCards().getLayout();
	   var pasoActual = layout.activeItem.id;
	   
	   console.log(pasoActual);
	   
	   if (pasoActual != 'pasoSeleccionar')
	   {
		   layout.setActiveItem(layout.getActiveItem()-1);
	   }
	
	   this.cambiarEstadoBotonesComprar();
   },

   comprarSiguiente: function() {
	   console.log('Siguiente');
	   
	   var layout = this.getFormComprarCards().getLayout();
	   var pasoActual = layout.activeItem.id;
	   
	   console.log(pasoActual);
	   
	   if (pasoActual == 'pasoSeleccionar')
	   {
		   // Llámamos al iframe para que nos pase las butacas seleccionadas
		   pm({
			   target: window.frames['iframeButacas'],
			   type:'butacas', 
			   data:{}
		   });
	   }
	
	   this.cambiarEstadoBotonesComprar();
   },
   
   cambiarEstadoBotonesComprar: function () {
	   var layout = this.getFormComprarCards().getLayout();
	   
	   Ext.getCmp('comprarAnterior').setDisabled(!layout.getPrev());
	   Ext.getCmp('comprarSiguiente').setDisabled(!layout.getNext());	
   },

   recargaStore: function(comp, opts) {
      console.log('RECARGA STORE EVENTOS TAQUILLA');
      this.getGridEventosTaquilla().recargaStore();
   },

   loadSesiones: function(selectionModel, record) {
      if (record[0]) {
         var storeSesiones = this.getGridSesionesTaquilla().getStore();
         var eventoId = record[0].get('id');

         storeSesiones.getProxy().url = urlPrefix + 'evento/' + eventoId + '/sesiones?activos=true';
         storeSesiones.load();
      }
   },

   comprar: function(button, event, opts) {
	 var selectedRecord = this.getGridSesionesTaquilla().getSelectedRecord();
	 this.getGridSesionesTaquilla().showComprarWindow(selectedRecord.data['id']);
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
         var plantillaId = record[0].get('id');

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