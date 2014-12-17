Ext.define('Paranimf.store.EventosCombo', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Evento',

   sorters: [{
      property: 'fechaPrimeraSesion',
      direction: 'DESC'
   }],
   autoLoad: true,
   autoSync: true,
   pageSize: 10000,

   proxy: {
      type: 'rest',
      url: urlPrefix + 'evento?activos=true',
      reader: {
         type: 'json',
         root: 'data',
         totalProperty: 'total'
      }
   }
});