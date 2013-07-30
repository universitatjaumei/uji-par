Ext.define('Paranimf.controller.Taquilla', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'taquilla.PanelTaquilla', 'taquilla.GridEventosTaquilla', 'taquilla.GridSesionesTaquilla', 
           'taquilla.FormComprar', 'taquilla.PanelSeleccionarNoNumeradas', 'taquilla.PanelNumeroEntradas'],
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
      },
      {
       	ref: 'comboLocalizacionNoNumeradas',
        selector: 'panelSeleccionarNoNumeradas combobox[name=localizacion]'
      },
      {
       	ref: 'disponiblesNoNumeradas',
        selector: 'panelSeleccionarNoNumeradas label[name=disponibles]'
      },      
      {
       	ref: 'localizacionesNoNumeradas',
        selector: 'panelSeleccionarNoNumeradas panel[name=localizaciones]'
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
         'panelSeleccionarNoNumeradas': {
             afterrender: this.panelSeleccionarNoNumeradasCreado
         },
         'panelSeleccionarNoNumeradas combobox[name=localizacion]': {
        	 select: this.localizacionNoNumeradasCambiada
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
      
	  var me = this;
	  
	  this.butacasSeleccionadas = [];

	  pm.bind('respuestaButacas', function(butacas){
		   console.log('Respuesta:', butacas);
		   
		   me.butacasSeleccionadas = butacas;
		   
		   me.avanzarAPasoDePago(butacas);
	  });      
   },
   
   localizacionNoNumeradasCambiada: function() {
	   var localizacion = this.getComboLocalizacionNoNumeradas().getValue();
	   
	   this.cambiaLocalizacionNoNumerada(localizacion);
	   
	   //console.log(localizacion.getValue());
   },
   
   cambiaLocalizacionNoNumerada: function(localizacion) {
	   this.muestraLocalizacionNoNumerada(localizacion);
	   this.muestraDisponiblesLocalizacion(localizacion);
   },
   
   muestraLocalizacionNoNumerada: function(localizacion) {
	   /*
	   var children = this.getLocalizacionesNoNumeradas().getEl().down('*');
	   Ext.each(children,function(child){child.hide();});
	   */
	   
	   //console.log(this.getLocalizacionesNoNumeradas().items);
	   
	   this.getLocalizacionesNoNumeradas().items.each(function(c){
		   
		   console.log('c.name:' + c.name);
		   console.log('localizacion:' + localizacion);
		   
		   if (c.name == localizacion)
			   c.show();
		   else
			   c.hide();
	   });
   },
   
   muestraDisponiblesLocalizacion: function(localizacion) {
	   
	   this.getDisponiblesNoNumeradas().setText(this.disponibles[localizacion]);
   },
   
   panelSeleccionarNoNumeradasCreado: function() {
	   
	   var me = this;
	   var idSesion = this.getGridSesionesTaquilla().getSelectedRecord().data['id'];
	   
	   this.cargaPrecios(idSesion, function() {
		   me.cargaDisponibles(idSesion, function() {
			   me.getFormComprar().cargaComboStore('localizacion', undefined);
			   me.getComboLocalizacionNoNumeradas().setValue('anfiteatro');
			   me.cambiaLocalizacionNoNumerada('anfiteatro');
		   });
	   });
   },
   
   avanzarAPasoDePago: function (butacas) {
	   
	   var layout = this.getFormComprarCards().getLayout();
	   layout.setActiveItem(layout.getNext());
	   
	   if (this.entradasNumeradas())
	   {
		   this.rellenaDatosPasoPagar(this.sumaImportes(butacas).toFixed(2));
	   }
	   else
	   {
		   this.rellenaDatosPasoPagar(this.sumaImportesNoNumeradas(butacas));
	   }
	   
	   this.cambiarEstadoBotonesComprar();
   },
   
   getButacasNoNumeradas: function() {
	   
	   var me = this;
	   var butacas = [];
	   
	   this.getLocalizacionesNoNumeradas().items.each(function(panel){
		   panel.items.each(function(field){
			   console.log(panel.name + ' - ' + field.name + ' - ' + field.value);
		
			   for (var i=0; i<parseInt(field.value); i++)
			   {
					var butaca = {
							localizacion : panel.name,
							fila : null,
							numero : null,
							x : null,
							y : null,
							tipo : field.name,
							precio: me.precios[panel.name][field.name]
						};
					
				   butacas.push(butaca);
			   }
			   //total += field.value * me.precios[panel.name][field.name];			   
		   });
	   });
	   
	   console.log(butacas);
	   
	   return butacas;
   },
   
   sumaImportesNoNumeradas: function(butacas) {
	   
	   var me = this;
	   var total = 0;
	   
	   for (var i=0; i<butacas.length; i++)
	   {
		   total += butacas[i].precio;
	   }
	   
	   console.log('total:', total);
	   
	   return total;
   },
   
   pagar: function() {
	   var tipoPago = Ext.getCmp('tipoPago').value;
	   
	   var idSesion = this.getGridSesionesTaquilla().getSelectedRecord().data['id'];

	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + idSesion,
	    	  method: 'POST',
	    	  jsonData: this.butacasSeleccionadas,
	    	  success: function (response) {
	    		  me.getFormComprar().up('window').close();
	    		  
	    		   if (tipoPago == 'metalico')
	    			   alert('Pagado en metálico');
	    		   else
	    			   alert('Pagado con tarjeta');
	    		   
	    	  }, failure: function (response) {

	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
	    		  
	    		  console.log(respuesta);
	    		  
	    		  if (respuesta['message']!=null)
	    			  alert(respuesta['message']);
	    		  else
	    			  alert(UI.i18n.error.formSave);
	    	  }
	   	  });
   },
   
   cargaPrecios: function(sesionId, callback) {
	   
	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + sesionId + '/precios',
	    	  method: 'GET',
	    	  success: function (response) {
	    		  
	    		  var jsonData = Ext.decode(response.responseText);
	    		  console.log(jsonData);
	    		  
	    		  me.precios = {};
	    		  
	    		  for (var i=0; i<jsonData.data.length; i++)
	    		  {
					var sesion = jsonData.data[i];
					me.precios[sesion.localizacion.codigo] = {normal:sesion.precio, descuento:sesion.descuento, invitacion:sesion.invitacion};
	    		  }

	    		  console.log(me.precios);
	    		  
	    		  callback();
	    		  
	    	  }, failure: function (response) {
	    		  alert(UI.i18n.error.loadingPreciosNoNumeradas);
	    	  }
	   	  });
   },
   
   cargaDisponibles: function(sesionId, callback) {
	   
	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + sesionId + '/disponibles',
	    	  method: 'GET',
	    	  success: function (response) {
	    		  
	    		  var jsonData = Ext.decode(response.responseText);
	    		  console.log(jsonData);
	    		  
	    		  me.disponibles = {};
	    		  
	    		  for (var i=0; i<jsonData.data.length; i++)
	    		  {
					var disponible = jsonData.data[i];
					me.disponibles[disponible.localizacion] = disponible.disponibles;
	    		  }

	    		  console.log(me.disponibles);
	    		  
	    		  callback();
	    		  
	    	  }, failure: function (response) {
	    		  alert(UI.i18n.error.loadingDisponiblesNoNumeradas);
	    	  }
	   	  });
   },   
   
   sumaImportes: function(butacas) {
	   var total = 0.0;
	   
	   for(var i=0; i<butacas.length; i++)
		   total += butacas[i].precio;
	   
	   return total;
   },
   
   rellenaDatosPasoPagar: function(importe) {
	 Ext.getCmp('total').setText(UI.i18n.field.total + ": " + importe + " €");  
   },
   
   cerrarComprar: function() {
	   this.getFormComprar().up('window').close();  
   },
   
   entradasNumeradas: function() {
	   return Ext.getCmp('pasoSeleccionar').getLayout().activeItem.id == 'iframeButacas';
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
		   if (this.entradasNumeradas())
			   this.comprarSiguienteNumeradas();
		   else
			   this.comprarSiguienteSinNumerar();
	   }
	   
	   this.cambiarEstadoBotonesComprar();
   },
   
   comprarSiguienteNumeradas: function() {
	   console.log('Siguiente numeradas');

	   // Llamamos al iframe para que nos pase las butacas seleccionadas
	   pm({
		   target: window.frames['iframeButacas'],
		   type:'butacas', 
		   data:{}
	   });
   },   
   
   comprarSiguienteSinNumerar: function() {
	   
	   this.butacasSeleccionadas = this.getButacasNoNumeradas(); 
	   
	   this.avanzarAPasoDePago(this.butacasSeleccionadas);
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
	 var evento = this.getGridEventosTaquilla().getSelectedRecord();
	 var sesion = this.getGridSesionesTaquilla().getSelectedRecord();
	 this.getGridSesionesTaquilla().showComprarWindow(sesion.data['id'], evento.data['asientosNumerados']);
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