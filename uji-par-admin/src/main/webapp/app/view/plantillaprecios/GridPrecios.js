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
        hidden: true,
        text: UI.i18n.field.idIntern
      }, {
        dataIndex: 'plantillaPrecios',
        hidden: true,
        text: UI.i18n.field.plantillaPreusInterna
      }, {
        dataIndex: 'parLocalizacione',
        hidden: true,
        text: UI.i18n.field.idLocalitzacio
      }, {
        dataIndex: 'localizacion_nombre',
        text: UI.i18n.field.localizacion,
        flex: 2,
        sortable: false
      }, {
        dataIndex: 'precio',
        text: UI.i18n.field.precio,
        flex: 1,
        renderer: Ext.util.Format.numberRenderer("0.00")
      },{
        dataIndex: 'descuento',
        text: UI.i18n.field.descuento,
        flex: 1,
        renderer: Ext.util.Format.numberRenderer("0.00")
      },{
        dataIndex: 'invitacion',
        text: UI.i18n.field.invitacion,
        flex: 1,
        renderer: Ext.util.Format.numberRenderer("0.00")
      }];

      this.callParent(arguments);
   },


   showAddPrecioWindow: function() {
      this.createPercentageModalWindow('formPrecios').show();
   }
});