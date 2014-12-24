Ext.define('Paranimf.store.Tpvs', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Tpv',

   sorters: ['nombre'],
   autoLoad: true,
   autoSync: false,
   pagesize: 1000,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'tpv',
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