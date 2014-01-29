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
        dataIndex: 'tarifa',
        hidden: true,
        text: UI.i18n.field.idTarifa
      }, {
        dataIndex: 'localizacion_nombre',
        text: UI.i18n.field.localizacion,
        flex: 2,
        sortable: false
      }, {
        dataIndex: 'tarifa_nombre',
        text: UI.i18n.field.tarifa,
        flex: 2
      }, {
        dataIndex: 'precio',
        text: UI.i18n.field.precio,
        flex: 1,
        renderer: Ext.util.Format.numberRenderer("0.00")
      }];

      this.callParent(arguments);
   },


   showAddPrecioWindow: function() {
      this.createPercentageModalWindow('formPrecios').show();
   }
});