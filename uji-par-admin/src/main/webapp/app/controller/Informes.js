Ext.define('Paranimf.controller.Informes', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'informes.PanelInformes', 'informes.GridSesionesInformes'],
   stores: ['SesionesInformes'],
   models: ['Sesion'],

   refs: [{
      ref: 'fechaInicio',
      selector: 'panelInformes datefield[name=fechaInicio]'
   }, {
      ref: 'fechaFin',
      selector: 'panelInformes datefield[name=fechaFin]'
   }, {
      ref: 'gridSesionesInformes',
      selector: 'gridSesionesInformes'
   }],

   init: function() {
      this.control({

         'panelInformes button[action=generateExcelTaquilla]': {
           click: this.generateExcelTaquilla
         },

         'panelInformes button[action=generatePdfTaquilla]': {
           click: this.generatePdfTaquilla
         },   
          
         'panelInformes button[action=generatePdfEfectivo]': {
           click: this.generatePdfEfectivo
         },      
         
         'panelInformes button[action=generatePdfTpv]': {
           click: this.generatePdfTpv
         }, 
         
         'panelInformes button[action=generatePdfEventos]': {
           click: this.generatePdfEventos
         },          

         'panelInformes button[action=generateExcelEvento]': {
           click: this.generateExcelEvento
         }, 

         'panelInformes button[action=filtrarSessions]': {
            click: this.filtraSesiones
         },

         'panelInformes button[action=generarInformeSesion]': {
            click: this.generarInformeSesion
         },

         'panelInformes button[action=generarInformeEvento]': {
            click: this.generarInformeEvento
         }
      });
   },

   generarInformeSesion: function() {
      var idsSelected = this.getGridSesionesInformes().getSelectedColumnValues("id");
      if (idsSelected && idsSelected.length == 1) {
         this.generatePdfSesion(idsSelected[0]);
      } else
         alert(UI.i18n.message.selectRow);
   },

   generarInformeEvento: function() {
      var idsSelected = this.getGridSesionesInformes().getSelectedColumnValues("evento");
      if (idsSelected && idsSelected.length == 1) {
         this.generatePdfEvento(idsSelected[0].id);
      } else
         alert(UI.i18n.message.selectRow);
   },

   filtraSesiones: function() {
      if (!this.sonFechasValidas(this.getFechaInicio().value, this.getFechaFin().value))
         return;

      var storeSesiones = this.getGridSesionesInformes().getStore();
      storeSesiones.getProxy().url = urlPrefix + 'evento/sesionesficheros/todo';
      storeSesiones.getProxy().extraParams = {
         'fechaInicio': this.getFechaInicio().rawValue,
         'fechaFin': this.getFechaFin().rawValue
      };
      storeSesiones.load();
   },

   generateExcelTaquilla: function(button, event, opts) {
      console.log(this.getFechaInicio(), this.getFechaFin());
      
      if (!this.sonFechasValidas(this.getFechaInicio().value, this.getFechaFin().value))
         return;

      var strFechaInicio = this.getStrFecha(this.getFechaInicio().value);
      var strFechaFin = this.getStrFecha(this.getFechaFin().value);

      var form = document.createElement("form");
      form.setAttribute("method", "post");
      form.setAttribute("action", urlPrefix + 'report/taquilla/' + strFechaInicio + '/' + strFechaFin);
      form.setAttribute("target", "_blank");

      document.body.appendChild(form);
      form.submit();
   }, 

   generatePdfSesion: function(idSesion) {
      var form = document.createElement("form");
      form.setAttribute("method", "post");
      form.setAttribute("action", urlPrefix + 'report/sesion/' + idSesion + '/pdf');
      form.setAttribute("target", "_blank");

      document.body.appendChild(form);
      form.submit();      
   },

   generatePdfEvento: function(idEvento) {
      var form = document.createElement("form");
      form.setAttribute("method", "post");
      form.setAttribute("action", urlPrefix + 'report/evento/' + idEvento + '/pdf');
      form.setAttribute("target", "_blank");

      document.body.appendChild(form);
      form.submit();      
   },
   
   generatePdfTaquilla: function(button, event, opts) {
      console.log(this.getFechaInicio(), this.getFechaFin());
      
      if (!this.sonFechasValidas(this.getFechaInicio().value, this.getFechaFin().value))
         return;

      var strFechaInicio = this.getStrFecha(this.getFechaInicio().value);
      var strFechaFin = this.getStrFecha(this.getFechaFin().value);

      var form = document.createElement("form");
      form.setAttribute("method", "post");
      form.setAttribute("action", urlPrefix + 'report/taquilla/' + strFechaInicio + '/' + strFechaFin + '/pdf');
      form.setAttribute("target", "_blank");

      document.body.appendChild(form);
      form.submit();
   },    
   
   generatePdfEfectivo: function(button, event, opts) {
      console.log(this.getFechaInicio(), this.getFechaFin());
      
      if (!this.sonFechasValidas(this.getFechaInicio().value, this.getFechaFin().value))
         return;

      var strFechaInicio = this.getStrFecha(this.getFechaInicio().value);
      var strFechaFin = this.getStrFecha(this.getFechaFin().value);

      var form = document.createElement("form");
      form.setAttribute("method", "post");
      form.setAttribute("action", urlPrefix + 'report/taquilla/' + strFechaInicio + '/' + strFechaFin + '/efectivo/pdf');
      form.setAttribute("target", "_blank");

      document.body.appendChild(form);
      form.submit();
   },    
   
   generatePdfTpv: function(button, event, opts) {
      console.log(this.getFechaInicio(), this.getFechaFin());
      
      if (!this.sonFechasValidas(this.getFechaInicio().value, this.getFechaFin().value))
         return;

      var strFechaInicio = this.getStrFecha(this.getFechaInicio().value);
      var strFechaFin = this.getStrFecha(this.getFechaFin().value);

      var form = document.createElement("form");
      form.setAttribute("method", "post");
      form.setAttribute("action", urlPrefix + 'report/taquilla/' + strFechaInicio + '/' + strFechaFin + '/tpv/pdf');
      form.setAttribute("target", "_blank");

      document.body.appendChild(form);
      form.submit();
   }, 
   
   generatePdfEventos: function(button, event, opts) {
      console.log(this.getFechaInicio(), this.getFechaFin());
      
      if (!this.sonFechasValidas(this.getFechaInicio().value, this.getFechaFin().value))
         return;

      var strFechaInicio = this.getStrFecha(this.getFechaInicio().value);
      var strFechaFin = this.getStrFecha(this.getFechaFin().value);

      var form = document.createElement("form");
      form.setAttribute("method", "post");
      form.setAttribute("action", urlPrefix + 'report/taquilla/' + strFechaInicio + '/' + strFechaFin + '/eventos/pdf');
      form.setAttribute("target", "_blank");

      document.body.appendChild(form);
      form.submit();
   },    

   sonFechasValidas: function(fechaInicio, fechaFin) {
      if (!fechaInicio || fechaInicio == '' || !fechaFin || fechaFin == '') {
         alert(UI.i18n.error.fechasObligatorias);
         return false;
      }
      return true;
   },

   getStrFecha: function(fecha) {
      var dt = fecha;
      var mes = dt.getMonth() + 1;
      mes = (mes>9)?mes:'0' + mes;
      var dia = (dt.getDate() > 9)?dt.getDate():'0' + dt.getDate();
      return dt.getFullYear() + '-' + mes + '-' + dia;
   },

   generateExcelEvento: function(button, event, opts) {
      console.log(this.getFechaInicio(), this.getFechaFin());
      
      if (!this.sonFechasValidas(this.getFechaInicio().value, this.getFechaFin().value))
         return;

      var strFechaInicio = this.getStrFecha(this.getFechaInicio().value);
      var strFechaFin = this.getStrFecha(this.getFechaFin().value);

      var form = document.createElement("form");
      form.setAttribute("method", "post");
      form.setAttribute("action", urlPrefix + 'report/eventos/' + strFechaInicio + '/' + strFechaFin);
      form.setAttribute("target", "_blank");

      document.body.appendChild(form);
      form.submit();
   }
});