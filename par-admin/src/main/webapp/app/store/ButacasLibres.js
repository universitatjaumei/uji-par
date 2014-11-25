Ext.define('Paranimf.store.ButacasLibres', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Butaca',

   autoLoad: false,
   autoSync: false,
   pageSize: 20,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'butacas/?',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});