Ext.define('Paranimf.view.EditBaseGrid', {
   extend: 'Ext.grid.Panel',

   viewConfig: {
      loadingText: UI.i18n.message.loading
   },

   tbar: [{
      xtype: 'button',
      text: UI.i18n.button.add,
      action: 'add'
   }, {
      xtype: 'button',
      text: UI.i18n.button.edit,
      action: 'edit'
   }, {
      xtype: 'button',
      text: UI.i18n.button.delete,
      action: 'del'
   }],

   getSelectedRecord: function(grid) {
      var selectedRows = grid.getSelectionModel().getSelection();
      var indiceFilaSeleccionada = grid.getStore().indexOf(selectedRows[0]);
      return grid.getStore().getAt(indiceFilaSeleccionada);
   },

   createModalWindow: function(xtype, width, height) {
      return Ext.create('Paranimf.view.EditModalWindow', {
         title: this.title,
         items: [{
            autoScroll: true,
            xtype: xtype,
            width: (width)? width: 500,
            height: (height)? height: 'auto'
         }]
      });
   },

   toJSON: function() {
      var jsonFilas = "";

      for (var i=0;i<this.store.getCount();i++) {
         if (jsonFilas != "")
            jsonFilas += ",";
         jsonFilas += Ext.JSON.encode(this.store.getRange()[i].getData());
      }
      jsonFilas = "[" + jsonFilas + "]";

      return jsonFilas;
   },

   /*edit: function(xtype) {
      this.edit(xtype, null);
   },*/

   edit: function(xtype, arrayComboClearFilter, width, height) {
      var selectedRows = this.getSelectionModel().getSelection();
      console.log(selectedRows[0]);
      if (selectedRows.length == 0)
         alert(UI.i18n.message.selectRow);
      else {
         if (!width) width = 500;

         var modalWindow = this.createModalWindow(xtype, width, height);
         var form = modalWindow.down('form').getForm();
         
         this.clearFilter(form, arrayComboClearFilter);
         
         modalWindow.down('form').loadRecord(selectedRows[0]);

         if(selectedRows[0].data.color)
            modalWindow.down('calendarColorPicker').select(selectedRows[0].data.color);
         modalWindow.show();
      }
   },

   clearFilter: function(form, arrayComboClearFilter) {
      if (arrayComboClearFilter)
      {
         for (var i = 0; i < arrayComboClearFilter.length; i++)
         {
            var comboEspacio = form.findField(arrayComboClearFilter[i]);
            if (comboEspacio && comboEspacio.store != undefined)
            {
               comboEspacio.store.clearFilter(true);
            }
         }
      }
   },

   remove: function(callback) {
      var records = this.getSelectionModel().getSelection();
      if (records.length == 0) {
         alert(UI.i18n.message.selectRow);
         if (callback)
               callback(false);
      }
      else {
         if (confirm(UI.i18n.message.sureDelete)) {
            var st = this.store;
            this.store.getProxy().on('exception', function(proxy, resp, operation) {
               alert(UI.i18n.error.element);
               st.add(st.getRemovedRecords());
            });
            this.store.remove(records);
            if (callback)
               callback(true);
         }
      }
   },

   getSelectedColumnId: function()
   {
      var records = this.getSelectionModel().getSelection();

      if (records[0])
         return records[0].get("id");
      else
         return undefined;
   },

   hasRowSelected: function() {
      var records = this.getSelectionModel().getSelection();

      if (records.length == 0)
         return false;
      else
         return true;
   },

   addItemToStore: function(item) {
      this.store.add(item);
   },

   updateSelectedItem: function(newItem) {
      this.getSelectedRecord(this).set(newItem);
   },

   recargaStore: function() {
      this.store.clearFilter();
      this.store.load();
   }
});