Ext.define('Paranimf.view.tipoevento.GridTiposEventos', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridTiposEventos',
   store: 'TiposEventos',

   title: UI.i18n.gridTitle.tipoEvento,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'TiposEventos',
     dock: 'bottom',
     displayInfo: true
   }],

   /*forceFit: true,*/

   initComponent: function() {

      this.columns = [{
         dataIndex: 'id',
         hidden: true
      }, {
         dataIndex: 'nombre',
         text: UI.i18n.field.name,
         flex: 5
      }];

      this.callParent(arguments);
   },


   showAddTipoEventoWindow: function() {
      this.createModalWindow('formTiposEventos').show();
   }
});