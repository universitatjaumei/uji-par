Ext.define('Paranimf.model.Precio', {
   extend: 'Ext.data.Model',

   fields: [
    	'id', 
	  	'plantillaPrecios',
	  	{name: 'localizacion_nombre', mapping: 'localizacion.nombreVa'},
	  	{name: 'localizacion_id', mapping: 'localizacion.id'},
	  	'localizacion',
	  	'precio',
	  	'descuento',
	  	'invitacion'
	]
});