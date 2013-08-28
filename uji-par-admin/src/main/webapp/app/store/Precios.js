Ext.define('Paranimf.store.Precios', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Precio',

   sorters: ['parLocalizacione'],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'precio',
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