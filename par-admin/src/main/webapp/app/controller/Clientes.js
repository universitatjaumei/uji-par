Ext.define('Paranimf.controller.Clientes', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseGrid', 'EditBaseForm', 'cliente.GridClientes', 'cliente.FormMails'],
   stores: ['Clientes'],
   models: ['Cliente'],

   refs: [{
      ref: 'gridClientes',
      selector: 'gridClientes'
   }, {
      ref: 'formMails',
      selector: 'formMails'
   }, {
      ref: 'mailsText',
      selector: 'formMails textarea[name=mails]'
   }],

   init: function() {
      this.control({

         'gridClientes button[action=copyEmails]': {
            click: this.copyEmails
         },

         'gridClientes': {
            beforeactivate: this.recargaStore
         },

         'formMails': {
            afterrender: this.getMails
         }
      });
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE CLIENTES");
      this.getGridClientes().recargaStore();
   },

   copyEmails: function(button, event, opts) {
      this.getGridClientes().createPercentageModalWindow('formMails').show();
   },

   getMails: function() {
      var me = this;
      Ext.Ajax.request({
        url : urlPrefix + 'clientes/mails',
        method: 'GET',
        success: function (response) {
            var respuesta = Ext.JSON.decode(response.responseText, true);
            var emails = "";
            for (var i = 0; i < respuesta.data.length; i++)
            {
               if (i < respuesta.data.length - 1)
               {
                  emails += respuesta.data[i] + ",";
               }
               else
               {
                  emails += respuesta.data[i];
               }
            }
            me.getMailsText().setValue(emails);
            var input = me.getMailsText().inputEl.dom;
            input.selectionStart = 0;
        }, failure: function (response) {
           me.getFormMails().up("window").close();
           alert(UI.i18n.error.loadingPrecios);
        }
      });
   }
});