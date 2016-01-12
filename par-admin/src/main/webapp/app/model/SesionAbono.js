Ext.define('Paranimf.model.SesionAbono', {
   extend: 'Ext.data.Model',

   fields: [
      'id',
      'sesion',
      {name: 'fechaCelebracion', mapping: 'sesion.fechaCelebracion'},
      {name: 'horaApertura', mapping: 'sesion.horaApertura'},
      {name: 'tituloEs', mapping: 'sesion.evento.tituloEs'},
      {name: 'tituloVa', mapping: 'sesion.evento.tituloVa'}
   ]
});