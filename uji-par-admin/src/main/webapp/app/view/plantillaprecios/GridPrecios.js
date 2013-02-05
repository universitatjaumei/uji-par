Ext.define('Paranimf.view.plantillaprecios.GridPrecios', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridPrecios',
   store: 'Precios',

   title: UI.i18n.gridTitle.precios,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'Precios',
     dock: 'bottom',
     displayInfo: true
   }],

   initComponent: function() {

      this.columns = [{
        dataIndex: 'id',
        hidden: true
      }, {
        dataIndex: 'plantillaPrecios',
        hidden: true
      }, {
        dataIndex: 'localizacion_id',
        hidden: false
      }, {
        dataIndex: 'localizacion_nombre',
        text: UI.i18n.field.localizacion,
        flex: 2
      }, {
        dataIndex: 'precio',
        text: UI.i18n.field.precio,
        flex: 1
      },{
        dataIndex: 'descuento',
        text: UI.i18n.field.descuento,
        flex: 1
      },{
        dataIndex: 'invitacion',
        text: UI.i18n.field.invitacion,
        flex: 1
      }];

      this.callParent(arguments);
   },


   showAddPrecioWindow: function() {
      this.createModalWindow('formPrecios', 600, 300).show();
   }
});