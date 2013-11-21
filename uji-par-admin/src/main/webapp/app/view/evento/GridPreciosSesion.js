Ext.define('Paranimf.view.evento.GridPreciosSesion', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridPreciosSesion',
   store: 'PreciosSesion',

   title: UI.i18n.gridTitle.precios,

   initComponent: function() {

      this.columns = [{
        dataIndex: 'id',
        hidden: true,
        sortable: false,
        text: UI.i18n.field.idIntern
      }, {
        dataIndex: 'localizacion',
        hidden: true,
        sortable: false,
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
        sortable: false,
        renderer: Ext.util.Format.numberRenderer("0.00")
      },{
        dataIndex: 'descuento',
        text: UI.i18n.field.descuento,
        flex: 1,
        sortable: false,
        renderer: Ext.util.Format.numberRenderer("0.00")
      },{
        dataIndex: 'invitacion',
        text: UI.i18n.field.invitacion,
        flex: 1,
        sortable: false,
        renderer: Ext.util.Format.numberRenderer("0.00")
      },{
        dataIndex: 'aulaTeatro',
        text: UI.i18n.field.aulaTeatro,
        flex: 1,
        sortable: false,
        renderer: Ext.util.Format.numberRenderer("0.00")
      }];

      this.callParent(arguments);
   },


   showAddPrecioSesionWindow: function() {
      this.createPercentageModalWindow('formPreciosSesion').show();
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
          jsonFilas += '"aulaTeatro":' + this.store.getRange()[i].data.aulaTeatro + ',';
          jsonFilas += '"localizacion":{"id": ' + this.store.getRange()[i].data.localizacion + '}';
          jsonFilas += '}';
      }
      jsonFilas = "[" + jsonFilas + "]";
      return jsonFilas;
   }
});