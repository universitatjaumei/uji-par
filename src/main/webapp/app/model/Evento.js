Ext.define('Paranimf.model.Evento', {
   extend: 'Ext.data.Model',

   fields: [
      'id', 
      'titulo',
      'descripcion',
      'companyia',
      'interpretes',
      'duracion',
      'premios',
      'caracteristicas',
      'comentarios',
      'tipoEventoId',
      
      'tipoEvento',
      {name: 'fechaInicio', type: 'date'},
      {name: 'fechaFin', type: 'date'}
   ]
});