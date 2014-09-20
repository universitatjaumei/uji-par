Ext.define('Paranimf.view.evento.GridEventosMultisesion', {
   extend: 'Paranimf.view.EditBaseGrid',

   alias: 'widget.gridEventosMultisesion',
   store: 'EventosMultisesion',
   title: UI.i18n.gridTitle.multisesion,

   tbar: [{
      xtype: 'button',
      text: UI.i18n.button.add,
      action: 'add'
   }, {
      xtype: 'button',
      text: UI.i18n.button.del,
      action: 'del'
   }],

   	initComponent: function() {

    	this.columns = [{
        	dataIndex: 'id',
        	hidden: true,
        	text: UI.i18n.field.idIntern
      	}, {
	        dataIndex: 'tituloEs',
        	text: UI.i18n.field.titolNeutreLabel,
        	flex: 5
      	}];

      	this.callParent(arguments);
   	},

   	showAddPelicula: function() {
   		this.createModalWindow('formPeliculaMultisesion', undefined, undefined, UI.i18n.formTitle.peliculas).show();
   	}
});