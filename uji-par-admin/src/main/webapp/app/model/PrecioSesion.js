Ext.define('Paranimf.model.PrecioSesion', {
   extend: 'Ext.data.Model',

   fields: [
    	'id', 
	  	'plantillaPrecios',
	  	{name: 'localizacion_nombre', mapping: 'localizacion.nombreVa'},
	  	{name: 'localizacion', mapping: 'localizacion.id'},
	  	'precio',
	  	'descuento',
	  	'invitacion',
	  	'aulaTeatro',
	  	'sesion_id'
	]
});