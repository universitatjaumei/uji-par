Ext.define('Paranimf.view.compra.GridDetalleCompras', {
  extend: 'Paranimf.view.EditBaseGrid',

  alias: 'widget.gridDetalleCompras',
  title: UI.i18n.gridTitle.detalleCompras,
  //store: 'Compras',

  tbar:{},
   
  dockedItems: [{
    xtype: 'pagingtoolbar',
    //store: 'Compras',
    dock: 'bottom',
    displayInfo: true
  }],


  initComponent: function() {

    this.columns = [{
      dataIndex: 'id',
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
    }/*, {
         dataIndex: 'apellidos',
         text: UI.i18n.field.surnameMulti,
         flex: 5
      }, {
          dataIndex: 'email',
          flex: 5,
          text: UI.i18n.field.email,
      }, {
          dataIndex: 'taquilla',
          flex: 2,
          text: UI.i18n.field.taquilla,
          renderer: function (val, p) {
              return (val)?'Sí':'No';
          }
      }, {
          dataIndex: 'pagada',
          flex: 2,
          text: UI.i18n.field.pagada,
          renderer: function (val, p) {
              return (val)?'Sí':'No';
          }
      }, {
          dataIndex: 'reserva',
          flex: 2,
          text: UI.i18n.field.reserva,
          renderer: function (val, p) {
        	  return (val)?'Sí':'No';
          }
      }*/
    ];
   
    this.callParent(arguments);
   }
});