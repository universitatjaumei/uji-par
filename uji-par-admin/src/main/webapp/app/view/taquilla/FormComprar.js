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
   
   /*
   buttons: [{
	      xtype: 'button',
	      text: UI.i18n.button.pagar,
	      action: 'pagar'
	   }, {
	      xtype: 'button',
	      text: UI.i18n.button.cancel,
	      handler: function() {
	         this.up('window').close();
	      }
	   }],
    */
   buttons: [],
   
   bbar: ['->', {
	   id: 'comprarAnterior',
	   text: UI.i18n.button.anterior
   },{
	   id: 'comprarSiguiente',
	   text: UI.i18n.button.siguiente
   },
   {
	   id: 'comprarCancelar',
	   text: UI.i18n.button.cancel
   }],

   items: [{
	   xtype: 'panel',
	   id: 'formComprarCards',
	   frame: false,
	   layout: 'card',
	   border: 0,
	   items: [{
		        id    : 'pasoSeleccionar',
		        xtype : 'panel',
		        layout: 'card',
		        items: [{
					        id    : 'iframeButacas',
					        xtype : 'component',
					        autoEl : {
					            tag : 'iframe',
					            src : ''
					        }
				        },
				        {
					        id    : 'noNumeradas',
					        xtype: 'panelSeleccionarNoNumeradas'
					    }]
		   },
	       {
		       	id: 'pasoPagar',
		       	xtype: 'panel',
		       	border: 0,
		       	frame: false,
		       	items: [
						{
						    fieldLabel: UI.i18n.field.tipoPago,
						    id: 'tipoPago',
						    xtype: 'combobox',
						    displayField: 'name',
						    valueField : 'value',
						    queryMode: 'local',
						    value: 'tarjeta',
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
									xtype: 'label',
								    id: 'total',
								    style: {
								    	fontSize: '30px',
								    	margin: '30px'
								    }
							 }]
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
								 id: 'pagar',
								 scale: 'large',
								 text: UI.i18n.button.pagar
						     }]
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
						    	 xtype: 'label',
								 name: 'estadoPagoTarjeta',
							     style: {
							    	fontSize: '20px',
							    	margin: '30px',
							    	textAlign: 'center',
							    	display: 'block'
							     }
						     }]
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
								    	 xtype: 'panel',
										 name: 'verEntrada',
										 frame: false,
										 border: 0,
										 hidden: true
						             }	 
						     ]
						 },
				]
	    }]
   }]
});