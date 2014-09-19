Ext.define('Paranimf.store.EventosMultisesion', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.Evento',

   sorters: [{
      property: 'fechaPrimeraSesion',
      direction: 'DESC'
   }],
   autoLoad: false
});