Ext.define('Paranimf.view.compra.GridEventosComprasReservas', {
  extend: 'Paranimf.view.EditBaseGrid',

  alias: 'widget.gridEventosComprasReservas',
  store: 'EventosTaquillaAll',
  autoScroll: true,

  title: UI.i18n.gridTitle.eventos,

  dockedItems: [{
    xtype: 'pagingtoolbar',
    store: 'EventosTaquillaAll',
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
        flex: 2,
        dataIndex: 'fechaPrimeraSesion',
        text: UI.i18n.field.fechaPrimeraSesion,
        renderer: function(val) {
          if (val != '' && val != undefined) {
            var dt = new Date(val);
            return Ext.Date.format(dt, 'd/m/Y H:i');
          }
          return '';
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
    }];

    this.callParent(arguments);
    this.getDockedItems('toolbar[dock=top]')[0].hide();
   }
});