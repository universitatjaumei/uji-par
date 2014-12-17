Ext.define('Paranimf.model.Abonado', {
   extend: 'Ext.data.Model',

   fields: [
        {name:"id", type:"int"},
        'nombre',
        'apellidos',
        'direccion',
        'poblacion',
        'cp',
        'provincia',
        'telefono',
        'email',
        {name:"infoPeriodica", type:"boolean"}
   ]
});