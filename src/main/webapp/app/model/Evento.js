Ext.define('Paranimf.model.Evento', {
   extend: 'Ext.data.Model',

   fields: [
      'id', 
      'tituloEs',
      'tituloVa',
      'tituloEn',
      'descripcionEs',
      'descripcionVa',
      'descripcionEn',
      'companyiaEs',
      'companyiaVa',
      'companyiaEn',
      'interpretesEs',
      'interpretesVa',
      'interpretesEn',
      'duracionEs',
      'duracionVa',
      'duracionEn',
      'premiosEs',
      'premiosVa',
      'premiosEn',
      'caracteristicasEs',
      'caracteristicasVa',
      'caracteristicasEn',
      'comentariosEs',
      'comentariosVa',
      'comentariosEn',
      'tipoEvento',
      
      'parTipoEvento',
      'dataBinary',
      'dataBinaryDetail',
      {name: 'fechaInicio', type: 'date'},
      {name: 'fechaFin', type: 'date'}
   ]
});