Ext.define('Paranimf.controller.Usuarios', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'usuario.GridUsuarios', 'usuario.FormUsuarios'],
   stores: ['Usuarios'],
   models: ['Usuario'],

   refs: [{
      ref: 'gridUsuarios',
      selector: 'gridUsuarios'
   }, {
      ref: 'formUsuarios',
      selector: 'formUsuarios'
   }],

   init: function() {
      this.control({

         'gridUsuarios button[action=add]': {
            click: this.addUsuario
         },

         'gridUsuarios button[action=edit]': {
            click: this.editUsuario
         },

         'gridUsuarios button[action=del]': {
            click: this.removeUsuario
         },

         'gridUsuarios': {
			beforeactivate: this.recargaStore
         },

         'formUsuarios button[action=save]': {
            click: this.saveUsuariosFormData
         }
      });
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE USUARIOS");
      this.getGridUsuarios().recargaStore();
   },

   addUsuario: function(button, event, opts) {
      this.getGridUsuarios().showAddUsuarioWindow();
   },

   editUsuario: function(button, event, opts) {
      this.getGridUsuarios().edit('formUsuarios');
   },

   removeUsuario: function(button, event, opts) {
      this.getGridUsuarios().remove();
   },

   saveUsuariosFormData: function(button, event, opts) {
      var grid = this.getGridUsuarios();
      var form = this.getFormUsuarios();
      form.saveFormData(grid, urlPrefix + 'usuarios');
   }
});