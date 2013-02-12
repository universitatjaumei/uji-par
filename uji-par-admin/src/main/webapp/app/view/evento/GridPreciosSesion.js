Ext.define('Paranimf.view.evento.GridPreciosSesion', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridPreciosSesion',
   store: 'PreciosSesion',

   title: UI.i18n.gridTitle.precios,

   initComponent: function() {

      this.columns = [{
        dataIndex: 'id',
        hidden: true,
        sortable: false
      }, {
        dataIndex: 'localizacion',
        hidden: false,
        sortable: false
      }, {
        dataIndex: 'localizacion_nombre',
        text: UI.i18n.field.localizacion,
        flex: 2,
        sortable: false
      }, {
        dataIndex: 'precio',
        text: UI.i18n.field.precio,
        flex: 1,
        sortable: false
      },{
        dataIndex: 'descuento',
        text: UI.i18n.field.descuento,
        flex: 1,
        sortable: false
      },{
        dataIndex: 'invitacion',
        text: UI.i18n.field.invitacion,
        flex: 1,
        sortable: false
      }];

      this.callParent(arguments);
   },


   showAddPrecioSesionWindow: function() {
      this.createModalWindow('formPreciosSesion').show();
   },

   toJSON: function() {
    var jsonFilas = "";

      for (var i=0;i<this.store.getCount();i++) {
         if (jsonFilas != "")
            jsonFilas += ",";

          jsonFilas += '{';
          jsonFilas += '"descuento":' + this.store.getRange()[i].data.descuento + ',';
          jsonFilas += '"precio":' + this.store.getRange()[i].data.precio + ',';
          jsonFilas += '"invitacion":' + this.store.getRange()[i].data.invitacion + ',';
          jsonFilas += '"localizacion":{"id": ' + this.store.getRange()[i].data.localizacion + '}';
          jsonFilas += '}'
      }
      jsonFilas = "[" + jsonFilas + "]";
      return jsonFilas;
   }
});