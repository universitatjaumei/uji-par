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
		        id    : "pasoSeleccionar",
		        xtype : "panel",
		        items: [{
			        id    : "iframeButacas",
			        xtype : "component",
			        autoEl : {
			            tag : "iframe",
			            src : ""
			        }
		        }]
		   },
	       {
		       	id: "pasoPagar",
		       	html: "Paso de pagar"
	       }]
   }]
   
});