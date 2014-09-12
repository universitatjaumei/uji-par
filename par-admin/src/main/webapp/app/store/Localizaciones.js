Ext.define('Paranimf.store.Localizaciones', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Localizacion',

   sorters: ['nombreEs'],
   autoLoad: false,
   autoSync: true,
   pageSize: 10,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'localizacion',
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