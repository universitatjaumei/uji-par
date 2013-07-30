Ext.define('Paranimf.view.taquilla.PanelSeleccionarNoNumeradas', {
   extend: 'Ext.panel.Panel',
   alias: 'widget.panelSeleccionarNoNumeradas',
   autoScroll: true,
   border: 0,
   frame: false,
   
   defaults: {
      anchor: '100%',
      xtype: 'textfield',
      labelWidth: 190
   },
   
   buttons: [],
   bbar: [],
   
  
   items: [
		{
			xtype: 'panel',
			layout: 'hbox',
			border: 0,
			frame: false,
			items: [
					{
					    fieldLabel: UI.i18n.field.localizacion,
					    name: 'localizacion',
					    xtype: 'combobox',
					    displayField: 'nombreVa',
					    valueField: 'codigo',
					    store: 'Localizaciones',
					    queryMode: 'local',
					    typeAhead: false,
					    width: 400,
					    editable: false
					},
			        {
			        	xtype: 'label',
			        	text: UI.i18n.field.disponibles,
			        	style: 'margin-left: 20px'
			        },
			        {
			        	name: 'disponibles',
			        	xtype: 'label',
			        	text: '-',
			        	style: 'margin-left: 10px'
			        }
			       ]
		},
		{
			name: 'localizaciones',
			xtype: 'panel',
	    	frame: false,
	    	border: 0,
	    	style: 'margin-top: 20px',
			items:[
			       {
			    	   name: 'anfiteatro',
			    	   xtype: 'panelNumeroEntradas',
			    	   hidden: true
			       },
			       {
			    	   name: 'platea1',
			    	   xtype: 'panelNumeroEntradas',
			    	   hidden: true
			       },
			       {
			    	   name: 'platea2',
			    	   xtype: 'panelNumeroEntradas',
			    	   hidden: true
			       },
			       {
			    	   name: 'discapacitados1',
			    	   xtype: 'panelNumeroEntradas',
			    	   hidden: true
			       },
			       {
			    	   name: 'discapacitados2',
			    	   xtype: 'panelNumeroEntradas',
			    	   hidden: true
			       },
			       {
			    	   name: 'discapacitados3',
			    	   xtype: 'panelNumeroEntradas',
			    	   hidden: true
			       }	       
			]
		}
   ]
});