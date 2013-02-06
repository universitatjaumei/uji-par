Ext.define('Paranimf.view.plantillaprecios.GridPlantillas', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridPlantillas',
   store: 'PlantillasPrecios',

   title: UI.i18n.gridTitle.plantillasprecios,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'PlantillasPrecios',
     dock: 'bottom',
     displayInfo: true
   }],

   initComponent: function() {

      this.columns = [{
        dataIndex: 'id',
        hidden: true
      }, {
        dataIndex: 'nombre',
        text: UI.i18n.field.name_va,
        flex: 1
      }];

      this.callParent(arguments);
   },


   showAddPlantillaWindow: function() {
      this.createModalWindow('formPlantillas', 600, 100).show();
   }
});