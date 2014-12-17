Ext.define('Paranimf.model.Cliente', {
   extend: 'Ext.data.Model',

   fields: [
      'id', 
      'nombre', 
      'apellidos', 
      'direccion',
      'poblacion',
      'cp',
      'provincia',
      'telefono',
      'email'
   ]
});