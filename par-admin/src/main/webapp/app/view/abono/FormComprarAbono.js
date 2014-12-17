Ext.define('Paranimf.view.abono.FormComprarAbono', {
   extend: 'Paranimf.view.EditBaseForm',
   alias: 'widget.formComprarAbono',
   layout: 'fit',

   url : urlPrefix + 'sesiones',
   
   defaults: {
      anchor: '100%',
      xtype: 'textfield',
      labelWidth: 190
   },
   
   buttons: undefined,
   
   bbar: [{
       id: 'anularPrevia',
       text: UI.i18n.button.anularPrevia
   }, 
   '->',
   {
       id: 'comprarAnterior',
       text: UI.i18n.button.anterior
   },{
       id: 'comprarSiguiente',
       text: UI.i18n.button.siguiente
   },{
       id: 'comprarCancelar',
       text: UI.i18n.button.close
   }],

   items: [{
       xtype: 'panel',
       id: 'formComprarAbonoCards',
       frame: false,
       layout: 'card',
       border: false,
       autoScroll: true,
       items: [{
                id    : 'pasoSeleccionar',
                xtype : 'panel',
                layout: 'card',
                border: false,
                frame: false,
                items: [{
                    id    : 'iframeButacas',
                    xtype : 'component',
                    autoEl : {
                        tag : 'iframe',
                        src : ''
                    }
                }]
           },
           {
                id: 'pasoPagar',
                xtype: 'panel',
                layout: {
                    type: 'vbox',
                    align: 'center',
                    pack: 'start'
                },
                border: 0,
                frame: false,
                items: [
                        {
                            name: 'panelComprar',
                            xtype: 'panel',
                            region: 'center',
                            frame: false,
                            align: 'middle',
                            border: 0,
                            layout: {
                                align: 'center',
                                pack: 'start',
                                type: 'vbox'
                            },
                            defaults: {
                                frame: false,
                                border: 0,
                                margin: '10px',
                                style: {
                                    fontSize: '20px'
                                },
                                fieldStyle: {
                                    fontSize: '20px'
                                }
                            },
                            items: [{
                                  fieldLabel : UI.i18n.field.nameMulti,
                                  name : 'nombre',
                                  xtype : 'textfield',
                                  allowBlank : false
                                }, {
                                  fieldLabel : UI.i18n.field.surnameMulti,
                                  name : 'apellidos',
                                  xtype : 'textfield',
                                  allowBlank : false
                                }, {
                                  fieldLabel : UI.i18n.field.address,
                                  name : 'direccion',
                                  xtype : 'textfield',
                                  allowBlank : false
                                }, {
                                  fieldLabel : UI.i18n.field.poblacion,
                                  name : 'poblacion',
                                  xtype : 'textfield',
                                  allowBlank : false
                                }, {
                                  fieldLabel : UI.i18n.field.codigoPostal,
                                  name : 'cp',
                                  xtype : 'textfield',
                                  allowBlank : false
                                }, {
                                  fieldLabel : UI.i18n.field.provincia,
                                  name : 'provincia',
                                  xtype : 'textfield',
                                  allowBlank : true
                                }, {
                                  fieldLabel : UI.i18n.field.phone,
                                  name : 'telefono',
                                  xtype : 'textfield',
                                  allowBlank : false
                                }, {
                                  fieldLabel : UI.i18n.field.email,
                                  name : 'email',
                                  allowBlank : true,
                                  xtype : 'textfield'
                                }, {
                                  xtype: 'checkbox',
                                  fieldLabel : UI.i18n.field.infoPeriodica,
                                  labelWidth : 220,
                                  checked   : true,
                                  name : 'infoPeriodica'
                                }, {
                                fieldLabel: UI.i18n.field.tipoPago,
                                id: 'tipoPago',
                                xtype: 'combobox',
                                displayField: 'name',
                                valueField : 'value',
                                queryMode: 'local',
                                value: 'metalico',
                                typeAhead: false,
                                editable: false,
                                allowBlank: false,
                                forceSelection:true,
                                store: new Ext.data.SimpleStore({
                                  fields: ['value', 'name'],
                                    data: payModes
                                })
                              }, {
                                xtype: 'label',
                                id: 'total'
                              }, {
                                xtype: 'hiddenfield',
                                name: 'hiddenTotalPrecio'
                              }, {
                                xtype: 'numberfield',
                                name: 'importePagado',
                                decimalSeparator: '.',
                                fieldLabel: UI.i18n.field.importePagado
                              }, {
                                name: 'dineroADevolver',
                                xtype: 'label',
                                text: UI.i18n.field.importeADevolver
                              }, {
                                name: 'referenciaDePago',
                                xtype: 'textfield',
                                allowBlank: false,
                                hidden: true,
                                fieldLabel: UI.i18n.field.referenciaDePago
                            }, {
                                xtype: 'button',
                                id: 'pagar',
                                scale: 'large',
                                text: UI.i18n.button.pagar
                            }, {
                                xtype: 'label',
                                name: 'estadoPagoTarjeta'
                            }
                        ]}                        
                ]
        }]
   }]
});
