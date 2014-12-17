Ext.define('Paranimf.view.cliente.FormMails', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formMails',

   buttons: [{
      xtype: 'button',
      text: UI.i18n.button.close,
      action: 'cancel',
      handler: function() {
         this.up('window').close();
      }
   }],

   url : urlPrefix + 'mails',
   
   defaults: {
      msgTarget: 'side',
      labelWidth: 90,
      anchor: '100%',
      xtype: 'textarea'
   },

   items: [{
      fieldLabel: UI.i18n.field.email,
      name: 'mails'
   },]
});