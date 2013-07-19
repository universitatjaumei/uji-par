Ext.define('Paranimf.view.taquilla.GridSesionesTaquilla', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridSesionesTaquilla',
   store: 'SesionesTaquilla',

   title: UI.i18n.gridTitle.sesiones,

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
    }, {
      dataIndex: 'plantillaPrecios_nombre',
      text: UI.i18n.field.plantillaprecios,
      flex: 1
    }];

    this.callParent(arguments);
    
    //this.getDockedItems('toolbar[dock=top]')[0].hide();
  },

  showComprarWindow: function() {
	this.createModalWindow('formComprar', 600, 600).show();

	Ext.Ajax.request({
	    url: 'http://google.es',
	    success: function(response){
	        Ext.getCmp('formComprar').update( response.responseText );
	    }
	});	
  }

});