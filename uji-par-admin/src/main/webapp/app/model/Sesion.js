Ext.define('Paranimf.model.Sesion', {
   extend: 'Ext.data.Model',

   fields: [
      'id',
      {name: 'fechaCelebracion', type: 'date', dateFormat: 'U'},
      {name: 'fechaInicioVentaOnline', type: 'date', dateFormat: 'U'},
      {name: 'fechaFinVentaOnline', type: 'date', dateFormat: 'U'},
      'evento',
      {name: 'tituloEs', mapping: 'evento.tituloEs'},
      {name: 'tituloCa', mapping: 'evento.tituloCa'},
      'canalInternet',
      'horaApertura',
      'horaCelebracion',
      'horaInicioVentaOnline',
      'horaFinVentaOnline',
      {name: 'plantillaPrecios', mapping: 'plantillaPrecios.id'},
      {name: 'plantillaPrecios_nombre', mapping: 'plantillaPrecios.nombre'},
      'preciosSesion',
      'butacasVendidas',
      'nombre',
      'formato',
      'versionLinguistica',
      'rssId',
      {name: 'sala', mapping: 'sala.id'},
   ]
});