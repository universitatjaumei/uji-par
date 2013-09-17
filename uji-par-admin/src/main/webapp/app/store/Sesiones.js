Ext.define('Paranimf.store.Sesiones', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Sesion',

   sorters: [{
        property: 'fechaCelebracion',
        direction: 'DESC'
   }],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'sesiones',
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