Ext.define('Paranimf.view.tarifa.GridTarifas', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridTarifas',
   store: 'Tarifas',

   title: UI.i18n.gridTitle.tarifas,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'Tarifas',
     dock: 'bottom',
     displayInfo: true
   }],


   initComponent: function() {

      this.columns = [{
         dataIndex: 'id',
         hidden: true,
         text: UI.i18n.field.idIntern
      }, {
          dataIndex: 'nombre',
          text: UI.i18n.field.name_va,
          flex: 5
      }, {
        dataIndex: 'isPublico',
        text: UI.i18n.field.isPublico,
        flex: 1,
        renderer: function(value) {
          if (value == 'on')
            return "Sí"
        }
      }, {
        dataIndex: 'defecto',
        text: UI.i18n.field.defecto,
        flex: 1,
        renderer: function(value) {
          if (value == 'on')
            return "Sí"
        }
      }];

      this.callParent(arguments);
   },


   showAddTarifaWindow: function() {
      this.createPercentageModalWindow('formTarifas').show();
   }
});