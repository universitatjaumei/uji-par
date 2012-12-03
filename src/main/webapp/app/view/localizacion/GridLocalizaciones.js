Ext.define('Paranimf.view.localizacion.GridLocalizaciones', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridLocalizaciones',
   store: 'Localizaciones',

   title: UI.i18n.gridTitle.localizaciones,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'Localizaciones',
     dock: 'bottom',
     displayInfo: true
   }],


   initComponent: function() {

      this.columns = [{
         dataIndex: 'id',
         hidden: true
      }, {
         dataIndex: 'nombre',
         text: UI.i18n.field.nameMulti,
         flex: 5
      }, {
          dataIndex: 'totalEntradas',
          text: UI.i18n.field.totalEntradas,
          flex: 1
      }];

      this.callParent(arguments);
   },


   showAddLocalizacionWindow: function() {
      this.createModalWindow('formLocalizaciones').show();
   }
});