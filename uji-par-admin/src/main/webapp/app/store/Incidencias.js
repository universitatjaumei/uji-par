Ext.define('Paranimf.store.Incidencias',
 {
   extend: 'Ext.data.Store',
   model: 'Paranimf.model.HoraMinuto',
   autoLoad: true,
   data : [
   	{'label': UI.i18n.incidencias.i0, 'id': 0},
   	{'label': UI.i18n.incidencias.i5, 'id': 5},
   	{'label': UI.i18n.incidencias.i6, 'id': 6},
   	{'label': UI.i18n.incidencias.i7, 'id': 7},
   	{'label': UI.i18n.incidencias.i8, 'id': 8},
   	{'label': UI.i18n.incidencias.i9, 'id': 9},
   	{'label': UI.i18n.incidencias.i10, 'id': 10},
   	{'label': UI.i18n.incidencias.i11, 'id': 11}
   ]
});