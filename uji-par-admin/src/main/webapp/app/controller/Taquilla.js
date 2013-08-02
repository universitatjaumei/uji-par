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
      },
      {
    	ref: 'estadoPagoTarjeta',
        selector: '#formComprarCards label[name=estadoPagoTarjeta]'
      },
      {
      	ref: 'botonPagar',
        selector: 'formComprar #pagar'
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
             afterrender: this.iniciaFormComprar
         },    
         'formComprar #pagar': {
        	 click: this.registraCompra
         },
         'panelSeleccionarNoNumeradas': {
             afterrender: this.panelSeleccionarNoNumeradasCreado
         },
         'panelSeleccionarNoNumeradas combobox[name=localizacion]': {
        	 select: this.localizacionNoNumeradasCambiada
         }          
      });
      
	  var me = this;
	  
	  pm.bind('respuestaButacas', function(butacas){
		   console.log('Respuesta:', butacas);
		   
		   me.butacasSeleccionadas = butacas;
		   
		   me.avanzarAPasoDePago(butacas);
	  });      
   },
   
   iniciaFormComprar: function() {
	   
	   this.idCompra = null;
	   this.idPagoTarjeta = null;
	   this.butacasSeleccionadas = [];  
	  
	   this.cambiarEstadoBotonesComprar();
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
	   
	   this.consultaImportes(butacas);

	   this.cambiarEstadoBotonesComprar();
   },
   
   consultaImportes: function(butacas) {

	   var me = this;
	   var idSesion = this.getGridSesionesTaquilla().getSelectedRecord().data['id'];
	   
	   me.rellenaDatosPasoPagar("-");
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + idSesion + '/importe',
	    	  method: 'POST',
	    	  jsonData: butacas,
	    	  success: function (response) {
	    		  
	    		  console.log(response);

	    		  var importe = Ext.JSON.decode(response.responseText, true);
	    		  me.rellenaDatosPasoPagar(importe);
	    		   
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
   
   muestraMensajePagoTarjeta: function(mensaje) {
	   this.getEstadoPagoTarjeta().setText(mensaje, false);
   },
   
   registraCompra: function() {
	   
	   var tipoPago = Ext.getCmp('tipoPago').value;
	   
	   this.getBotonPagar().setDisabled(true);
	   
	   var idSesion = this.getGridSesionesTaquilla().getSelectedRecord().data['id'];

	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + idSesion,
	    	  method: 'POST',
	    	  jsonData: {idCompra:me.idCompra, butacasSeleccionadas:this.butacasSeleccionadas},
	    	  success: function (response) {
	    		  
	    		   if (tipoPago == 'metalico')
	    		   {
	    			   me.getBotonPagar().setDisabled(false);
	    			   alert('Pagado en metálico');
	    			   me.muestraMensajePagoTarjeta('');
	    			   me.getFormComprar().up('window').close();
	    		   }   
	    		   else
	    		   {
	    			   var respuesta = Ext.JSON.decode(response.responseText, true);

	    			   me.idCompra = respuesta['id'];
	    			   me.pagarConTarjeta(respuesta['id'], 'Entradas Paranimf UJI');
	    		   }   
	    		   
	    	  }, failure: function (response) {
	    		  
	    		  console.log(respuesta);
	    		  
	    		  me.getBotonPagar().setDisabled(false);

	    		  me.muestraMensajePagoTarjeta(UI.i18n.error.errorRegistrandoCompra);
	    		  
	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
	    		  
	    		  if (respuesta['message']!=null)
	    			  alert(respuesta['message']);
	    		  else
	    			  alert(UI.i18n.error.formSave);
	    	  }
	   	  });
   },   
   
   pagarConTarjeta: function(id, concepto) {
	   
	   var me = this;
	   
	   me.muestraMensajePagoTarjeta(UI.i18n.message.pagoTarjetaEnviando);
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'pago/' + id,
	    	  method: 'POST',
	    	  jsonData: {concepto:concepto},
	    	  success: function (response) {
	   
	    		  console.log('Pago con tarjeta aceptado:', response);
	    		  
    			  var respuesta = Ext.JSON.decode(response.responseText, true);
    			  
	    		  if (respuesta==null || respuesta.error)
	    		  {
	    			  var msj = UI.i18n.error.errorRealizaPago;
	    			  
	    			  if (respuesta['mensajeExcepcion'])
	    				  msj += ' (' + respuesta['mensajeExcepcion']  + ')';
	    			  
	    			  alert(msj);
	    			  me.muestraMensajePagoTarjeta(UI.i18n.error.errorRealizaPago);
		    		  me.getBotonPagar().setDisabled(false);
	    		  }
	    		  else
	    		  {
	    			  me.idPagoTarjeta = respuesta.codigo;
	    			  me.muestraMensajePagoTarjeta(UI.i18n.message.pagoTarjetaEnviadoLector);
	    			  
	    			  me.lanzaComprobacionEstadoPago(id);
	    		  }
    			  
	    	  }, failure: function (response) {
	    		  
	    		  me.getBotonPagar().setDisabled(false);
	    		  me.muestraMensajePagoTarjeta(UI.i18n.error.errorRealizaPago);

	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
	    		  
	    		  if (respuesta!=null && respuesta['message']!=null)
	    			  alert(respuesta['message']);
	    		  else
	    			  alert(UI.i18n.error.errorRealizaPago);
	    	  }
	   	  });
   },

   lanzaComprobacionEstadoPago: function(idPago) {
	   var me = this;
	   
	   this.intervalEstadoPago = window.setInterval(function(){me.compruebaEstadoPago(idPago)}, 1000);
   },
   
   paraComprobacionEstadoPago: function() {
	   window.clearInterval(this.intervalEstadoPago);
   },
   
   compruebaEstadoPago: function(idPago) {
	   
	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'pago/' + idPago,
	    	  method: 'GET',
	    	  success: function (response) {
	   
	    		  //me.getBotonPagar().setDisabled(false);
	    		  
	    		  console.log('Estado del pago con tarjeta:', response);
	    		  
	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
 			  
	    		  if (respuesta==null || respuesta.error)
	    		  {
	    			  var msj = UI.i18n.error.errorRealizaPago;
	    			  
	    			  if (respuesta['mensajeExcepcion'])
	    				  msj += ' (' + respuesta['mensajeExcepcion']  + ')';
	    			  
	    			  //alert(msj);
	    			  me.muestraMensajePagoTarjeta(msj);
	    		  }
	    		  else
	    		  {
	    			  if (respuesta['codigoAccion'] == '')
	    			  {
	    				  // El pago está en proceso
	    			  }
	    			  else if (respuesta['codigoAccion'] == '20' || respuesta['codigoAccion'] == '30' )
	    			  {
	    				  me.paraComprobacionEstadoPago();
	    				  me.muestraMensajePagoTarjeta('');
	    				  me.getBotonPagar().setDisabled(false);
	    				  alert(UI.i18n.message.pagoTarjetaCorrecto);
	    				  me.cerrarComprar();
	    			  }
	    			  else
	    			  {
	    				  me.paraComprobacionEstadoPago();
	    				  me.muestraMensajePagoTarjeta(UI.i18n.error.errorRealizaPago + '<br/>(' + respuesta['codigoAccion'] + ': ' + UI.i18n.error.pinpad[respuesta['codigoAccion']] + ')');
	    				  me.getBotonPagar().setDisabled(false);
	    		      }
	    		  }
 			  
	    	  }, failure: function (response) {
	    		  
	    		  me.getBotonPagar().setDisabled(false);
	    		  me.muestraMensajePagoTarjeta(UI.i18n.error.errorRealizaPago);

	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
	    		  
	    		  if (respuesta!=null && respuesta['message']!=null)
	    			  alert(respuesta['message']);
	    		  else
	    			  alert(UI.i18n.error.errorRealizaPago);
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

});