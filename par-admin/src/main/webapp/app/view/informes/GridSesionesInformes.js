Ext.define('Paranimf.view.informes.GridSesionesInformes', {
  extend: 'Paranimf.view.EditBaseGrid',

  alias: 'widget.gridSesionesInformes',
  store: 'SesionesInformes',

  title: UI.i18n.gridTitle.informeSessions,
  tbar: [{
    action: 'generarInformeSesion',
    xtype: 'button',
    text: UI.i18n.button.generarInformeSesion
  },{ 
    action: 'generarInformeEvento',
    xtype: 'button',
    text: UI.i18n.button.generarInformeEvento
  }],

  dockedItems: [{
    xtype: 'pagingtoolbar',
    store: 'SesionesInformes',
    dock: 'bottom',
    displayInfo: true
  }],

  initComponent: function() {
    this.columns = [{
      dataIndex: 'id',
      hidden: true,
      text: UI.i18n.field.idIntern,
      sortable: false
    }, {
      dataIndex: 'tituloEs',
      flex: 3,
      text: UI.i18n.field.tituloMulti,
      sortable: false
    }, {
      dataIndex: 'fechaCelebracion',
      text: UI.i18n.field.eventDate,
      format:'d/m/Y H:i',
      xtype: 'datecolumn',
      flex: 1,
      sortable: false
    }, {
      dataIndex: 'salaNombre',
      text: UI.i18n.field.sala,
      flex: 2,
      sortable: false
    }];

    this.callParent(arguments);
  },

  showAddSesionWindow: function() {
    this.createPercentageModalWindow('formSesiones', undefined, 0.8).show();
  }
});