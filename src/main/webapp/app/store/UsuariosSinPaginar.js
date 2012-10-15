Ext.define('Paranimf.store.UsuariosSinPaginar', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Usuario',

   sorters: ['nombre'],
   autoLoad: false,
   autoSync: false,
   pageSize: 1000,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'usuarios',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});