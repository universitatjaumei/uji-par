Ext.define('Paranimf.view.generarficheros.GridSesionesCompleto', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridSesionesCompleto',
   store: 'SesionesFicheros',

   title: UI.i18n.gridTitle.sesionesICAA,
   tbar: [{
      xtype: 'button',
      text: UI.i18n.button.saveFileICAA,
      action: 'saveFileICAA'
   }],

   dockedItems: [],

   selModel: {
    mode: 'MULTI'
   },
   selType: 'checkboxmodel',

   initComponent: function() {

    this.columns = [{
      dataIndex: 'id',
      hidden: true,
      text: UI.i18n.field.idIntern
    }, {
      dataIndex: 'tituloEs',
      flex: 3,
      text: UI.i18n.field.tituloMulti
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
      dataIndex: 'ficheroICAAgenerado',
      text: UI.i18n.field.ficheroICAAgenerado,
      flex: 1
    }, {
      dataIndex: 'ficheroICAAenviado',
      text: UI.i18n.field.ficheroICAAenviado,
      flex: 1
    }];

    this.callParent(arguments);
  },

  showDatosFicheroForm: function() {
      this.createPercentageModalWindow('formDatosFichero', undefined, undefined, UI.i18n.formTitle.datosFichero).show();
   }
});