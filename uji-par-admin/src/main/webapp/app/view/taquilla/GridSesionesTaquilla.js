Ext.define('Paranimf.view.taquilla.GridSesionesTaquilla', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridSesionesTaquilla',
   store: 'SesionesTaquilla',

   title: UI.i18n.gridTitle.comprar,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'SesionesTaquilla',
     dock: 'bottom',
     displayInfo: true
   }],
   
   tbar: [{
      xtype: 'button',
      text: UI.i18n.button.comprar,
      action: 'comprar'
   }],   
   
   comprar: function() {
	 console.log('COMPRAR ENTRADA');  
   },

   initComponent: function() {

    this.columns = [{
       dataIndex: 'id',
       hidden: true
    }, {
      hidden: true,
      dataIndex: 'horaCelebracion'
    }, {
      dataIndex: 'fechaCelebracion',
      text: UI.i18n.field.eventDate,
      format:'d/m/Y H:i',
      xtype: 'datecolumn',
      flex: 1
    }, {
      dataIndex: 'horaAperturaPuertas',
      text: UI.i18n.field.opening,
      flex: 1
    }, {
      dataIndex: 'fechaInicioVentaOnline',
      text: UI.i18n.field.startOnlineSelling,
      format:'d/m/Y',
      xtype: 'datecolumn',
      flex: 1
    }, {
      dataIndex: 'fechaFinVentaOnline',
      text: UI.i18n.field.endOnlineSelling,
      format:'d/m/Y',
      xtype: 'datecolumn',
      flex: 1
    }];

    this.callParent(arguments);
    
    //this.getDockedItems('toolbar[dock=top]')[0].hide();
  },

  showComprarWindow: function(idSesion, asientosNumerados) {
	console.log("showComprarWindow: ", idSesion	);  
	  
	this.createModalWindow('formComprar', 700, 700).show();

	var cardLayout = Ext.getCmp('pasoSeleccionar').getLayout();
	
	if (asientosNumerados)
	{
		cardLayout.setActiveItem(0);
		Ext.getDom('iframeButacas').src = urlPublic + "/rest/entrada/butacasFragment/" + idSesion;
	}
	else
	{
		cardLayout.setActiveItem(1);
	}
  }

});