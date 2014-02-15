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

   setIncidencia: function() {
      if (this.getGridSesionesCompleto().rowsSelectedCount() == 1) {
         this.getGridSesionesCompleto().showIncidenciasForm();
      } else {
         alert(UI.i18n.message.onlyOneRowSelected);
      }
   },

   saveIncidencia: function() {
      var rec = this.getGridSesionesCompleto().getSelectedRecord();
      var url = urlPrefix + 'evento/sesionesficheros/' + rec.data.id + '/incidencia/' + this.getComboIncidencias().value;
      this.getFormIncidencias().saveFormData(this.getGridSesionesCompleto(), url, 'PUT');
   },

   markAsSent: function() {
      var idsSelected = this.getGridSesionesCompleto().getSelectedColumnValues("idEnvioFichero");
      console.log(idsSelected);
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
      console.log(idsSelected);
      if (idsSelected && idsSelected.length > 0) {
         this.getGridSesionesCompleto().showDatosFicheroForm();
      } else
         alert(UI.i18n.message.noRowSelectedFileICAA);
   },

   saveFileICAA: function() {
      if (this.getFormDatosFichero().isValid()) {
         var me = this;
         var idsSelected = this.getGridSesionesCompleto().getSelectedColumnIds();
         var fechaEnvioHabitualAnterior = this.getFechaUltimoEnvioHabitual().rawValue;
         var tipoEnvio = this.getTipoEnvio().value;

         this.getFormDatosFichero().getForm().doAction('standardsubmit', {
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
      }
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