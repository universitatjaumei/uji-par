Ext.define('Paranimf.view.taquilla.FormComprar', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formComprar',
   layout: 'fit',

   url : urlPrefix + 'sesiones',
   
   defaults: {
      anchor: '100%',
      xtype: 'textfield',
      labelWidth: 190
   },
   
   buttons: undefined,
   
   bbar: ['->', {
	   id: 'comprarAnterior',
	   text: UI.i18n.button.anterior
   },{
	   id: 'comprarSiguiente',
	   text: UI.i18n.button.siguiente
   },
   {
	   id: 'comprarCancelar',
	   text: UI.i18n.button.close
   }],

   items: [{
	   xtype: 'panel',
	   id: 'formComprarCards',
	   frame: false,
	   layout: 'card',
	   border: false,
	   items: [{
		        id    : 'pasoSeleccionar',
		        xtype : 'panel',
		        layout: 'card',
		        border: false,
		        frame: false,
		        items: [{
					        id    : 'iframeButacas',
					        xtype : 'component',
					        autoEl : {
					            tag : 'iframe',
					            src : ''
					        }
				        },
				        {
				        	border: false,
				        	frame: false,
					        id    : 'noNumeradas',
					        xtype: 'panelSeleccionarNoNumeradas'
					    }]
		   },
	       {
		       	id: 'pasoPagar',
		       	xtype: 'panel',
		       	layout: {
		       		type: 'vbox',
		       		align: 'center',
		       		pack: 'start'
		       	},
		       	border: 0,
		       	frame: false,
		       	items: [
		       	        {
		       	        	name: 'panelComprar',
		       	        	xtype: 'panel',
		       	        	region: 'center',
		   					frame: false,
		   					align: 'middle',
		   					border: 0,
	   					 	layout: {
			   				    align: 'center',
			   					pack: 'start',
			   					type: 'vbox'
			   				},
			   				defaults: {
			   				 	frame: false,
			   				 	border: 0,
			   				 	margin: '10px',
			   				 	style: {
			   				 		fontSize: '20px'
			   				 	},
			   				 	fieldStyle: {
			   				 		fontSize: '20px'
			   				 	}
			   				},
		       	        	items: [{
								fieldLabel: UI.i18n.field.tipoPago,
							    id: 'tipoPago',
							    xtype: 'combobox',
							    displayField: 'name',
							    valueField : 'value',
							    queryMode: 'local',
							    value: 'metalico',
							    typeAhead: false,
							    editable: false,
							    allowBlank: false,
							    forceSelection:true,
							    store: new Ext.data.SimpleStore({
							      fields: ['value', 'name'],
							        data: [
							          ['metalico', UI.i18n.field.metalico],
							          ['tarjeta', UI.i18n.field.tarjeta]
							        ]
							    })
							 }, {
								xtype: 'label',
							    id: 'total'
							}, {
								xtype: 'hiddenfield',
								name: 'hiddenTotalPrecio'
							}, {
							 	xtype: 'numberfield',
							 	name: 'importePagado',
							 	decimalSeparator: '.',
							 	fieldLabel: UI.i18n.field.importePagado
							}, {
								name: 'dineroADevolver',
								xtype: 'label',
								text: UI.i18n.field.importeADevolver
							}, {
								xtype: 'button',
								id: 'pagar',
								scale: 'large',
								text: UI.i18n.button.pagar
							}, {
							    xtype: 'label',
								name: 'estadoPagoTarjeta'
							}, {
							
												    	 xtype: 'button',
												    	 name: 'verEntrada',
														 scale: 'large',
														 text: UI.i18n.button.verEntrada,
														 hidden: true
												     }
										     ]
		       	        },
		       	        {
		       	        	name: 'panelReservar',
		       	        	xtype: 'panel',
		   					frame: false,
		   					border: 0,
		    		       	defaults: {
			   					 layout: {
			   					        align: 'middle',
			   					        pack: 'center',
			   					        type: 'hbox'
			   					 },
			   					 frame: false,
			   					 border: 0
		    		       	},		       	        	
		       	        	items: [
		       	        	        	{
		       	        	        		xtype: 'panel',
		       	        	        		items: [
		       	        	        		        {
														 xtype: 'panel',
														 layout: 'hbox',
									 					 frame: false,
									 					 border: 0,																 
														 items: [
														         {
														        	 xtype: 'panel',
														        	 layout: 'vbox',
												 					 frame: false,
												 					 border: 0,		       	        	        		        	 
														        	 items: [
													        	        	 		{
													        	        	 			xtype: 'label',
													        	        	 			text: UI.i18n.field.desde
													        	        	 		},
													        	        	 		{
													        	        	 			xtype: 'datepicker',
													        	        	 			name: 'desde'
													        	        	 		}		       	     		       	        	        	 		
														        	 ]
														         },
														         {
														        	 xtype: 'panel',
														        	 layout: 'vbox',
												 					 frame: false,
												 					 border: 0,		 
																	 style: {
																	   	marginLeft: '30px'
																	 },												 					 
														        	 items: [
													        	        	 		{
													        	        	 			xtype: 'label',
													        	        	 			text: UI.i18n.field.hasta
													        	        	 		},
													        	        	 		{
													        	        	 			xtype: 'datepicker',
													        	        	 			name: 'hasta'
													        	        	 		}		       	     		       	        	        	 		
														        	 ]
														         }		       	        	        		         
														 ]
													 }
		       	        	        		]
		       	        	        	},
		       	        	        	{
		       	        	        		style: {
		       	        	        			marginTop: '30px'
		       	        	        		},
		       	        	        		items:[{
					       	        	        		xtype: 'textareafield',
					       	        	        		grow: true,
					       	        	        		name: 'observacionesReserva',
					       	        	        		fieldLabel: UI.i18n.field.observacionesReserva,
					       	        	        		width: 400
		       	        	        				}]
		       	        	        	},
										{
										    items: [{
										   	 xtype: 'button',
												 id: 'reservar',
												 scale: 'large',
												 text: UI.i18n.button.reservar,
												 style: {
												   	margin: '30px'
											     }
										    }]
										}		       	        	        	
								 ]
		       	        },
						{
		   					 layout: {
		   					        align: 'middle',
		   					        pack: 'center',
		   					        type: 'hbox'
		   					 },
		   					 frame: false,
		   					 border: 0,		       	        	
						     items: [{
								    	 xtype: 'button',
								    	 name: 'verEntrada',
										 scale: 'large',
										 text: UI.i18n.button.verEntrada,
										 hidden: true
								     }	 
						     ]
						 }			       	        
				]
	    }]
   }]
});
