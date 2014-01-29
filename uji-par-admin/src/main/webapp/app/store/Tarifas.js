Ext.define('Paranimf.store.Tarifas', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Tarifa',

   sorters: ['nombre'],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'tarifa',
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