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

   items: [{
	   xtype: 'panel',
	   id: 'formComprar',
	   frame: false,
	   border: 0,
	   items: [{
	        xtype : "component",
	        id    : "iframeButacas",
	        autoEl : {
	            tag : "iframe",
	            src : ""
	        }
	    }]
   }]
   
});