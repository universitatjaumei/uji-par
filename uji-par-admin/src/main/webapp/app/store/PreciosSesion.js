Ext.define('Paranimf.store.PreciosSesion', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.PrecioSesion',

   sorters: ['localizacion_id'],
   autoLoad: false,
   autoSync: false,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
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