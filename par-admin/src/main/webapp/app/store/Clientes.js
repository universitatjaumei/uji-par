Ext.define('Paranimf.store.Clientes', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Cliente',

   sorters: ['nombre'],
   autoLoad: false,
   autoSync: true,
   pageSize: 10,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'clientes',
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