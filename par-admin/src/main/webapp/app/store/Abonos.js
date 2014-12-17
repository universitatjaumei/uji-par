Ext.define('Paranimf.store.Abonos', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Abono',

   sorters: [{
      property: 'nombre',
      direction: 'DESC'
   }],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'abono',
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