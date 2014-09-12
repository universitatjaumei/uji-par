Ext.define('Paranimf.store.SiNo',
 {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.HoraMinuto',
   autoLoad: true,
   data : [
   	{'label': 'No', 'id': false},
   	{'label': 'Sí', 'id': true}
   ]
});