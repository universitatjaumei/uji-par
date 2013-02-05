Ext.define('Paranimf.store.Eventos', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Evento',

   sorters: ['tituloEs'],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'evento',
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