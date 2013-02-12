Ext.define('Paranimf.store.PlantillasPreciosEditables', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.PlantillaPrecios',

   sorters: ['nombre'],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'plantillaprecios/editables',
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