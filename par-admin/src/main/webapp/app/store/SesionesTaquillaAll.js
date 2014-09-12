Ext.define('Paranimf.store.SesionesTaquillaAll', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Sesion',

   sorters: [{
        property: 'fechaCelebracion',
        direction: 'ASC'
   }],
   
   autoLoad: false,
   autoSync: false,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'evento/?/sesiones',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});