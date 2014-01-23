Ext.define('Paranimf.controller.Taquilla', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'taquilla.PanelTaquilla', 'taquilla.GridEventosTaquilla', 'taquilla.GridSesionesTaquilla', 
           'taquilla.FormComprar', 'taquilla.PanelSeleccionarNoNumeradas', 'taquilla.PanelNumeroEntradas'],
   stores: ['EventosTaquilla', 'SesionesTaquilla', 'Horas', 'Minutos'],
   models: ['Evento', 'Compra', 'HoraMinuto'],

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
    	  ref: 'botonAnularPrevia',
    	  selector: 'formComprar #anularPrevia'
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
      	ref: 'totalPrecioCompra',
      	selector: 'panelSeleccionarNoNumeradas label[name=totalPrecios]'
      }, {
        ref: 'importePagado',
        selector: 'formComprar #pasoPagar numberfield[name=importePagado]'
      }, {
        ref: 'importeADevolver',
        selector: 'formComprar #pasoPagar label[name=dineroADevolver]'
      }, {
        ref: 'hiddenTotalPrecio',
        selector: 'formComprar #pasoPagar hiddenfield[name=hiddenTotalPrecio]'
      }, {
        ref: 'horaInicio',
        selector: 'formComprar combobox[name=horaInicio]'
      }, {
        ref: 'horaFin',
        selector: 'formComprar combobox[name=horaFin]'
      }, {
        ref: 'minutoInicio',
        selector: 'formComprar combobox[name=minutoInicio]'
      }, {
        ref: 'minutoFin',
        selector: 'formComprar combobox[name=minutoFin]'
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
         'gridSesionesTaquilla': {
             itemdblclick: this.comprar
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
         'formComprar #anularPrevia': {
        	 click: this.anularPrevia
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
         },
         'formComprar #tipoPago': {
            change: this.muestraOcultaDevolucionImporte
         },

         'formComprar #pasoPagar numberfield[name=importePagado]': {
          change: this.actualizaCambioADevolver
         },

         'formComprar panel[name=panelReservar]': {
            beforerender: this.setFechaHoraFinalReserva
         }
      });
      
	  this.intervalEstadoPago = {};
   },

    setFechaHoraFinalReserva: function(panel) {
      //console.log("setFechaHoraFinalReserva");
      var seccionSeleccionada = this.getGridSesionesTaquilla().getSelectedRecord();
      this.getReservarHasta().setValue(seccionSeleccionada.data.fechaFinVentaOnline);
      var fecha = new Date();
      fecha = seccionSeleccionada.data.fechaFinVentaOnline;
      panel.down('combobox[name=horaFin]').setValue(fecha.getHours());
      panel.down('combobox[name=minutoFin]').setValue(fecha.getMinutes());
    },

    actualizaCambioADevolver: function() {
      var importeADevolver = this.getImportePagado().value - this.getHiddenTotalPrecio().value;
      this.getImporteADevolver().setText(UI.i18n.field.importeADevolver + importeADevolver.toFixed(2) + " €");
    },

    muestraOcultaDevolucionImporte: function(combo, newvalue, oldvalue) {
      if (newvalue == 'metalico')
        this.getImportePagado().show();
      else
        this.getImportePagado().hide();
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
   				var numeroEntradasAulaTeatro = (panel[0].down('numberfield[name=aulaTeatro]').getValue() == '')?0:panel[0].down('numberfield[name=aulaTeatro]').getValue();
   				var precioNormal = (this.precios[key] != undefined)?this.precios[key]['normal']:0;
   				var precioDescuento = (this.precios[key] != undefined)?this.precios[key]['descuento']:0;
   				var precioInvitacion = (this.precios[key] != undefined)?this.precios[key]['invitacion']:0;
   				var precioAulaTeatro = (this.precios[key] != undefined)?this.precios[key]['aulaTeatro']:0;

   				precio += numeroEntradasNormal*precioNormal + numeroEntradasDescuento*precioDescuento + numeroEntradasAulaTeatro*precioAulaTeatro + numeroEntradasInvitacion*precioInvitacion;
   			}
   		}
   		this.getTotalPrecioCompra().setText(UI.i18n.field.totalCompra + precio.toFixed(2) + EURO);
   	},
   
   iniciaFormComprar: function() {
	   
	   this.idCompra = null;
	   this.idPagoTarjeta = null;
	   this.butacasSeleccionadas = [];  
	   
	   var layout = this.getFormComprarCards().getLayout();
	   layout.setActiveItem(0);
	  
	   this.cambiarEstadoBotonesComprar();
   },
   
   	muestraDisponibles: function() {
   		var DISPONIBLES = UI.i18n.field.entradesDisponibles;
   		for (var key in this.disponibles) {
   			var panel = Ext.ComponentQuery.query('panelSeleccionarNoNumeradas panelNumeroEntradas[name=' + key + ']');
   			
   			if (panel != undefined && panel.length >= 1)
   				panel[0].down('label[name=disponibles]').setText(DISPONIBLES + this.disponibles[key]);
   		}
   	},
   	
   	anularPrevia: function() {
   		//console.log(this.idCompraPrevia);
   		//console.log(this.butacasSeleccionadasPrevia);
   		
   		var textoButacas = "";
   		
   		for (var i=0; i<this.butacasSeleccionadasPrevia.length; i++)
   		{
   			var butaca = this.butacasSeleccionadasPrevia[i];
   			var filaNum = "";

   			if (butaca['fila']!=null && butaca['numero']!=null)
   				filaNum = " fila: " + butaca['fila'] + ", num: " + butaca['numero'];
   			//console.log(butaca['localizacion']);
   			textoButacas += UI.i18n.tipos[butaca['localizacion']]  + filaNum + ' (' + UI.i18n.tipoEntrada[butaca['tipo']] + ')<br>';  			
   		}
   		
   		var textoConfirm = UI.i18n.message.anularPreviaIntro + '<br><b>' + this.tituloEventoPrevio + '</b><br>' + 
   				UI.i18n.message.anularPreviaSesion + '<b>' + this.sesionPrevia + '</b><br>' + textoButacas;
   		
   		Ext.Msg.confirm(UI.i18n.formTitle.anularPrevia, textoConfirm, function (id, value) {
   			if (id == 'yes')
   			{
   				this.llamaAnularPrevia();
   			}
   	    }, this);
   	},
   	
   	llamaAnularPrevia: function() {
   		//console.log('llamaAnularPrevia');
   		
   		var me = this;
   		
   		Ext.Ajax.request({
            url : urlPrefix + 'compra/' + this.idSesionPrevia + '/' + this.idCompraPrevia,
            method: 'PUT',
            success: function (response) {
        	  
              me.butacasSeleccionadasPrevia = null;
              me.idCompraPrevia = null;
              me.cambiaVisibilidadBotonAnularPrevia();
            	
              me.getFormComprar().up('window').close();
              me.comprar();
              
              alert('Compra anul·lada correctament');
              
            }, failure: function (response) {
              alert(UI.i18n.error.anularCompraReserva);
            }
         });
   	},

   	ocultaDescuentosNoDisponiblesNoNumeradas: function() {

   		var tipoEvento = this.getGridEventosTaquilla().getSelectedRecord().data['parTiposEvento']['nombreEs'].toLowerCase();
   		
   		for (var localizacion in this.precios) {

   			var precioNormal = this.precios[localizacion]['normal'];
   			var precioDescuento = this.precios[localizacion]['descuento'];
   			var panel = Ext.ComponentQuery.query('panelSeleccionarNoNumeradas panelNumeroEntradas[name=' + localizacion + ']');

        //TODO -> esta funcion es del par-public, y si no está puesto, falla
        if (descuentoNoDisponible != undefined) {
     			if (descuentoNoDisponible(tipoEvento, precioNormal, precioDescuento))
     			{
     				panel[0].down('numberfield[name=descuento]').hide();
     				panel[0].down('panel[name=preuDescuento]').hide();
     			}
     			else
     			{
     				panel[0].down('numberfield[name=descuento]').show();
     				panel[0].down('panel[name=preuDescuento]').show();
     			}
        }
   		}
   	},   	

   	muestraPrecios: function() {
   		var PRECIOS = UI.i18n.field.precioPorEntrada;
   		var EURO = ' &euro;';
   		for (var key in this.precios) {
   			var panel = Ext.ComponentQuery.query('panelSeleccionarNoNumeradas panelNumeroEntradas[name=' + key + ']');
   			if (panel != undefined && panel.length >= 1) {
   				
   				var normal = this.precios[key]['normal'];
   				if (normal != null)
   					normal = normal.toFixed(2);
   				
   				var descuento = this.precios[key]['descuento'];
   				if (descuento != null)
   					descuento = descuento.toFixed(2);
   				
   				var invitacion = this.precios[key]['invitacion'];
   				if (invitacion != null)
   					invitacion = invitacion.toFixed(2);
   				
   				var aulaTeatro = this.precios[key]['aulaTeatro'];
   				if (aulaTeatro != null)
   					aulaTeatro = aulaTeatro.toFixed(2);   				
   				
   				panel[0].down('panel[name=preuNormal]').update(PRECIOS + normal + EURO);
   				panel[0].down('panel[name=preuDescuento]').update(PRECIOS + descuento + EURO);
   				panel[0].down('panel[name=preuInvitacion]').update(PRECIOS + invitacion + EURO);
   				panel[0].down('panel[name=preuAulaTeatro]').update(PRECIOS + aulaTeatro + EURO);
   			}
   		}
   	},

   	activaCamposCompra: function() {
   		for (var key in this.disponibles) {
   			var hayDisponibles = (this.disponibles[key]==0)?false:true;
   			var hayPrecioNormal = false, hayPrecioDescuento = false, hayPrecioInvitacion = false;
        var hayPrecioAulaTeatro = false;
   			var panel;
   			
   			if (this.precios[key] != undefined && this.precios[key] != '' && this.precios[key] != 0) {
   				hayPrecioNormal = (this.precios[key]['normal'] == undefined)?false:true;
   				hayPrecioDescuento = (this.precios[key]['descuento'] == undefined)?false:true;
   				hayPrecioInvitacion = (this.precios[key]['invitacion'] == undefined)?false:true;
   				hayPrecioAulaTeatro = (this.precios[key]['aulaTeatro'] == undefined)?false:true;
   			}

        /*TODO
          -- no se podra hacer con un switch ya que las claves no seran fijas
          -- parece facil, realizar la busqueda del panel utilizando la key en lugar del getXXX...
          -- comprobar que, si no se encuentra el panel, que no haga nada
        */
   			if (hayDisponibles) {
   				/*switch (key) {
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
   				}*/
          var panel = Ext.ComponentQuery.query('panelSeleccionarNoNumeradas panelNumeroEntradas[name=' + key + ']');
          //console.log(panel);
          if (panel) {
            panel = panel[0];
            //console.log(panel.down('numberfield[name=normal]'));
  				  panel.down('numberfield[name=normal]').setDisabled(!hayPrecioNormal);
  				  panel.down('numberfield[name=descuento]').setDisabled(!hayPrecioDescuento);
  				  panel.down('numberfield[name=invitacion]').setDisabled(!hayPrecioInvitacion);
  				  panel.down('numberfield[name=aulaTeatro]').setDisabled(!hayPrecioAulaTeatro);
          }
   			}
   		}
   	},
   
   	panelSeleccionarNoNumeradasCreado: function() {
      var me = this;
      var idSesion = this.getGridSesionesTaquilla().getSelectedRecord().data['id'];
     
      this.cargaLocalizaciones(idSesion, function() {
        me.cargaPrecios(idSesion, function() {
		      me.cargaDisponibles(idSesion, function() {
            me.muestraDisponibles();
            me.muestraPrecios();
            me.activaCamposCompra();
            me.ocultaDescuentosNoDisponiblesNoNumeradas();
		      });
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
	   me.getFormComprar().setLoading(UI.i18n.message.loading);
	   me.rellenaDatosPasoPagar("-");
     this.getHiddenTotalPrecio().setValue("0");
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + idSesion + '/importe',
	    	  method: 'POST',
	    	  jsonData: butacas,
	    	  success: function (response) {
            me.getFormComprar().setLoading(false);
	    		  var importe = Ext.JSON.decode(response.responseText, true);
            me.getHiddenTotalPrecio().setValue(importe);
	    		  me.rellenaDatosPasoPagar(importe.toFixed(2));
	    		   
	    	  }, failure: function (response) {
            me.getFormComprar().setLoading(false);
	    		  var respuesta = Ext.JSON.decode(response.responseText, true);
	    		  //console.log(respuesta);
	    		  
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
	   	var tipos = {};tipos['normal'] = '';tipos['descuento'] = '';tipos['invitacion'] = ''; tipos['aulaTeatro'] = '';
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
     me.getFormComprar().setLoading(UI.i18n.message.loading);
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + idSesion,
	    	  method: 'POST',
	    	  jsonData: this.butacasSeleccionadas,
	    	  success: function (response) {
	    		  me.getFormComprar().setLoading(false);
	    		   var respuesta = Ext.JSON.decode(response.responseText, true);

	    		   me.idCompra = respuesta['id'];
	    		   me.uuidCompra = respuesta['uuid'];
	    		  
	    		   if (tipoPago == 'metalico')
	    		   {
	    			   me.marcaPagada(me.idCompra);
	    		   }   
	    		   else
	    		   {
	    			   var tituloEvento = me.getGridEventosTaquilla().getSelectedRecord().data['tituloVa'];
	    			   me.pagarConTarjeta(respuesta['id'], tituloEvento);
	    		   }   
	    		   
	    	  }, failure: function (response) {
	    		  me.getFormComprar().setLoading(false);
	    		  //console.log(respuesta);
	    		  
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
	   me.getFormComprar().setLoading(UI.i18n.message.loading);
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'reserva/' + idSesion,
	    	  method: 'POST',
	    	  jsonData: {butacasSeleccionadas:this.butacasSeleccionadas, desde:this.getReservarDesde().getValue(), hasta:this.getReservarHasta().getValue()
	    		  		 , observaciones: this.getObservacionesReserva().getValue(), horaInicial: this.getHoraInicio().value, horaFinal: this.getHoraFin().value,
                 minutoInicial: this.getMinutoInicio().value, minutoFinal: this.getMinutoFin().value},
	    	  success: function (response) {
            me.getFormComprar().setLoading(false);
            me.habilitaBotonReservar();
            me.cerrarComprar();
	    		  
	    	  }, failure: function (response) {
            me.getFormComprar().setLoading(false); 
            //console.log(respuesta);
	    		  
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
	   me.getFormComprar().setLoading(UI.i18n.message.loading);
	   me.muestraMensajePagoTarjeta(UI.i18n.message.pagoTarjetaEnviando);
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'pago/' + id,
	    	  method: 'POST',
	    	  jsonData: concepto,
	    	  success: function (response) {
	           me.getFormComprar().setLoading(false);
	    		  //console.log('Pago con tarjeta aceptado:', response);
	    		  
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
	    		 me.getFormComprar().setLoading(false);
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
	   me.getFormComprar().setLoading(UI.i18n.message.loading);
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'pago/' + idPago,
	    	  method: 'GET',
	    	  success: function (response) {
	         me.getFormComprar().setLoading(false);
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
	    		  me.getFormComprar().setLoading(false);
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
	   this.getVerEntrada().show();
   },
   
   verEntrada: function() {
	   
	   this.getFormComprar().up('window').close();
	   this.comprar();

	   if (this.windowEntrada != null)
	   {
		   // Cerramos para evitar imprimir las entradas anteriores
		   this.windowEntrada.close();
	   }
	   
	   var href = urlPrefix + 'compra/' + this.uuidCompra + '/pdftaquilla';
	   this.windowEntrada = window.open(href, 'Imprimir entrada');
	   this.windowEntrada.print();
   },
   
   guardarDatosCompraPrevia: function() {
	   this.butacasSeleccionadasPrevia = this.butacasSeleccionadas;
	   this.idCompraPrevia = this.idCompra;
	   this.idSesionPrevia = this.getGridSesionesTaquilla().getSelectedRecord().data['id'];
	   this.tituloEventoPrevio = this.getGridEventosTaquilla().getSelectedRecord().data['tituloVa'];
	   this.sesionPrevia = Ext.Date.format(this.getGridSesionesTaquilla().getSelectedRecord().data['fechaCelebracion'], 'd/m/Y H:i');
   },

  cargaLocalizaciones: function(sesionId, callback) {
    /*TODO
      --get localizaciones de la sesion
      --para cada localizacion
        --añadir en el panel "localizaciones" del panel "panelSeleccionarNoNumeradas"
          --un fieldset con
            {
              columnWidth: 1/2,
              title: nombre de la localización,
              items: [{
                name: 'codigo_localizacion',
                xtype: 'panelNumeroEntradas'
              }]
            }
    */
    //console.log("cargaLocalizaciones");
    var me = this;
    this.getFormComprar().setLoading(UI.i18n.message.loading);
    
    Ext.Ajax.request({
      url : urlPrefix + 'evento/sesion/' + sesionId + '/localizacion',
      method: 'GET',
      
      success: function (response) {
        me.getFormComprar().setLoading(false);
        var jsonData = Ext.decode(response.responseText);

        for (var i=0; i<jsonData.length; i++) {
          var localizacion = jsonData[i];
          console.log(localizacion);
          
          var field = new Ext.form.Panel({
            columnWidth: 1/2,
            title: UI.i18n.legends.entrades + " " + localizacion.nombreVa.toUpperCase(),
            items: [{
              name: localizacion.codigo,
              xtype: 'panelNumeroEntradas'
            }]
          });

          me.getLocalizacionesNoNumeradas().add(field);
        }
        callback();
      },

      failure: function (response) {
        me.getFormComprar().setLoading(false);
        alert(UI.i18n.error.loadingLocalizaciones);
      }
    });
  },
   
   cargaPrecios: function(sesionId, callback) {
      var me = this;
      this.getFormComprar().setLoading(UI.i18n.message.loading);
      Ext.Ajax.request({
    	  url : urlPrefix + 'compra/' + sesionId + '/precios',
    	  method: 'GET',
    	  success: function (response) {
          me.getFormComprar().setLoading(false);
	    		var jsonData = Ext.decode(response.responseText);

	    		me.precios = {};
	    		  
     		  for (var i=0; i<jsonData.data.length; i++)
	    		{
            var sesion = jsonData.data[i];
            me.precios[sesion.localizacion.codigo] = {normal:sesion.precio, descuento:sesion.descuento, invitacion:sesion.invitacion, aulaTeatro:sesion.aulaTeatro};
	    		}
          callback();
	    	}, failure: function (response) {
          me.getFormComprar().setLoading(false);
	    	  alert(UI.i18n.error.loadingPreciosNoNumeradas);
	    	}
	   	});
   },
   
   cargaDisponibles: function(sesionId, callback) {
	   
	   var me = this;
     me.getFormComprar().setLoading(UI.i18n.message.loading);
	   
	   Ext.Ajax.request({
	    	  url : urlPrefix + 'compra/' + sesionId + '/disponibles',
	    	  method: 'GET',
	    	  success: function (response) {
	    		  me.getFormComprar().setLoading(false);
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
            me.getFormComprar().setLoading(false);
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

	   
	   var me = this;
	   
	   // Llamamos al iframe de butacas para que nos pase las butacas seleccionadas
	   pm({
		   target: window.frames[1],
		   type: 'butacas',
		   data: {},
		   success: function(butacas){
			   console.log('Respuesta:', butacas);
			   
			   me.butacasSeleccionadas = butacas;
			   
			   me.avanzarAPasoDePago(butacas);
		  }
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
         
         this.getGridSesionesTaquilla().setTitle(UI.i18n.gridTitle.sesionesCompras + ': ' + record[0].get('tituloVa'));

         storeSesiones.getProxy().url = urlPrefix + 'evento/' + eventoId + '/sesiones?activos=true';
         storeSesiones.load();
      }
   },   

   comprar: function(button, event, opts) {
   		if (this.getGridEventosTaquilla().hasRowSelected() && this.getGridSesionesTaquilla().hasRowSelected()) {
  			var evento = this.getGridEventosTaquilla().getSelectedRecord();
  			var sesion = this.getGridSesionesTaquilla().getSelectedRecord();
        //console.log(evento, sesion);

        var title = UI.i18n.formTitle.comprar + " -> " + UI.i18n.message.evento + ": " + evento.data.tituloVa + " - " + UI.i18n.message.sessio + ": " + Ext.Date.format(sesion.data.fechaCelebracion, 'd/m/Y H:i');
  			this.getGridSesionesTaquilla().showComprarWindow(sesion.data['id'], evento.data['asientosNumerados'], title, false);
  			
  			this.getPanelComprar().show();
  			this.getPanelReservar().hide();
  			
  			this.cambiaVisibilidadBotonAnularPrevia();
  		} else
  			alert(UI.i18n.message.selectRow);
   	},
   	
   	cambiaVisibilidadBotonAnularPrevia: function() {
		if (this.butacasSeleccionadasPrevia)
			this.getBotonAnularPrevia().show();
		else
			this.getBotonAnularPrevia().hide();
   	},
   
   	reservar: function(button, event, opts) {
   		if (this.getGridEventosTaquilla().hasRowSelected() && this.getGridSesionesTaquilla().hasRowSelected()) {
			var evento = this.getGridEventosTaquilla().getSelectedRecord();
			var sesion = this.getGridSesionesTaquilla().getSelectedRecord();
      var title = UI.i18n.formTitle.reservar + " -> " + UI.i18n.message.evento + ": " + evento.data.tituloVa + " - " + UI.i18n.message.sessio + ": " + Ext.Date.format(sesion.data.fechaCelebracion, 'd/m/Y H:i');

			this.getGridSesionesTaquilla().showComprarWindow(sesion.data['id'], evento.data['asientosNumerados'], title, true);

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
				  me.guardarDatosCompraPrevia();
				  me.cambiaVisibilidadBotonAnularPrevia();
    			  
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