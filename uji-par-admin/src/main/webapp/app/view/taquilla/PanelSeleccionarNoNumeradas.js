Ext.define('Paranimf.view.taquilla.PanelSeleccionarNoNumeradas', {
   extend: 'Ext.panel.Panel',
   alias: 'widget.panelSeleccionarNoNumeradas',
   autoScroll: true,
   border: false,
   defaults: {
      anchor: '100%',
      labelWidth: 190
   },
  
   items: [
		{
			name: 'localizaciones',
			xtype: 'panel',
			layout: 'column',
	    	defaults: [{
	    		xtype: 'fieldset'
	    	}],
			items:[
				{
					columnWidth: 1/2,
					title: UI.i18n.legends.entradesAmfiteatre,
					items: [{
		    	   		name: 'anfiteatro',
		    	   		xtype: 'panelNumeroEntradas'
		       		}]
		       	},
		       	{
		       		columnWidth: 1/2,
		       		title: UI.i18n.legends.entradesPlatea1,
		       		items: [{
		    	   		name: 'platea1',
		    	   		xtype: 'panelNumeroEntradas'
		       		}]
		       	},
		       {
		       		columnWidth: 1/2,
		       		title: UI.i18n.legends.entradesPlatea2,
		       		items: [{
		    	   		name: 'platea2',
		    	   		xtype: 'panelNumeroEntradas'
		       		}]
		       	},
		       	{
		       		columnWidth: 1/2,
		       		title: UI.i18n.legends.entradesDiscapacitados1,
		       		items: [{
		    	   		name: 'discapacitados1',
		    	   		xtype: 'panelNumeroEntradas'
		       		}]
		       	},{
		       		columnWidth: 1/2,
		       		title: UI.i18n.legends.entradesDiscapacitados2,
		       		items: [{
		    	   		name: 'discapacitados2',
		    	   		xtype: 'panelNumeroEntradas'
		       		}]
		       	}/*,
		       {
		       		columnWidth: 1/2,
		       		title: UI.i18n.legends.entradesDiscapacitados3,
		       		items: [{
		    	   		name: 'discapacitados3',
		    	   		xtype: 'panelNumeroEntradas'
		      		}]
		      	}*/
			]
		},
		{
			xtype: 'label',
			text: UI.i18n.field.totalCompra,
			name: 'totalPrecios',
			style: {
		    	fontSize: '13px',
		    	fontWeight: 'bold'
		    }
		}
   ]
});