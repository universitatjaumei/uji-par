Ext.define('Paranimf.store.TiposEventosSinPaginar', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.TipoEvento',

   sorters: ['nombre'],
   autoLoad: true,
   autoSync: false,
   pageSize: 1000,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'tipoevento',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});