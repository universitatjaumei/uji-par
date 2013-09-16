Ext.define('Paranimf.view.compra.GridDetalleCompras', {
  extend: 'Paranimf.view.EditBaseGrid',

  alias: 'widget.gridDetalleCompras',
  title: UI.i18n.gridTitle.detalleCompras,
  store: 'ButacasCompra',

  tbar:{},
   
  dockedItems: [{
    xtype: 'pagingtoolbar',
    store: 'ButacasCompra',
    dock: 'bottom',
    displayInfo: true
  }],


  initComponent: function() {

    this.columns = [{
      dataIndex: 'id',
      text: UI.i18n.field.idIntern,
      hidden: true
    }, {
      dataIndex: 'localizacion',
      text: UI.i18n.field.localizacion,
      flex: 1,
      renderer: function(val) {
        return (!val)?'':eval("UI.i18n.tipos." + val);
      }
    }, {
      dataIndex: 'fila',
      text: UI.i18n.field.fila,
      flex: 1,
      renderer: function(val) {
        return (val)?val:UI.i18n.message.noNumerada
      }
    }, {
      dataIndex: 'numero',
      text: UI.i18n.field.numeroSeient,
      flex: 1,
      renderer: function(val) {
        return (val)?val:UI.i18n.message.noNumerada
      }
    }, {
      dataIndex: 'tipo',
      text: UI.i18n.field.tipusEntrada,
      flex: 1,
      renderer: function(val) {
        console.log(val);
        console.log(eval("UI.i18n.tipoEntrada." + val));
        return (val!=undefined&&val!='')?eval("UI.i18n.tipoEntrada." + val):'';
      }
    }, {
      dataIndex: 'precio',
      text: UI.i18n.field.precio,
      flex: 1,
      renderer: function(val) {
        return (val == 0)?'':val.toFixed(2) + '€';
      }
    }];
   
    this.callParent(arguments);
   }
});