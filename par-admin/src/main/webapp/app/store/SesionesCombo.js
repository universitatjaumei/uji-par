Ext.define('Paranimf.store.SesionesCombo', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Sesion',

   sorters: [{
        property: 'fechaCelebracion',
        direction: 'ASC'
   }],
   autoLoad: false,
   autoSync: true,
   remoteSort: true,
   pageSize: 10000,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'sesiones',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});