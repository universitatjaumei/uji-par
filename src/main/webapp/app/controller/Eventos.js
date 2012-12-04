Ext.define('Paranimf.controller.Eventos', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'evento.GridEventos', 'evento.FormEventos', 'evento.PanelEventos', 'evento.GridSesiones', 'evento.FormSesiones'],
   stores: ['Eventos', 'TiposEventosSinPaginar', 'Sesiones'],
   models: ['Evento', 'Sesion'],

   refs: [{
      ref: 'gridEventos',
      selector: 'gridEventos'
   }, {
      ref: 'formEventos',
      selector: 'formEventos'
   }, {
	   ref: 'panelEventos',
	   selector: 'panelEventos'
   }, {
	   ref: 'gridSesiones',
	   selector: 'gridSesiones'
   }, {
	   ref: 'formSesiones',
	   selector: 'formSesiones'
   }],

   init: function() {
      this.control({

         'gridEventos button[action=add]': {
            click: this.addEvento
         },

         'gridEventos button[action=edit]': {
            click: this.editEvento
         },

         'gridEventos button[action=del]': {
            click: this.removeEvento
         },

         'panelEventos': {
			beforeactivate: this.recargaStore
         },

         'formEventos button[action=save]': {
            click: this.saveEventoFormData
         },
         
         'formEventos': {
        	 beforerender: this.setImagen
         },
         
         'gridEventos': {
             selectionchange: this.loadSesiones
         },
         
         'gridSesiones button[action=add]': {
             click: this.addSesion
         },

         'gridSesiones button[action=edit]': {
             click: this.editSesion
         },

         'gridSesiones button[action=del]': {
            click: this.removeSesion
         },
         
         'formSesiones button[action=save]': {
            click: this.saveSesionFormData
         }
      });
   },
   
   
   setImagen: function(comp, opts) {
	   var imagen = comp.down("#imagenInsertada");
	   var idEvento = comp.down("textfield[name='id']").getValue(); 
	   if (idEvento != "")
		   imagen.setSrc(urlPrefix + 'evento/' + idEvento + '/imagen'); 
	   else
		   imagen.setSrc('');  
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE EVENTOS");
      this.getGridEventos().recargaStore();
   },

   addEvento: function(button, event, opts) {
      this.getGridEventos().showAddEventoWindow();
   },

   editEvento: function(button, event, opts) {
      this.getGridEventos().edit('formEventos', undefined, 600, 600);
   },

   removeEvento: function(button, event, opts) {
      var gridSesiones = this.getGridSesiones();
      this.getGridEventos().remove(function (borradoCorrectamente) {
         if (borradoCorrectamente)
            gridSesiones.getStore().removeAll();
      });
   },

   saveEventoFormData: function(button, event, opts) {
      var grid = this.getGridEventos();
      var form = this.getFormEventos();
      form.saveFormData(grid, urlPrefix + 'evento', undefined, 'multipart/form-data');
   },
   
   loadSesiones: function(selectionModel, record) {
      if (record[0]) {
         var storeSesiones = this.getGridSesiones().getStore();
         var eventoId = record[0].get("id");

         storeSesiones.getProxy().url = urlPrefix + 'evento/' + eventoId + '/sesiones';
         storeSesiones.load();
      }
   },
   
   addSesion: function(button, event, opts) {
	   if (this.getGridEventos().getSelectedColumnId())
		   this.getGridSesiones().showAddSesionWindow();
	   else
		   alert(UI.i18n.message.event);
   },
   
   saveSesionFormData: function(button, event, opts) {
	   var eventoId = this.getGridEventos().getSelectedColumnId();

	   var grid = this.getGridSesiones();
	   var form = this.getFormSesiones();
	   form.saveFormData(grid, urlPrefix + 'evento/' + eventoId + '/sesiones');
   },
   
   removeSesion: function(button, event, opts) {
      this.getGridSesiones().remove();
   },
   
   editSesion: function(button, event, opts) {
      this.getGridSesiones().edit('formSesiones');
   }
});