Ext.define('Paranimf.store.Abonados', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Abonado',

   sorters: [{
        property: 'nombre',
        direction: 'ASC'
   }],
   autoLoad: false,
   autoSync: true,
   pageSize: 20,
   remoteSort: true,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'abono/abonados',
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