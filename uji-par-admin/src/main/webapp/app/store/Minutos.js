Ext.define('Paranimf.store.Minutos', {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.HoraMinuto',
   autoLoad: true,
   data : [
   	{'id': 0, 'label': '00'},{'id': 15, 'label': '15'},{'id': 30, 'label': '30'},{'id': 45, 'label': '45'}
   ]
});