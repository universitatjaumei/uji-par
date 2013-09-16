Ext.define('Paranimf.view.compra.GridCompras', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridCompras',
   store: 'Compras',
   title: UI.i18n.gridTitle.comprasReservasHechas,

  tbar:[{
    action: 'anular',
    text: UI.i18n.button.anular
  }, {
    xtype: 'checkbox',
    fieldLabel: UI.i18n.field.mostrarAnuladas,
    labelWidth: 120
  }],
   
   dockedItems: [{
     xtype: 'pagingtoolbar',
     store: 'Compras',
     dock: 'bottom',
     displayInfo: true
   }],

  viewConfig: {
    getRowClass: function(record) {
      if (record && record.data.anulada)
        return 'gridAnulada'
      else if(record && record.data.reserva)
        return 'gridReserva';
      else if (record && record.data.taquilla)
        return 'gridTaquilla';
      else
        return 'gridOnline';
    }
  },

  initComponent: function() {

    this.columns = [{
      dataIndex: 'id',
      text: UI.i18n.field.idIntern,
      hidden: true
    }, {
      dataIndex: 'uuid',
      text: UI.i18n.field.uuid,
      hidden: true
    }, {
      dataIndex: 'fecha',
      text: UI.i18n.field.date,
      format:'d/m/Y H:i',
      xtype: 'datecolumn',          
      flex: 5
    }, {
      dataIndex: 'nombre',
      text: UI.i18n.field.nameMulti,
      flex: 5
    }, {
      dataIndex: 'apellidos',
      text: UI.i18n.field.surnameMulti,
      flex: 5
    }, {
      dataIndex: 'email',
      flex: 5,
      text: UI.i18n.field.email,
    }, {
      dataIndex: 'telefono',
      flex: 2,
      text: UI.i18n.field.phone,
    }, {
      align: 'center',
      dataIndex: 'importe',
      flex: 2,
      text: UI.i18n.field.importe,
      renderer: function(val) {
        return (val == 0)?'':val.toFixed(2) + '€';
      }
    },{
      align: 'center',
      dataIndex: 'taquilla',
      flex: 2,
      text: UI.i18n.field.taquilla,
      renderer: function (val, p) {
          return (val)?'<img src="../resources/images/tick.png" alt="Sí" title="Sí" />':'';
      }
    }, {
      align: 'center',
      flex: 2,
      text: UI.i18n.field.online,
      renderer: function (val, p, rec) {
        return (!rec.data.taquilla)?'<img src="../resources/images/tick.png" alt="Sí" title="Sí" />':'';
      }
    }, {
      align: 'center',
      dataIndex: 'reserva',
      flex: 2,
      text: UI.i18n.field.reserva,
      renderer: function (val, p) {
        return (val)?'<img src="../resources/images/tick.png" alt="Sí" title="Sí" />':'';
      }
    }, {
      align: 'center',
      dataIndex: 'pagada',
      flex: 2,
      text: UI.i18n.field.pagada,
      renderer: function (val, p) {
          return (val)?'<img src="../resources/images/tick.png" alt="Sí" title="Sí" />':'<img src="../resources/images/cross.png" alt="Sí" title="Sí" />';
      }
    }, {
      flex: 2,
      text: UI.i18n.message.printTaquilla,
      renderer: function(val, p, rec) {
        return '<a target="blank" href="' + urlPrefix + 'compra/' + rec.data.uuid + '/pdftaquilla">' + UI.i18n.message.print + '</a>';
      }
    }, {
      flex: 2,
      text: UI.i18n.message.printAtHome,
      renderer: function(val, p, rec) {
        return '<a target="blank" href="' + urlPrefix + 'compra/' + rec.data.uuid + '/pdf">' + UI.i18n.message.print + '</a>';
      }
    }];

    this.callParent(arguments);
  },

  showAddCompraWindow: function() {
    this.createPercentageModalWindow('formCompras').show();
  }
});