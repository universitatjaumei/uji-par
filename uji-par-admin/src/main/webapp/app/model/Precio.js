Ext.define('Paranimf.model.Precio', {
   extend: 'Ext.data.Model',

   fields: [
    	'id', 
	  	'plantillaPrecios',
	  	{name: 'localizacion_nombre', mapping: 'localizacion.nombreVa'},
	  	{name: 'parLocalizacione', mapping: 'localizacion.id'},
	  	'localizacion',
	  	'precio',
	  	{name: 'tarifa_nombre', mapping: 'tarifa.nombre'},
	  	{name: 'tarifa', mapping: 'tarifa.id'}
	]
});