Ext.define('Paranimf.model.Sesion', {
   extend: 'Ext.data.Model',

   fields: [
      'id',
      {name: 'fechaCelebracion', type: 'date'/*, dateFormat: 'Y-m-d H:i'*/},
      {name: 'fechaInicioVentaOnline', type: 'date'/*, dateFormat: 'Y-m-d H:i'*/},
      {name: 'fechaFinVentaOnline', type: 'date'/*, dateFormat: 'Y-m-d H:i'*/},
      'evento',
      'horaAperturaPuertas',
      'canalInternet',
      'canalTaquilla'
   ]
});