Ext.define('Paranimf.store.Peliculas', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Evento',

   sorters: [{
      property: 'titulo_es',
      direction: 'DESC'
   }],
   autoLoad: false,
   pageSize: 10000,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'evento/peliculas',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});