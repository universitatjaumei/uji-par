Ext.define('Paranimf.model.Precio', {
   extend: 'Ext.data.Model',

   fields: [
    	'id', 
	  	'plantillaPrecios',
	  	{name: 'parLocalizacione.nombreVa', mapping: 'localizacion.nombreVa'},
	  	{name: 'localizacion_id', mapping: 'localizacion.id'},
	  	'localizacion',
	  	'precio',
	  	{name: 'parTarifa.nombre', mapping: 'tarifa.nombre'},
	  	{name: 'tarifa_id', mapping: 'tarifa.id'}
	]
});