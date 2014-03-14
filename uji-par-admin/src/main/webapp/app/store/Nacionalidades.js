Ext.define('Paranimf.store.Nacionalidades',
 {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.HoraMinuto',
   autoLoad: true,
   data : [
   		{'label': UI.i18n.nacionalidades.ES, 'id': 'ES'},
        {'label': UI.i18n.nacionalidades.US, 'id': 'US'},
        {'label': UI.i18n.nacionalidades.EN, 'id': 'EN'},
        {'label': UI.i18n.nacionalidades.FR, 'id': 'FR'},
        {'label': UI.i18n.nacionalidades.DE, 'id': 'DE'},
        {'label': UI.i18n.nacionalidades.PO, 'id': 'PO'},
        {'label': UI.i18n.nacionalidades.IT, 'id': 'IT'},
        {'label': UI.i18n.nacionalidades.altra, 'id': 'altra'}
   ]
});