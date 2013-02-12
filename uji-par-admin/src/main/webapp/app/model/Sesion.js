Ext.define('Paranimf.model.Sesion', {
   extend: 'Ext.data.Model',

   fields: [
      'id',
      {name: 'fechaCelebracion', type: 'date', dateFormat: 'U'},
      {name: 'fechaInicioVentaOnline', type: 'date', dateFormat: 'U'},
      {name: 'fechaFinVentaOnline', type: 'date', dateFormat: 'U'},
      'evento',
      'horaAperturaPuertas',
      'horaCelebracion',
      'horaInicioVentaOnline',
      'horaFinVentaOnline',
      {name: 'plantillaPrecios', mapping: 'plantillaPrecios.id'},
      {name: 'plantillaPrecios_nombre', mapping: 'plantillaPrecios.nombre'},
      'preciosSesion'
   ]
});