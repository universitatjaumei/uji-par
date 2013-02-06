Ext.define('Paranimf.model.Sesion', {
   extend: 'Ext.data.Model',

   fields: [
      'id',
      {name: 'fechaCelebracion', type: 'date', dateFormat: 'U'},
      {name: 'fechaInicioVentaOnline', type: 'date', dateFormat: 'U'},
      {name: 'fechaFinVentaOnline', type: 'date', dateFormat: 'U'},
      'evento',
      'horaAperturaPuertas',
      'canalInternet',
      'canalTaquilla',
      'horaCelebracion'
   ]
});