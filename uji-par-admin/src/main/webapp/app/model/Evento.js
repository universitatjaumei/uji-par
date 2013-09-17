Ext.define('Paranimf.model.Evento', {
   extend: 'Ext.data.Model',

   fields: [
      'id', 
      'tituloEs',
      'tituloVa',
      'descripcionEs',
      'descripcionVa',
      'companyiaEs',
      'companyiaVa',
      'duracionEs',
      'duracionVa',
      'premiosEs',
      'premiosVa',
      'caracteristicasEs',
      'caracteristicasVa',
      'comentariosEs',
      'comentariosVa',
      'tipoEvento',
      
      'parTiposEvento',
      
      'dataBinary',
      'dataBinaryDetail',
      'imagenContentType',
      'imagenSrc',

      'asientosNumerados',
      'porcentajeIVA',
      'retencionSGAE',
      'ivaSGAE',
      'fechaPrimeraSesion',
      //{name: 'fechaPrimeraSesion', type: 'date', dateFormat: 'U'},

      {name: 'fechaInicio', type: 'date'},
      {name: 'fechaFin', type: 'date'}
   ]
});