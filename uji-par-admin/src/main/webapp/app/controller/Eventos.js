Ext.define('Paranimf.controller.Eventos', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'evento.GridEventos', 'evento.FormEventos', 'evento.PanelEventos', 'evento.GridSesiones', 'evento.FormSesiones', 'evento.GridPreciosSesion', 'evento.FormPreciosSesion'],
   stores: ['Eventos', 'TiposEventosSinPaginar', 'Sesiones', 'PlantillasPrecios', 'PreciosSesion'],
   models: ['Evento', 'Sesion', 'PrecioSesion'],

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
      ref: 'gridPreciosSesion',
      selector: 'gridPreciosSesion'
   }, {
      ref: 'formPreciosSesion',
      selector: 'formPreciosSesion'
   }],

   init: function() {
      this.control({

         'gridEventos button[action=edit]': {
            click: this.editEvento
         },

         'panelEventos': {
			   beforeactivate: this.recargaStore
         },

         'formEventos button[action=save]': {
            click: this.saveEventoFormData
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
            afterrender: this.preparaStorePlantillaPrecios
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

   preparaStorePlantillaPrecios: function(comp, opts) {
      var idASeleccionar = -1;
      if (this.getGridSesiones().getSelectedColumnId() != undefined) {
         var selectedRecord = this.getGridSesiones().getSelectedRecord();
         idASeleccionar = selectedRecord.data.plantillaPrecios;
      }
      this.getFormSesiones().cargaComboStore('plantillaPrecios', idASeleccionar);
      
      //TODO cargar con un Ajax.request el store del grid
      //this.getGridPreciosSesion().getStore().getProxy().url = urlPrefix + 'evento/' + this.getGridEventos().getSelectedColumnId() + '/sesiones/' + this.getGridSesiones().getSelectedColumnId() + '/precios';
      //this.getGridPreciosSesion().recargaStore();
   },   
   
   showImagenIfExists: function(comp, opts) {
      var record = comp.getRecord();
      
	   if (record != undefined && record.data["imagenSrc"]) {
         var imagen = comp.down("#imagenInsertada");
         var idEvento = record.data["id"]; 
		   imagen.html = '<a href="' + urlPrefix + 'evento/' + idEvento + '/imagen" target="blank">' + UI.i18n.field.imagenInsertada + '</a>';
      }
   },

   recargaStore: function(comp, opts) {
      console.log("RECARGA STORE EVENTOS");
      this.getGridEventos().recargaStore();
   },

   editEvento: function(button, event, opts) {
      this.getGridEventos().edit('formEventos', undefined, undefined, 0.8);
   },

   saveEventoFormData: function(button, event, opts) {
      var grid = this.getGridEventos();
      var form = this.getFormEventos();
      form.saveFormData(grid, urlPrefix + 'evento', undefined, 'application/x-www-form-urlencoded');
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
	   form.saveFormData(grid, urlPrefix + 'evento/' + eventoId + '/sesiones');
   },
   
   removeSesion: function(button, event, opts) {
      this.getGridSesiones().remove();
   },
   
   editSesion: function(button, event, opts) {
      this.getGridSesiones().edit('formSesiones', undefined, undefined, 0.8);
   }
});