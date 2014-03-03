Ext.define('Paranimf.store.SesionesInformes', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Sesion',

   autoLoad: false,
   autoSync: false,
   pageSize: 1000,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'report/sesiones',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});