Ext.define('Paranimf.view.EditModalWindow', {
  extend: 'Ext.window.Window',
  alias: 'widget.editmodalwindow',
  layout: 'fit',
  
  //autoHeight: true,
  //autoScroll: true,
  modal: true,
  closable: false,

  listeners: {
    show: function(win) {
      var map = new Ext.util.KeyMap(Ext.getBody(), [{
        key: Ext.EventObject.ESC,
        defaultEventAction: 'preventDefault',
        scope: this,
        fn: function(){win.close()}
      }]);
    }
  }
});