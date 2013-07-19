Ext.define('Paranimf.view.taquilla.FormComprar', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formComprar',

   url : urlPrefix + 'sesiones',
   
   defaults: {
      anchor: '100%',
      xtype: 'textfield',
      labelWidth: 190,
   },

   items: [{
	   xtype: 'panel',
	   id: 'formComprar',
	   frame: false,
	   border: 0
   }]
   
});