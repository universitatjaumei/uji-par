Ext.define('Paranimf.view.evento.GridEventos', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridEventos',
   store: 'Eventos',

   title: UI.i18n.gridTitle.eventos,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'Eventos',
     dock: 'bottom',
     displayInfo: true
   }],

   initComponent: function() {

      this.columns = [{
        dataIndex: 'id',
        hidden: true
      }, {
        dataIndex: 'asientosNumerados',
        hidden: true
      }, {
        dataIndex: 'parTipoEvento',
        text: UI.i18n.field.type,
        flex: 2,
        renderer: function (val, p) {
          return val["nombreEs"];
        }
      }, {
        dataIndex: 'tituloEs',
        text: UI.i18n.field.title,
        flex: 5
      }, {
        dataIndex: 'tituloVa',
        text: UI.i18n.field.title_va,
        flex: 5
      }, {
        dataIndex: 'imagenSrc',
        text: UI.i18n.field.imagen,
        flex: 4,
        renderer: function (val, record, p) {
          if (val != undefined) {
            return '<a href="' + urlPrefix + 'evento/' + p.data.id + '/imagen" target="blank">' + UI.i18n.field.imagenInsertada + '</a>'
          }
        }
      }];

      this.callParent(arguments);
   },


   showAddEventoWindow: function() {
      this.createModalWindow('formEventos', 600, 600).show();
   }

});