Ext.define('Paranimf.store.EventosTaquilla', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Evento',

   sorters: ['tituloEs'],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'evento?activos=true',
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