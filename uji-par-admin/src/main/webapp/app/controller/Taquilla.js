Ext.define('Paranimf.controller.Taquilla', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'taquilla.PanelTaquilla', 'taquilla.GridEventosTaquilla', 'taquilla.GridSesionesTaquilla', 
           'taquilla.FormComprar', 'taquilla.PanelSeleccionarNoNumeradas', 'taquilla.PanelNumeroEntradas'],
   stores: ['EventosTaquilla', 'SesionesTaquilla'/*, 'Compras'*/],
   models: ['Evento', 'Compra'],

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
        ref: 'verEntrada',
        selector: 'formComprar button[name=verEntrada]'
      },      
      {
      	ref: 'botonPagar',
        selector: 'formComprar #pagar'
      },
      {
       	ref: 'botonReservar',
        selector: 'formComprar #reservar'
      },
      {
          ref: 'panelComprar',
          selector: 'formComprar panel[name=panelComprar]'
      },         
      {
          ref: 'panelReservar',
          selector: 'formComprar panel[name=panelReservar]'
      },
      {
          ref: 'reservarDesde',
          selector: 'formComprar datepicker[name=desde]'
      },               
      {
          ref: 'reservarHasta',
          selector: 'formComprar datepicker[name=hasta]'
      },
      {
          ref: 'observacionesReserva',
          selector: 'formComprar textareafield[name=observacionesReserva]'
      },
      {
      	ref: 'panelAnfiteatro',
      	selector: 'panelSeleccionarNoNumeradas panelNumeroEntradas[name=anfiteatro]'
      },
      {
      	ref: 'panelPlatea1',
      	selector: 'panelSeleccionarNoNumeradas panelNumeroEntradas[name=platea1]'
      },
      {
      	ref: 'panelPlatea2',
      	selector: 'panelSeleccionarNoNumeradas panelNumeroEntradas[name=platea2]'
      },
      {
      	ref: 'panelDiscapacitados1',
      	selector: 'panelSeleccionarNoNumeradas panelNumeroEntradas[name=discapacitados1]'
      },
      {
      	ref: 'panelDiscapacitados2',
      	selector: 'panelSeleccionarNoNumeradas panelNumeroEntradas[name=discapacitados2]'
      },
      {
      	ref: 'panelDiscapacitados3',
      	selector: 'panelSeleccionarNoNumeradas panelNumeroEntradas[name=discapacitados3]'
      },
      {
      	ref: 'totalPrecioCompra',
      	selector: 'panelSeleccionarNoNumeradas label[name=totalPrecios]'
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
         'gridSesionesTaquilla button[action=reservar]': {
             click: this.reservar
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
         'formComprar #reservar': {
        	 click: this.registraReserva
         },        
         'formComprar button[name=verEntrada]': {
        	click: this.verEntrada 
         },
         'panelSeleccionarNoNumeradas': {
             afterrender: this.panelSeleccionarNoNumeradasCreado
         },
         'panelNumeroEntradas numberfield': {
         	change: this.actualizaPrecio
         }
      });
      
	  this.intervalEstadoPago = {};
	  
	  var me = this;
	  pm.bind('respuestaButacas', function(butacas){
		   console.log('Respuesta:', butacas);
		   
		   me.butacasSeleccionadas = butacas;
		   
		   me.avanzarAPasoDePago(butacas);
	  });      
   },

   	actualizaPrecio: function() {
   		var precio = 0;
   		var EURO = '€';
   		for (var key in this.precios) {
   			var panel = Ext.ComponentQuery.query('panelSeleccionarNoNumeradas panelNumeroEntradas[name=' + key + ']');
   			if (panel != undefined) {
   				var numeroEntradasNormal = (panel[0].down('numberfield[name=normal]').getValue() == '')?0:panel[0].down('numberfield[name=normal]').getValue();
   				var numeroEntradasDescuento = (panel[0].down('numberfield[name=descuento]').getValue() == '')?0:panel[0].down('numberfield[name=descuento]').getValue();
   				var numeroEntradasInvitacion = (panel[0].down('numberfield[name=invitacion]').getValue() == '')?0:panel[0].down('numberfield[name=invitacion]').getValue();
   				var precioNormal = (this.precios[key] != undefined)?this.precios[key]['normal']:0;
   				var precioDescuento = (this.precios[key] != undefined)?this.precios[key]['descuento']:0;
   				var precioInvitacion = (this.precios[key] != undefined)?this.precios[key]['invitacion']:0;

   				precio += numeroEntradasNormal*precioNormal + numeroEntradasDescuento*precioDescuento + numeroEntradasInvitacion*precioInvitacion;
   			}
   		}
   		this.getTotalPrecioCompra().setText(UI.i18n.field.totalCompra + precio.toFixed(2) + EURO);
   	},
   
   iniciaFormComprar: function() {
	   
	   this.idCompra = null;
	   this.idPagoTarjeta = null;
	   this.butacasSeleccionadas = [];  
	  
	   this.cambiarEstadoBotonesComprar();
	   
	   if (this.windowEntrada != null)
	   {
		   this.windowEntrada.close();
	   }
   },
   
   	muestraDisponibles: function() {
   		var DISPONIBLES = UI.i18n.field.entradesDisponibles;
   		for (var key in this.disponibles) {
   			var panel = Ext.ComponentQuery.query('panelSeleccionarNoNumeradas panelNumeroEntradas[name=' + key + ']');
   			
   			if (panel != undefined && panel.length >= 1)
   				panel[0].down('label[name=disponibles]').setText(DISPONIBLES + this.disponibles[key]);
   		}
   	},

   	muestraPrecios: function() {
   		var PRECIOS = UI.i18n.field.precioPorEntrada;
   		var EURO = '&euro;';
   		for (var key in this.precios) {
   			var panel = Ext.ComponentQuery.query('panelSeleccionarNoNumeradas panelNumeroEntradas[name=' + key + ']');
   			if (panel != undefined && panel.length >= 1) {
   				panel[0].down('panel[name=preuNormal]').update(PRECIOS + this.precios[key]['normal'] + EURO);
   				panel[0].down('panel[name=preuDescuento]').update(PRECIOS + this.precios[key]['descuento'] + EURO);
   				panel[0].down('panel[name=preuInvitacion]').update(PRECIOS + this.precios[key]['invitacion'] + EURO);
   			}
   		}
   		/*this.getPanelAnfiteatro().down('panel[name=preuNormal]').update(PRECIOS + this.precios['anfiteatro']['normal'] + EURO);
   		this.getPanelPlatea1().down('panel[name=preuNormal]').update(PRECIOS + this.precios['platea1']['normal'] + EURO);
   		this.getPanelPlatea2().down('panel[name=preuNormal]').update(PRECIOS + this.precios['platea2']['normal'] + EURO);
   		this.getPanelDiscapacitados1().down('panel[name=preuNormal]').update(PRECIOS + this.precios['discapacitados1']['normal'] + EURO);
   		this.getPanelDiscapacitados2().down('panel[name=preuNormal]').update(PRECIOS + this.precios['discapacitados2']['normal'] + EURO);
   		this.getPanelDiscapacitados3().down('panel[name=preuNormal]').update(PRECIOS + this.precios['discapacitados3']['normal'] + EURO);

   		this.getPanelAnfiteatro().down('panel[name=preuDescuento]').update(PRECIOS + this.precios['anfiteatro']['descuento'] + EURO);
   		this.getPanelPlatea1().down('panel[name=preuDescuento]').update(PRECIOS + this.precios['platea1']['descuento'] + EURO);
   		this.getPanelPlatea2().down('panel[name=preuDescuento]').update(PRECIOS + this.precios['platea2']['descuento'] + EURO);
   		this.getPanelDiscapacitados1().down('panel[name=preuDescuento]').update(PRECIOS + this.precios['discapacitados1']['descuento'] + EURO);
   		this.getPanelDiscapacitados2().down('panel[name=preuDescuento]').update(PRECIOS + this.precios['discapacitados2']['descuento'] + EURO);
   		this.getPanelDiscapacitados3().down('panel[name=preuDescuento]').update(PRECIOS + this.precios['discapacitados3']['descuento'] + EURO);

   		this.getPanelAnfiteatro().down('panel[name=preuInvitacion]').update(PRECIOS + this.precios['anfiteatro']['invitacion'] + EURO);
   		this.getPanelPlatea1().down('panel[name=preuInvitacion]').update(PRECIOS + this.precios['platea1']['invitacion'] + EURO);
   		this.getPanelPlatea2().down('panel[name=preuInvitacion]').update(PRECIOS + this.precios['platea2']['invitacion'] + EURO);
   		this.getPanelDiscapacitados1().down('panel[name=preuInvitacion]').update(PRECIOS + this.precios['discapacitados1']['invitacion'] + EURO);
   		this.getPanelDiscapacitados2().down('panel[name=preuInvitacion]').update(PRECIOS + this.precios['discapacitados2']['invitacion'] + EURO);
   		this.getPanelDiscapacitados3().down('panel[name=preuInvitacion]').update(PRECIOS + this.precios['discapacitados3']['invitacion'] + EURO);*/
   	},

   	activaCamposCompra: function() {
   		for (var key in this.disponibles) {
   			//console.log(key);
   			//console.log(this.disponibles[key]);
   			var hayDisponibles = (this.disponibles[key]==0)?false:true;
   			var hayPrecioNormal = false, hayPrecioDescuento = false, hayPrecioInvitacion = false;
   			var panel;
   			
   			if (this.precios[key] != undefined && this.precios[key] != '' && this.precios[key] != 0) {
   				//console.log(this.precios[key]['normal']);
   				hayPrecioNormal = (this.precios[key]['normal'] == undefined)?false:true;
   				hayPrecioDescuento = (this.precios[key]['descuento'] == undefined)?false:true;
   				hayPrecioInvitacion = (this.precios[key]['invitacion'] == undefined)?false:true;
   			}

   			if (hayDisponibles) {
   				switch (key) {
   					case 'anfiteatro':
   						panel = this.getPanelAnfiteatro();
   						break;
   					case 'platea1':
   						panel = this.getPanelPlatea1();
   						break;
   					case 'platea2':
   						panel = this.getPanelPlatea2();
   						break;
   					case 'discapacitados1':
   						panel = this.getPanelDiscapacitados1();
   						break;
   					case 'discapacitados2':
   						panel = this.getPanelDiscapacitados2();
   						break;
   					case 'discapacitados2':
   						panel = this.getPanelDiscapacitados2();
   						break;
   				}
				panel.down('numberfield[name=normal]').setDisabled(!hayPrecioNormal);
				panel.down('numberfield[name=descuento]').setDisabled(!hayPrecioDescuento);
				panel.down('numberfield[name=invitacion]').setDisabled(!hayPrecioInvitacion);
   			}
   		}
   	},
   
   	panelSeleccionarNoNumeradasCreado: function() {
	   var me = this;
	   var idSesion = this.getGridSesionesTaquilla().getSelectedRecord().data['id'];
	   
	   this.cargaPrecios(idSesion, function() {
		   me.cargaDisponibles(idSesion, function() {
			   me.muestraDisponibles();
			   me.muestraPrecios();
			   me.activaCamposCompra();
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
	   	var tipos = {};tipos['normal'] = '';tipos['descuento'] = '';tipos['invitacion'] = '';
	   	for (var key in this.precios) {
	   		var panel = Ext.ComponentQuery.query('panelSeleccionarNoNumeradas panelNumeroEntradas[name=' + key + ']');
   			if (panel != undefined && panel.length >= 1) {
   				for (var tipoEntrada in tipos) {
   					//console.log(tipoEntrada);
   					//console.log(panel[0].down('numberfield[name=' + tipoEntrada + ']'));
   					var value = panel[0].down('numberfield[name=' + tipoEntrada + ']').value;
   					for (var i=0; i<parseInt(value); i++)
				   	{
						var butaca = {
								localizacion : key,
								fila : null,
								numero : null,
								x : null,
								y : null,
								tipo : tipoEntrada,
								precio: me.precios[key][tipoEntrada]
							};
						
					   	butacas.push(butaca);
				   	}
   				}
   			}
	   	}
	   	//console.log(butacas);
	   	return butacas;
   	},
   
   muestraMensajePagoTarjeta: function(mensaje) {
	   if (this.getEstadoPagoTarjeta()!=null)
		   this.getEstadoPagoTarjeta().setText(mensaje, false);
   },
   
   habilitaBotonPagar: function()
   {
	   if (this.getBotonPagar()!=null)
		   this.getBotonPagar().setDisabled(false);
   },

   deshabilitaBotonPagar: function()
   {
	   if (this.getBotonPagar()!=null)
		   this.getBotonPagar().setDisabled(true);   
   },
   
   habilitaBotonReservar: function()
   {
	   if (this.getBotonReservar()!=null)
		   this.getBotonReservar().setDisabled(false);
   },
   
   deshabilitaBotonReservar: function()
   {
	   if (this.getBotonReservar()!=null)
		   this.getBotonReservar().setDisabled(true);   
   },
   
   registraCompra: function() {
	   
	   var tipoPago = Ext.getCmp('tipoPago').value;
	   
	   this.deshabilitaBotonPagar();
	   
	   var idSesion = this.getGridSesionesTaquilla().getSelectedRecord().data['id'];

	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + idSesion,
	    	  method: 'POST',
	    	  jsonData: this.butacasSeleccionadas,
	    	  success: function (response) {
	    		  
	    		   var respuesta = Ext.JSON.decode(response.responseText, true);

	    		   me.idCompra = respuesta['id'];
	    		   me.uuidCompra = respuesta['uuid'];
	    		  
	    		   if (tipoPago == 'metalico')
	    		   {
	    			   me.marcaPagada(me.idCompra);
	    		   }   
	    		   else
	    		   {
	    			   me.pagarConTarjeta(respuesta['id'], 'Entradas Paranimf UJI');
	    		   }   
	    		   
	    	  }, failure: function (response) {
	    		  
	    		  console.log(respuesta);
	    		  
	    		  me.habilitaBotonPagar();

	    		  me.muestraMensajePagoTarjeta(UI.i18n.error.errorRegistrandoCompra);
	    		  
	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
	    		  
	    		  if (respuesta['message']!=null)
	    			  alert(respuesta['message']);
	    		  else
	    			  alert(UI.i18n.error.formSave);
	    	  }
	   	  });  
   },
   
   registraReserva: function() {
	   
	   this.deshabilitaBotonReservar();
	   
	   var idSesion = this.getGridSesionesTaquilla().getSelectedRecord().data['id'];

	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'reserva/' + idSesion,
	    	  method: 'POST',
	    	  jsonData: {butacasSeleccionadas:this.butacasSeleccionadas, desde:this.getReservarDesde().getValue(), hasta:this.getReservarHasta().getValue()
	    		  		 , observaciones: this.getObservacionesReserva().getValue()},
	    	  success: function (response) {
	    		  
	    		   me.habilitaBotonReservar();
	    		   me.cerrarComprar();
	    		  
	    	  }, failure: function (response) {
	    		  
	    		  console.log(respuesta);
	    		  
	    		  me.habilitaBotonReservar();
	    		  
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
		    		  me.habilitaBotonPagar();
	    		  }
	    		  else
	    		  {
	    			  me.idPagoTarjeta = respuesta.codigo;
	    			  me.muestraMensajePagoTarjeta(UI.i18n.message.pagoTarjetaEnviadoLector);
	    			  
	    			  me.lanzaComprobacionEstadoPago(id);
	    		  }
    			  
	    	  }, failure: function (response) {
	    		  
	    		  me.habilitaBotonPagar();
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
	   
	   this.intervalEstadoPago[idPago] = window.setInterval(function(){me.compruebaEstadoPago(idPago);}, 1000);
   },
   
   paraComprobacionEstadoPago: function(idPago) {
	   window.clearInterval(this.intervalEstadoPago[idPago]);
	   delete this.intervalEstadoPago[idPago];
   },
   
   compruebaEstadoPago: function(idPago) {
	   
	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'pago/' + idPago,
	    	  method: 'GET',
	    	  success: function (response) {
	   
	    		  //habilitaBotonPagar();
	    		  
	    		  console.log('Estado del pago con tarjeta:', response);
	    		  
	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
 			  
	    		  if (respuesta==null || respuesta.error)
	    		  {
	    			  me.paraComprobacionEstadoPago(idPago);

	    			  var msj = UI.i18n.error.errorRealizaPago;
	    			  
	    			  if (respuesta!=null && respuesta['mensajeExcepcion'])
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
	    				  me.paraComprobacionEstadoPago(idPago);
	    				  me.muestraMensajePagoTarjeta(UI.i18n.message.pagoTarjetaCorrecto);
	    				  me.desahiblitaEstadoBotonesComprar();
	    				  me.muestraEnlacePdf();
	    			  }
	    			  else
	    			  {
	    				  me.paraComprobacionEstadoPago(idPago);
	    				  me.muestraMensajePagoTarjeta(UI.i18n.error.errorRealizaPago + '<br/>(' + respuesta['codigoAccion'] + ': ' + UI.i18n.error.pinpad[respuesta['codigoAccion']] + ')');
	    				  me.habilitaBotonPagar();
	    		      }
	    		  }
 			  
	    	  }, failure: function (response) {
	    		  
	    		  me.habilitaBotonPagar();
	    		  me.muestraMensajePagoTarjeta(UI.i18n.error.errorRealizaPago);

	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
	    		  
	    		  if (respuesta!=null && respuesta['message']!=null)
	    			  alert(respuesta['message']);
	    		  else
	    			  alert(UI.i18n.error.errorRealizaPago);
	    	  }
	   	  });
   },
   
   muestraEnlacePdf: function() {
	   //console.log(this.getVerEntrada());
	   
	   //var href = urlPrefix + 'compra/' + this.uuidCompra + '/pdftaquilla';
	   //this.getVerEntrada().update('<a href="' + href + '" target="_blank">' + UI.i18n.button.verEntrada + '</a>');
	   
	   //this.getVerEntrada().update('<a href="javascript:w=window.open(\'' + href + '\', \'Imprimir entrades\'); w.print();" target="_blank">' + UI.i18n.button.verEntrada + '</a>');
	   
	   this.getVerEntrada().show();
   },
   
   verEntrada: function() {
	   var href = urlPrefix + 'compra/' + this.uuidCompra + '/pdftaquilla';
	   
	   this.windowEntrada = window.open(href, 'Imprimir entrada');
	   this.windowEntrada.print();
	   //w.close();
   },
   
   cargaPrecios: function(sesionId, callback) {
	   
	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + sesionId + '/precios',
	    	  method: 'GET',
	    	  success: function (response) {
	    		  
	    		  var jsonData = Ext.decode(response.responseText);
	    		  //console.log(jsonData);
	    		  
	    		  me.precios = {};
	    		  
	    		  for (var i=0; i<jsonData.data.length; i++)
	    		  {
					var sesion = jsonData.data[i];
					me.precios[sesion.localizacion.codigo] = {normal:sesion.precio, descuento:sesion.descuento, invitacion:sesion.invitacion};
	    		  }

	    		  //console.log(me.precios);
	    		  
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
	    		  //console.log(jsonData);
	    		  
	    		  me.disponibles = {};
	    		  
	    		  for (var i=0; i<jsonData.data.length; i++)
	    		  {
					var disponible = jsonData.data[i];
					me.disponibles[disponible.localizacion] = disponible.disponibles;
	    		  }

	    		  //console.log(me.disponibles);
	    		  
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
	   //console.log('Anterior');
	   
	   var layout = this.getFormComprarCards().getLayout();
	   var pasoActual = layout.activeItem.id;
	   
	   //console.log(pasoActual);
	   
	   if (pasoActual != 'pasoSeleccionar')
	   {
		   layout.setActiveItem(layout.getActiveItem()-1);
	   }
	
	   this.cambiarEstadoBotonesComprar();
   },

   comprarSiguiente: function() {
	   //console.log('Siguiente');
	   
	   var layout = this.getFormComprarCards().getLayout();
	   var pasoActual = layout.activeItem.id;
	   
	   //console.log(pasoActual);
	   
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
   
   desahiblitaEstadoBotonesComprar: function () {
	   Ext.getCmp('comprarAnterior').setDisabled(true);
	   Ext.getCmp('comprarSiguiente').setDisabled(true);	
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
   		if (this.getGridEventosTaquilla().hasRowSelected() && this.getGridSesionesTaquilla().hasRowSelected()) {
			var evento = this.getGridEventosTaquilla().getSelectedRecord();
			var sesion = this.getGridSesionesTaquilla().getSelectedRecord();

			this.getGridSesionesTaquilla().showComprarWindow(sesion.data['id'], evento.data['asientosNumerados'], UI.i18n.formTitle.comprar, false);
			
			this.getPanelComprar().show();
			this.getPanelReservar().hide();
		} else
			alert(UI.i18n.message.selectRow);
   	},
   
   	reservar: function(button, event, opts) {
   		if (this.getGridEventosTaquilla().hasRowSelected() && this.getGridSesionesTaquilla().hasRowSelected()) {
			var evento = this.getGridEventosTaquilla().getSelectedRecord();
			var sesion = this.getGridSesionesTaquilla().getSelectedRecord();

			this.getGridSesionesTaquilla().showComprarWindow(sesion.data['id'], evento.data['asientosNumerados'], UI.i18n.formTitle.reservar, true);

			this.getPanelReservar().show();
			this.getPanelComprar().hide();
		} else
			alert(UI.i18n.message.selectRow);
   	},
   
   marcaPagada: function(idCompra) {
	   
	   var me = this;
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'pago/' + idCompra + '/pagada',
	    	  method: 'POST',
	    	  success: function (response) {
	   
	    		  console.log('Compra marcada como pagada:', response);
	    		  
   			      me.muestraMensajePagoTarjeta(UI.i18n.message.compraRegistradaOk);
   			      me.desahiblitaEstadoBotonesComprar();
			      me.muestraEnlacePdf();
    			  
	    	  }, failure: function (response) {
	    		  
	    		  me.habilitaBotonPagar();
	    		  me.muestraMensajePagoTarjeta(UI.i18n.error.errorRealizaPago);

	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
	    		  
	    		  if (respuesta!=null && respuesta['message']!=null)
	    			  alert(respuesta['message']);
	    		  else
	    			  alert(UI.i18n.error.errorRealizaPago);
	    	  }
	   	  });
   }
});