Ext.define('Paranimf.model.PlantillaPrecios', {
   extend: 'Ext.data.Model',

   fields: [
      'id', 
      'nombre',
      {name: 'nombreSala', mapping: 'sala.nombre'},
      {name: 'sala', mapping: 'sala.id'}
	]
});