Ext.define('Paranimf.store.Eventos', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Evento',

   sorters: ['titulo'],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'eventos',
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