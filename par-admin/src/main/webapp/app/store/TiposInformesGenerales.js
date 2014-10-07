Ext.define('Paranimf.store.TiposInformesGenerales',
 {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Informe',

   sorters: ['nombre'],
   autoLoad: false,
   pagesize: 1000,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'informe/generales',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});