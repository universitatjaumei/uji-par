Ext.define('Paranimf.store.SesionesFicheros', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Sesion',

   sorters: [{
      property: 'fechaCelebracion',
      direction: 'ASC'
   }],
   autoLoad: false,
   autoSync: false,
   pageSize: 100,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'evento/sesionesficheros',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});