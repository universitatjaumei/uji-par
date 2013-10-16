Ext.define('Paranimf.controller.Informes', {
   extend: 'Ext.app.Controller',

   views: ['EditModalWindow', 'EditBaseForm', 'EditBaseGrid', 'informes.PanelInformes'],
   stores: [],
   models: [],

   refs: [{
      ref: 'fechaInicio',
      selector: 'panelInformes datefield[name=fechaInicio]'
   }, {
      ref: 'fechaFin',
      selector: 'panelInformes datefield[name=fechaFin]'
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

         'panelInformes button[action=generateExcelEvento]': {
           click: this.generateExcelEvento
         }
      });
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