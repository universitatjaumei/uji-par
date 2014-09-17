Ext.define('Paranimf.view.taquilla.GridEventosTaquilla', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridEventosTaquilla',
   store: 'EventosTaquilla',

   title: UI.i18n.gridTitle.eventos,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'EventosTaquilla',
     dock: 'bottom',
     displayInfo: true
   }],

   initComponent: function() {

      this.columns = [{
        dataIndex: 'id',
        hidden: true,
        text: UI.i18n.field.idIntern
      }, {
        dataIndex: 'asientosNumerados',
        hidden: true,
        text: UI.i18n.field.asientosNumerados,
        renderer: function(val) {
          if (val ==0)
            return 'No';
          else
            return 'Sí';
        }
      }, {
        dataIndex: 'parTiposEvento',
        text: UI.i18n.field.type,
        flex: 2,
        renderer: function (val, p) {
          return val["nombreVa"];
        }
      }, {
        dataIndex: 'tituloVa',
        text: UI.i18n.field.title_va,
        flex: 5
      },{
          dataIndex: 'rssId',
          text: UI.i18n.field.rssId,
          hidden: true
      }];

      this.callParent(arguments);
      
      this.getDockedItems('toolbar[dock=top]')[0].hide();
   },

});