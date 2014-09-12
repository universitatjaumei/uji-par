Ext.define('Paranimf.controller.Dashboard', {
	extend: 'Ext.app.Controller',

	views: ['dashboard.Dashboard'],

    init: function() {
        this.control({
            'viewport > panel[region=center] > panel[region=north] button[action=logout]': {
                click: function(button, opts) {
                    window.location = urlLogout;
                }
            }
        });
    }
});
