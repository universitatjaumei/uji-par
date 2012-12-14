Ext.Loader.setConfig({
   enabled: true/*,
   paths: {
      "Extensible": "extensible/src",
      "Extensible.example": "extensible/examples"
   }*/
});

/*Ext.Ajax.on('requestexception', function (conn, response, options) {
    if (response.status === 302 || response.status === 0) {
        window.location = 'http://www.uji.es';
    }
});*/

Ext.application({
   name: 'Paranimf',
   autoCreateViewport: false,
   appFolder: 'app',
   controllers: controllers,

   launch: function() {
      Ext.create('Ext.container.Viewport', {
         layout: 'border',         
         items: [{
            xtype: 'panel',
            region: 'west',
            layout: 'fit',
            width: 215
         }, {
            layout: 'vbox',
            region: 'center',
            items: [{
               xtype: 'form',
               height: 25,
               region: 'north',
               width: '100%',
               items: [{
                  xtype: 'button',
                  text: UI.i18n.button.closeSession,
                  style: 'float: right;margin-right:2em',
                  action: 'logout'
               },{
                  xtype: 'label',
                  text: botonLogout,
                  style: 'float: right;margin-right:1em;font-weight:bold;margin-top:5px'
               }]
            }, {
               style : 'margin:2em;',
               xtype: 'panel',
               layout: 'card',
               region: 'south',
               width: '100%',
               autoScroll: true,
               flex: 1,
               items: [{
                     xtype: 'dashboard'
                  }, {
                     xtype: 'gridUsuarios'
                  }, {
                     xtype: 'gridTiposEventos'
                  }, {
                     xtype: 'panelEventos'
                  }, {
                     xtype: 'gridLocalizaciones'
                  }],
               listeners: {
                  afterlayout: function(container, layout, opts) {
                     document.getElementById("divCargador").style.display = 'none';
                  }
               }
            }]
         }]
      });

      Ext.History.init(this.initDispatch, this);
      Ext.History.on('change', this.historyChange, this);
      
      if (Ext.History.getToken() == null) {
         Ext.History.add('Dashboard', true);
      }

      this.loadMenuContent();
   },

   initDispatch: function() {
      this.historyChange(Ext.History.getToken());
   },

   historyChange: function(token) {
      var cardPanel = Ext.ComponentQuery.query('viewport > panel[region=center] > panel[region=south]')[0];
      var itemIndex = screens[token];

      cardPanel.getLayout().setActiveItem(itemIndex);
   },

   loadMenuContent: function() {
      Ext.Ajax.request({
         url: menuUrl,
         success: function(response, opts) {
            var menuPanel = Ext.ComponentQuery.query('viewport > panel[region=west]')[0];
            menuPanel.update(response.responseText, false, function() {
               seleccionaMenu(menu);
            });
         }
      });
   }
});
