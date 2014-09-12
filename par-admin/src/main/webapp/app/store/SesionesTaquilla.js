Ext.define('Paranimf.store.SesionesTaquilla', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Sesion',

   sorters: [{
        property: 'fechaCelebracion',
        direction: 'ASC'
   }],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'sesiones?activos=true',
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