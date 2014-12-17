Ext.define('Paranimf.store.PlantillasPreciosCombo', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.PlantillaPrecios',

   sorters: ['nombre'],
   autoLoad: false,
   autoSync: true,
   pageSize: 1000,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'plantillaprecios/abonos',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});