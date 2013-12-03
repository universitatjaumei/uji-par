Ext.define('Paranimf.store.Salas', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Sala',

   sorters: ['nombre'],
   autoLoad: true,
   autoSync: false,
   pagesize: 1000,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'sala',
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