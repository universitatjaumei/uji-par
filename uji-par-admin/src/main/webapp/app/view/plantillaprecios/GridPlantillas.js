Ext.define('Paranimf.view.plantillaprecios.GridPlantillas', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridPlantillas',
   store: 'PlantillasPreciosEditables',

   title: UI.i18n.gridTitle.plantillasprecios,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'PlantillasPreciosEditables',
     dock: 'bottom',
     displayInfo: true
   }],

   initComponent: function() {

      this.columns = [{
        dataIndex: 'id',
        hidden: true,
        text: UI.i18n.field.idIntern
      }, {
        dataIndex: 'idSala',
        hidden: true,
        text: UI.i18n.field.idSala
      }, {
        dataIndex: 'nombre',
        text: UI.i18n.field.name_va,
        flex: 1
      }, {
        dataIndex: 'nombreSala',
        text: UI.i18n.field.sala,
        flex: 1
      }];

      this.callParent(arguments);
   },


   showAddPlantillaWindow: function() {
      this.createPercentageModalWindow('formPlantillas').show();
   }
});