Ext.define('Paranimf.view.dashboard.Dashboard', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.dashboard',

	initComponent: function() {
		var me = this;

		Ext.Ajax.request({
			url: '../dashboard_' + lang + '.html',
			success: function(response, opts) {
				me.update(response.responseText);
			}
		});

		this.callParent(arguments);
	},
});