Ext.define('Paranimf.store.PlantillasPrecios', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.PlantillaPrecios',

   sorters: ['nombre'],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'plantillaprecios',
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