Ext.define('Paranimf.controller.Eventos', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'evento.GridEventos', 'evento.FormEventos', 'evento.PanelEventos', 'evento.GridSesiones', 'evento.FormSesiones', 'evento.GridPreciosSesion', 'evento.FormPreciosSesion'],
   stores: ['Eventos', 'TiposEventosSinPaginar', 'Sesiones', 'PlantillasPrecios', 'PreciosSesion', 'Salas'],
   models: ['Evento', 'Sesion', 'PrecioSesion', 'Sala'],

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
   }, {
      ref: 'botonDeleteImagen',
      selector: 'formEventos button[action=deleteImage]'
   }, {
      ref: 'gridPreciosSesion',
      selector: 'gridPreciosSesion'
   }, {
      ref: 'formPreciosSesion',
      selector: 'formPreciosSesion'
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

         'formEventos button[action=deleteImage]': {
            click: this.deleteImage
         },
         
         'formEventos': {
        	   beforerender: this.showImagenIfExists
         },
         
         'gridEventos': {
             selectionchange: this.loadSesiones
         },
         
         'gridSesiones': {
             itemdblclick: this.editSesion
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

         'formSesiones': {
            afterrender: this.preparaStoresSesiones
         },
         
         'formSesiones button[action=save]': {
            click: this.saveSesionFormData
         },

         'formSesiones combobox[name=plantillaPrecios]': {
            change: this.cambiaPlantilla
         },

         'gridPreciosSesion button[action=add]': {
            click: this.addPrecioSesion
         },

         'gridPreciosSesion button[action=del]': {
            click: this.deletePrecioSesion
         },

         'gridPreciosSesion button[action=edit]': {
            click: this.editPrecioSesion
         },

         'formPreciosSesion': {
            afterrender: this.cargaLocalizaciones
         }, 

         'formPreciosSesion button[action=save]': {
            click: this.savePrecioSesion
         }
      });
   },

   deletePrecioSesion: function(button, event, opts) {
      if (button.up('form').getForm().findField('plantillaPrecios').value == -1) {
         this.getGridPreciosSesion().remove();
      }
   },

   editPrecioSesion: function(button, event, opts) {
      this.getGridPreciosSesion().edit('formPreciosSesion');
   },

   cambiaPlantilla: function(combo, newValue, oldValue, opts) {
      if (newValue == -1) {
         this.getGridPreciosSesion().mostrarToolbar();
         this.getGridPreciosSesion().vaciar();
         this.cargarPreciosSesion();
      } else {
         this.getGridPreciosSesion().ocultarToolbar();
         this.cargarPreciosPlantilla(newValue);
      }
   },

   cargarPreciosPlantilla: function(plantillaId) {
      var gridPreciosSesion = this.getGridPreciosSesion();
      Ext.Ajax.request({
        url : urlPrefix + 'plantillaprecios/' + plantillaId + '/precios',
        method: 'GET',
        success: function (response) {
            var respuesta = Ext.JSON.decode(response.responseText, true);
            gridPreciosSesion.store.loadRawData(respuesta.data);
        }, failure: function (response) {
           alert(UI.i18n.error.loadingPrecios);
        }
      });
   },
   
   cargarPreciosSesion: function(plantillaId) {
      var gridPreciosSesion = this.getGridPreciosSesion();
      
      if (this.getGridEventos().hasRowSelected() && this.getGridSesiones().hasRowSelected()) {
         var idEvento = this.getGridEventos().getSelectedRecord().data["id"];
         var idSesion = this.getGridSesiones().getSelectedRecord().data["id"];
         
         if (idEvento != undefined && idSesion != undefined) {
            console.log('idEvento:', idEvento);
            console.log('idSesion:', idSesion);
            
            Ext.Ajax.request({
              url : urlPrefix + 'evento/' + idEvento + '/sesiones/' + idSesion + '/precios',
              method: 'GET',
              success: function (response) {
                  var respuesta = Ext.JSON.decode(response.responseText, true);
                  gridPreciosSesion.store.loadRawData(respuesta.data);
              }, failure: function (response) {
                 alert(UI.i18n.error.loadingPrecios);
              }
            });
         }
      }
   },   

   cargaLocalizaciones: function(comp, opts) {
      this.getFormPreciosSesion().cargaComboStore('localizacion');
   },

   addPrecioSesion: function(button, event, opts) {
      this.getGridPreciosSesion().deseleccionar();
      this.getGridPreciosSesion().showAddPrecioSesionWindow();
   }, 

   existeRecordConLocalizacionElegida: function(indexFilaSeleccionada, localizacionSeleccionada) {
      var indexConMismaLocalizacion = this.getGridPreciosSesion().store.findExact('localizacion', localizacionSeleccionada);
      if (indexConMismaLocalizacion == -1)
         return false;
      else if (indexConMismaLocalizacion == indexFilaSeleccionada)
         return false;
      else
         return true;
   },

   savePrecioSesion: function(button, event, opts) {
      var indiceFilaSeleccionada = this.getGridPreciosSesion().getIndiceFilaSeleccionada();
      var localizacionSeleccionada = button.up('form').getForm().findField('localizacion').value

      if (!this.existeRecordConLocalizacionElegida(indiceFilaSeleccionada, localizacionSeleccionada)) {
         var precioSesion = new Ext.create('Paranimf.model.PrecioSesion', {
            plantillaPrecios :-1,
            precio : button.up('form').getForm().findField('precio').value,
            descuento : button.up('form').getForm().findField('descuento').value,
            invitacion : button.up('form').getForm().findField('invitacion').value,
            aulaTeatro : button.up('form').getForm().findField('aulaTeatro').value,
            localizacion: localizacionSeleccionada,
            localizacion_nombre: button.up('form').getForm().findField('localizacion').rawValue
         });

         console.log(precioSesion);

         if (indiceFilaSeleccionada != -1) {
            var recordSeleccionado = this.getGridPreciosSesion().store.getAt(indiceFilaSeleccionada);
            recordSeleccionado.set('precio', precioSesion.data.precio);
            recordSeleccionado.set('descuento', precioSesion.data.descuento);
            recordSeleccionado.set('invitacion', precioSesion.data.invitacion);
            recordSeleccionado.set('aulaTeatro', precioSesion.data.aulaTeatro);
            recordSeleccionado.set('localizacion', precioSesion.data.localizacion);
            recordSeleccionado.set('localizacion_nombre', precioSesion.data.localizacion_nombre);
            recordSeleccionado.commit(true);
         }
         else
            this.getGridPreciosSesion().store.add(precioSesion);

         this.getGridPreciosSesion().getView().refresh();
         button.up('window').close();
      } else {
         alert(UI.i18n.message.registroConMismaLocalizacionExiste)
      }
   },
   
   preparaStoresSesiones: function(comp, opts) {
	   this.preparaStorePlantillaPrecios();
	   this.preparaStoreSalas();
   },

   preparaStorePlantillaPrecios: function() {
      var idASeleccionar = -1;
      if (this.getGridSesiones().getSelectedColumnId() != undefined) {
         var selectedRecord = this.getGridSesiones().getSelectedRecord();
         idASeleccionar = selectedRecord.data.plantillaPrecios;
      }
      this.getFormSesiones().cargaComboStore('plantillaPrecios', idASeleccionar);
   },   
   
   preparaStoreSalas: function() {
      var idASeleccionar = -1;
      if (this.getGridSesiones().getSelectedColumnId() != undefined) {
         var selectedRecord = this.getGridSesiones().getSelectedRecord();
         idASeleccionar = selectedRecord.data.sala;
      }
      this.getFormSesiones().cargaComboStore('sala', idASeleccionar);
   },     
   
   showImagenIfExists: function(comp, opts) {
      var record = comp.getRecord();
      
	   if (record != undefined && record.data["imagenSrc"]) {
         var imagen = comp.down("#imagenInsertada");
         var idEvento = record.data["id"]; 
		   imagen.html = '<a href="' + urlPrefix + 'evento/' + idEvento + '/imagen" target="blank">' + UI.i18n.field.imagenInsertada + '</a>';
         this.getBotonDeleteImagen().show();
      } else {
         this.getBotonDeleteImagen().hide();
      }
   },

   deleteImage: function(button, event, opts) {
      if (confirm(UI.i18n.message.sureDeleteImage)) {
         var record = button.up('form').getRecord();
         var idEvento = record.data["id"];
         var grid = this.getGridEventos();
         
         Ext.Ajax.request({
           url : urlPrefix + 'evento/' + idEvento + '/imagen',
           method: 'DELETE',
           success: function (response) {
               alert(UI.i18n.message.deletedImage);

               button.up('window').close();
               grid.store.load();
           }, failure: function (response) {
              alert(UI.i18n.error.deletedImage);
           }
         });
      }
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE EVENTOS");
      this.getGridEventos().recargaStore();
   },

   addEvento: function(button, event, opts) {
      this.getGridEventos().showAddEventoWindow();
   },

   editEvento: function(button, event, opts) {
      this.getGridEventos().edit('formEventos', undefined, undefined, 0.8);
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
	   if (this.getGridEventos().getSelectedColumnId()) {
         this.getGridSesiones().deseleccionar();
		   this.getGridSesiones().showAddSesionWindow();
      }
	   else
		   alert(UI.i18n.message.event);
   },
   
   saveSesionFormData: function(button, event, opts) {
	   var eventoId = this.getGridEventos().getSelectedColumnId();

	   var grid = this.getGridSesiones();
	   var form = this.getFormSesiones();

      this.getFormSesiones().getForm().findField('preciosSesion').setValue(this.getGridPreciosSesion().toJSON());
      
      var sala = this.getFormSesiones().getForm().findField('sala');
      
      if (sala.getValue() == '')
    	  sala.setValue(null);
      
	  form.saveFormData(grid, urlPrefix + 'evento/' + eventoId + '/sesiones');
   },
   
   removeSesion: function(button, event, opts) {
      this.getGridSesiones().remove();
   },
   
   editSesion: function(button, event, opts) {
      this.getGridSesiones().edit('formSesiones', undefined, undefined, 0.8);
   }
});