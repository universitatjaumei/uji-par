Ext.define('Paranimf.model.Abono', {
   extend: 'Ext.data.Model',

   fields: [
      'id', 
      'nombre',
      {name: 'plantillaPrecios', mapping: 'plantillaPrecios.id'},
      {name: 'plantillaPrecios_nombre', mapping: 'plantillaPrecios.nombre'}
   ]
});