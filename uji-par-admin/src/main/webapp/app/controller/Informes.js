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
         }
      });
   },

   generateExcelTaquilla: function(button, event, opts) {
      console.log(this.getFechaInicio(), this.getFechaFin());
      if (!this.getFechaInicio().value || this.getFechaInicio().value == '' || !this.getFechaFin().value || this.getFechaFin().value == '') {
         alert(UI.i18n.error.fechasObligatorias);
         return;
      }

      var dtInicio = this.getFechaInicio().value;
      var mesInicio = dtInicio.getMonth() + 1;
      mesInicio = (mesInicio>9)?mesInicio:'0' + mesInicio;
      var diaInicio = (dtInicio.getDate() > 9)?dtInicio.getDate():'0' + dtInicio.getDate();
      var strFechaInicio = dtInicio.getFullYear() + '-' + mesInicio + '-' + diaInicio;

      var dtFin = this.getFechaFin().value;
      var mesFin = dtFin.getMonth() + 1;
      mesFin = (mesFin>9)?mesFin:'0' + mesFin;
      var diaFin = (dtFin.getDate() > 9)?dtFin.getDate():'0' + dtFin.getDate();
      var strFechaFin = dtFin.getFullYear() + '-' + mesFin + '-' + diaFin;

      var form = document.createElement("form");
      form.setAttribute("method", "post");
      form.setAttribute("action", urlPrefix + 'report/taquilla/' + strFechaInicio + '/' + strFechaFin);
      form.setAttribute("target", "_blank");

      document.body.appendChild(form);
      form.submit();
   }
});