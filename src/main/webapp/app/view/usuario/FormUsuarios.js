Ext.define('Paranimf.view.usuario.FormUsuarios', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formUsuarios',

   url : urlPrefix + 'usuarios',
   
   defaults: {
      allowBlank: false,
      msgTarget: 'side',
      labelWidth: 90,
      anchor: '100%',
      xtype: 'textfield'
   },

   items: [{
      name: 'id',
      hidden: true,
      allowBlank: true
   }, {
      fieldLabel: UI.i18n.field.user,
      name: 'usuario'
   }, {
      fieldLabel: UI.i18n.field.name,
      name: 'nombre'
   }, {
      fieldLabel: UI.i18n.field.email,
      name: 'mail',
      vtype: 'email'
   }]
});