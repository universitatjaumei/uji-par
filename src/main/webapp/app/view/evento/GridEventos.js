Ext.define('Paranimf.view.evento.GridEventos', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridEventos',
   store: 'Eventos',

   title: UI.i18n.gridTitle.eventos,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'Eventos',
     dock: 'bottom',
     displayInfo: true
   }],

   initComponent: function() {

      this.columns = [{
         dataIndex: 'id',
         hidden: true
      }, {
          dataIndex: 'parTipoEvento',
          text: UI.i18n.field.type,
          flex: 2,
          renderer: function (val, p) {
        	  return val["nombre"];
          }
      }, {
         dataIndex: 'titulo',
         text: UI.i18n.field.title,
         flex: 5
      }, {
         dataIndex: 'fechaInicio',
         text: UI.i18n.field.startDate,
         flex: 3
      }, {
          dataIndex: 'fechaFin',
          text: UI.i18n.field.endDate,
          flex: 3
      }];

      this.callParent(arguments);
   },


   showAddEventoWindow: function() {
      this.createModalWindow('formEventos', 600, 600).show();
   }
});