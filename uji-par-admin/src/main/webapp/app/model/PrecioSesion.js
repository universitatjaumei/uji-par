Ext.define('Paranimf.model.PrecioSesion', {
   extend: 'Ext.data.Model',

   fields: [
    	'id', 
	  	'plantillaPrecios',
	  	{name: 'localizacion_nombre', mapping: 'localizacion.nombreVa'},
	  	{name: 'localizacion', mapping: 'localizacion.id'},
	  	'precio',
	  	'sesion_id',
	  	{name: 'tarifa_nombre', mapping: 'tarifa.nombre'},
	  	{name: 'tarifa', mapping: 'tarifa.id'}
	]
});