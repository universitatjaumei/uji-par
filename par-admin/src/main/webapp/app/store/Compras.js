Ext.define('Paranimf.store.Compras', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Compra',

   sorters: ['fecha'],
   autoLoad: false,
   autoSync: true,
   pageSize: 10,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'compra',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      },
      writer: {
         type: 'json'
      }
   }
});