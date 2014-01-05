Ext.define('Paranimf.store.TipoEnvio',
 {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.HoraMinuto',
   autoLoad: true,
   data : [
   	{'label': UI.i18n.field.envioHabitual, 'id': 'FL'},
   	{'label': UI.i18n.field.envioRetrasado, 'id': 'AT'}
   ]
});