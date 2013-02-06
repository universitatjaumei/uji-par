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
      'interpretesEs',
      'interpretesVa',
      'duracionEs',
      'duracionVa',
      'premiosEs',
      'premiosVa',
      'caracteristicasEs',
      'caracteristicasVa',
      'comentariosEs',
      'comentariosVa',
      'tipoEvento',
      
      'parTipoEvento',
      
      'dataBinary',
      'dataBinaryDetail',
      'imagenContentType',
      'imagenSrc',

      'asientosNumerados',
      'porcentajeIVA',
      'retencionSGAE',
      'ivaSGAE',

      {name: 'fechaInicio', type: 'date'},
      {name: 'fechaFin', type: 'date'}
   ]
});