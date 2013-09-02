Ext.define('Paranimf.store.ButacasCompra', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Butaca',

   autoLoad: false,
   autoSync: false,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'compra/?/butacas',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});