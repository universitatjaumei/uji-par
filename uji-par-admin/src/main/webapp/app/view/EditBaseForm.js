Ext.define('Paranimf.view.EditBaseForm', {
  extend: 'Ext.form.Panel',

  autoHeight: true,
  bodyPadding: '20 20 10 20',

   buttons: [{
      xtype: 'button',
      text: UI.i18n.button.save,
      action: 'save'
   }, {
      xtype: 'button',
      text: UI.i18n.button.cancel,
      handler: function() {
         this.up('window').close();
      }
   }],

  saveFormData: function(grid, url, method, contentType, callback) {
    var me = this;
    var id = me.getForm().findField('id').getValue();
    var methodHTTP;
    var ct;
    var formURL;
    
    if (me.getForm().isValid()) {
      if (method) {
        methodHTTP = method;
        formURL = url;
      }
      else {
        methodHTTP = (id) ? 'PUT' : 'POST';
        formURL = url + '/' + ((id) ? id : '');
      }
      
      if (contentType)
    	  ct = contentType;
      else
    	  ct = 'application/json';
    	  //ct = 'application/x-www-form-urlencoded';

      me.setLoading(UI.i18n.message.saving);
      
      if (contentType) {
    	  me.getForm().submit({
  	        method: methodHTTP,
  	        url: formURL,
  	        headers: { 'Content-Type': ct },
  	        params: Ext.JSON.encode(me.getValues()),
  	        success: function(form, action) {
  	          me.up('window').close();
  	          grid.store.load();
  	        },failure: function(form, action) {
  	            if (callback)
  	              callback(form, action);
  	            else {
  	              alert(UI.i18n.error.formSave);
  	            }
  	            me.setLoading(false);
  	        }
  	      });
      } else {
    	  Ext.Ajax.request({
	    	  url : formURL,
	    	  method: methodHTTP,
	    	  jsonData: me.getForm().getValues(),
	    	  success: function (response) {
	    		  me.up('window').close();
	              grid.store.load();
	    	  }, failure: function (response) {
	    		  if (callback)
	    			  callback(form, action);
	    		  else
	    			  alert(UI.i18n.error.formSave);
	              me.setLoading(false);
	    	  }
	   	  });
      }
    } else {
      alert(UI.i18n.error.form);
    }
  },

  addRedMarkIfRequired: function(component) {
    if (component.fieldLabel && !component.allowBlank && component.xtype != 'checkbox')
      component.fieldLabel += ' <span class="req" style="color:red">*</span>';
  },

  cargaStoreElemento: function(elemento) {
      if (elemento.store.count() == 0)
         elemento.store.load(function(records, operation, success) {
            if (success)
               elemento.setDisabled(false);
         });
      else
         elemento.setDisabled(false);
  },

  listeners: {
    'beforeadd': function(container, component, index, opts) {
      if (component.xtype != 'fieldset')
        this.addRedMarkIfRequired(component);
      else {
        for (var i=0;i<component.items.length;i++) {
          this.addRedMarkIfRequired(component.items.items[i]);
        }
      }
    }
  }
});