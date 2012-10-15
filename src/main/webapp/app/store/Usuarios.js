Ext.define('Paranimf.store.Usuarios', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Usuario',

   sorters: ['nombre'],
   autoLoad: false,
   autoSync: true,
   pageSize: 10,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'usuarios',
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