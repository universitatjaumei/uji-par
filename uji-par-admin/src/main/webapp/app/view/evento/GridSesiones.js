Ext.define('Paranimf.view.evento.GridSesiones', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridSesiones',
   store: 'Sesiones',

   title: UI.i18n.gridTitle.sesiones,

   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'Sesiones',
     dock: 'bottom',
     displayInfo: true
   }],

   initComponent: function() {

    this.columns = [{
      dataIndex: 'id',
      hidden: true,
      text: UI.i18n.field.idIntern
    }, {
      hidden: true,
      dataIndex: 'horaCelebracion',
      text: UI.i18n.field.sessionTime
    }, {
      dataIndex: 'fechaCelebracion',
      text: UI.i18n.field.eventDate,
      format:'d/m/Y H:i',
      xtype: 'datecolumn',
      flex: 1
    }, {
      dataIndex: 'horaApertura',
      text: UI.i18n.field.opening,
      flex: 1
    }, {
      dataIndex: 'fechaInicioVentaOnline',
      text: UI.i18n.field.startOnlineSelling,
      format:'d/m/Y',
      xtype: 'datecolumn',
      flex: 1
    }, {
      dataIndex: 'fechaFinVentaOnline',
      text: UI.i18n.field.endOnlineSelling,
      format:'d/m/Y',
      xtype: 'datecolumn',
      flex: 1
    }, {
      dataIndex: 'plantillaPrecios_nombre',
      text: UI.i18n.field.plantillaprecios,
      sortable: false,
      flex: 1
    }];

    this.callParent(arguments);
  },

  showAddSesionWindow: function() {
    this.createModalWindow('formSesiones', 600, 600).show();
  }
});