Ext.define('Paranimf.view.evento.GridSesiones', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridSesiones',
   store: 'Sesiones',

   title: UI.i18n.gridTitle.sesiones,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'Sesiones',
     dock: 'bottom',
     displayInfo: true
   }],

   initComponent: function() {

      this.columns = [{
         dataIndex: 'id',
         hidden: true
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
          format:'d/m/Y H:i',
          xtype: 'datecolumn',
          flex: 1
      }, {
          dataIndex: 'fechaFinVentaOnline',
          text: UI.i18n.field.endOnlineSelling,
          format:'d/m/Y H:i',
          xtype: 'datecolumn',
          flex: 1
      }, {
    	 dataIndex: 'canalInternet',
    	 text: UI.i18n.field.online,
    	 flex: 0.5,
    	 renderer: function (val, p) {
    		 if (val == 1)
    			 return "<img src='resources/images/tick.png' height='16'/>";
    		 else
    			 return "<img src='resources/images/cross.png' height='16' />";
    	 }
      }, {
    	 dataIndex: 'canalTaquilla',
    	 text: UI.i18n.field.taquilla,
    	 flex: 0.5,
    	 renderer: function (val, p) {
    		 if (val == 1)
    			 return "<img src='resources/images/tick.png' height='16' />";
    		 else
    			 return "<img src='resources/images/cross.png' height='16' />";
    	 }
      }];

      this.callParent(arguments);
   },


   showAddSesionWindow: function() {
      this.createModalWindow('formSesiones').show();
   }
});