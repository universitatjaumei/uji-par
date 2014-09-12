Ext.define('Paranimf.store.TiposEventos', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.TipoEvento',

   sorters: ['nombreEs'],
   autoLoad: false,
   autoSync: true,
   pageSize: 10,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'tipoevento',
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