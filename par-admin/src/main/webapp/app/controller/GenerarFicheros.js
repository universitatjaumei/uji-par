Ext.define('Paranimf.controller.GenerarFicheros', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'generarficheros.PanelSesiones', 'generarficheros.GridSesionesCompleto', 'generarficheros.FormDatosFichero', 'generarficheros.FormIncidencias'],
   stores: ['SesionesFicheros', 'TipoEnvio', 'Incidencias'],
   models: ['Sesion'],

   refs: [{
	   ref: 'gridSesionesCompleto',
	   selector: 'gridSesionesCompleto'
   }, {
      ref: 'txtFechaInicial',
      selector: 'panelSesionesFicheros datefield[name=fechaInicio]'
   }, {
      ref: 'txtFechaFinal',
      selector: 'panelSesionesFicheros datefield[name=fechaFin]'
   }, {
      ref: 'formDatosFichero',
      selector: 'formDatosFichero'
   }, {
      ref: 'fechaUltimoEnvioHabitual',
      selector: 'formDatosFichero datefield[name=fechaUltimoEnvioHabitual]'
   }, {
      ref: 'tipoEnvio',
      selector: 'formDatosFichero combobox[name=tipoEnvio]'
   }, {
      ref: 'comboIncidencias',
      selector: 'formIncidencias combobox[name=incidencias]'
   }, {
      ref: 'formIncidencias',
      selector: 'formIncidencias'
   }],

   init: function() {
      this.control({
         'panelSesionesFicheros button[name=filtrar]': {
            click: this.filtrarSesiones
         },

         'gridSesionesCompleto button[action=saveFileICAA]': {
            click: this.showOpcionsFileICAA
         },

         'formDatosFichero button[action=save]': {
            click: this.saveFileICAA
         },

         'gridSesionesCompleto button[action=markAsSent]': {
            click: this.markAsSent
         },

         'formDatosFichero button[action=cancel]': {
            click: this.reloadGrid
         }, 

         'panelSesionesFicheros button[action=setIncidencia]': {
            click: this.setIncidencia
         },

         'formIncidencias button[action=save]': {
            click: this.saveIncidencia
         }
      });
   },

   isAnyAlreadySent: function() {
      var records = this.getGridSesionesCompleto().getSelectionModel().getSelection();
      for (var i=0;i<records.length;i++) {
         if (records[i].data.fechaGeneracionFichero != undefined)
            return true;
      }
   },

   setIncidencia: function() {
      if (!this.isAnyAlreadySent()) {
         if (this.getGridSesionesCompleto().rowsSelectedCount() == 1) {
            this.getGridSesionesCompleto().showIncidenciasForm();
         } else {
            alert(UI.i18n.message.onlyOneRowSelected);
         }
      } else
         alert(UI.i18n.error.fitxerJaGenerat);
   },

   saveIncidencia: function() {
      var rec = this.getGridSesionesCompleto().getSelectedRecord();
      var url = urlPrefix + 'evento/sesionesficheros/' + rec.data.id + '/incidencia/' + this.getComboIncidencias().value;
      this.getFormIncidencias().saveFormData(this.getGridSesionesCompleto(), url, 'PUT');
   },

   markAsSent: function() {
      var idsSelected = this.getGridSesionesCompleto().getSelectedColumnValues("idEnvioFichero");
      //console.log(idsSelected);
      if (idsSelected && idsSelected.length > 0) {
         this.getGridSesionesCompleto().setLoading(UI.i18n.message.loading);
         var me = this;
         Ext.Ajax.request({
            url : urlPrefix + 'comunicacionesicaa',
            method: 'PUT',
            params: {
               ids: idsSelected
            },
            success: function (response) {
              me.getGridSesionesCompleto().setLoading(false);
              me.reloadGrid();
            }, failure: function (response) {
              me.getGridSesionesCompleto().setLoading(false);
              alert(UI.i18n.error.markAsSent);
            }
         });
      } else
         alert(UI.i18n.message.noRowSelectedFileICAA);
   },

   reloadGrid: function() {
      this.getGridSesionesCompleto().recargaStore();
   },

   showOpcionsFileICAA: function() {
      var idsSelected = this.getGridSesionesCompleto().getSelectedColumnIds();
      //console.log(idsSelected);
      if (!this.isAnyAlreadySent()) {
         if (idsSelected && idsSelected.length > 0)
            this.getGridSesionesCompleto().showDatosFicheroForm();
         else
            alert(UI.i18n.message.noRowSelectedFileICAA);
      } else
         alert(UI.i18n.error.fitxerJaGenerat);
   },

   saveFileICAA: function() {
      if (this.getFormDatosFichero().isValid()) {
         if (!this.isAnyAlreadySent()) {
            var me = this;
            var idsSelected = this.getGridSesionesCompleto().getSelectedColumnIds();
            var fechaEnvioHabitualAnterior = this.getFechaUltimoEnvioHabitual().rawValue;
            var tipoEnvio = this.getTipoEnvio().value;
            this.getFormDatosFichero().setLoading(UI.i18n.message.loading);

            Ext.Ajax.request({
               url : urlPrefix + 'comunicacionesicaa/check',
               method: 'POST',
               params: {
                  ids: idsSelected,
                  tipoEnvio: tipoEnvio
               },
               success: function (response) {
                  me.getFormDatosFichero().getForm().doAction('standardsubmit', {
                     standardSubmit: true,
                     target: '_blank',
                     url : urlPrefix + 'comunicacionesicaa',
                     method: 'POST',
                     timeout: 120000,
                     params: {
                        ids: idsSelected,
                        fechaEnvioHabitualAnterior: fechaEnvioHabitualAnterior,
                        tipoEnvio: tipoEnvio
                     }
                  });
                  me.getFormDatosFichero().setLoading(false);
               }, failure: function (response) {
                  me.getFormDatosFichero().setLoading(false);
                  var responseText = me.getResponseText(response);
                  console.log(responseText);
                  if (responseText != undefined) {
                     if (responseText.codi != undefined && (responseText.codi >= 519 && responseText.codi <= 546)) {
                        console.log(responseText.codi, eval("UI.i18n.error.error" + responseText.codi));
                        alert(eval("UI.i18n.error.error" + responseText.codi));
                     }
                     else
                        alert(UI.i18n.error.markAsSent);
                  } else
                     alert(UI.i18n.error.markAsSent);
               }
            });
         } else
            alert(UI.i18n.error.fitxerJaGenerat);
      }
   },

   getResponseText: function(response) {
      console.log(response);
      if (response.responseText != undefined)
         return Ext.JSON.decode(response.responseText, true);
      return undefined;  
   },

   filtrarSesiones: function() {
      if (this.getTxtFechaInicial().rawValue || this.getTxtFechaFinal().rawValue) {
         var storeSesiones = this.getGridSesionesCompleto().getStore();

         storeSesiones.getProxy().url = urlPrefix + 'evento/sesionesficheros';
         storeSesiones.getProxy().extraParams = {
            'fechaInicio': this.getTxtFechaInicial().rawValue,
            'fechaFin': this.getTxtFechaFinal().rawValue
         };
         storeSesiones.load();
      }
   }
});