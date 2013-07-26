Ext.define('Paranimf.view.taquilla.PanelNumeroEntradas', {
   extend: 'Ext.panel.Panel',
   alias: 'widget.panelNumeroEntradas',
   autoScroll: true,
   border: 0,
   frame: false,
   
   defaults: {
      anchor: '100%',
      xtype: 'textfield',
      labelWidth: 190
   },
   
   items: [
           {
        	   name: 'normal',	    	        	   
        	   xtype: 'numberfield',
               fieldLabel: UI.i18n.field.normal,
               value: 0,
               minValue: 0
           },
           {
        	   name: 'descuento',	    	        	   
        	   xtype: 'numberfield',
               fieldLabel: UI.i18n.field.descuento,
               value: 0,
               minValue: 0
           },
           {
        	   name: 'invitacion',	    	        	   
        	   xtype: 'numberfield',
               fieldLabel: UI.i18n.field.invitacion,
               value: 0,
               minValue: 0
           }			    	           
   ]
});