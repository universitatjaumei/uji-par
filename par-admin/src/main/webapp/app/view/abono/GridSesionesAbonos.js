Ext.define('Paranimf.view.abono.GridSesionesAbonos', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridSesionesAbonos',
   store: 'SesionesAbono',

   title: UI.i18n.gridTitle.sesiones,

   tbar: [{
      xtype: 'button',
      text: UI.i18n.button.add,
      action: 'add',
      hidden: (readOnlyUser == undefined)?false:readOnlyUser
   }, {
      xtype: 'button',
      text: UI.i18n.button.del,
      action: 'del',
      hidden: (readOnlyUser == undefined)?false:readOnlyUser
   }],

   initComponent: function() {

    this.columns = [{
      dataIndex: 'id',
      hidden: true,
      text: UI.i18n.field.idIntern
    }, {
      dataIndex: 'tituloVa',
      text: UI.i18n.field.nameMulti,
      flex: 1
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
    }];

    this.callParent(arguments);
  },

  showAddSesionWindow: function() {
    this.createPercentageModalWindow('formSesionesAbono', undefined, 0.15).show();
  }
});