Ext.define('Paranimf.store.SesionesAbono', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.SesionAbono',

   sorters: [{
        property: 'id',
        direction: 'ASC'
   }]
});