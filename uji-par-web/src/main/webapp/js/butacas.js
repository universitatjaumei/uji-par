Butacas = (function() {
	
	var baseUrl = "";
	var sesionId;
	var precios = {};
	
	var butacasSeleccionadas = [];
	
	function init(url, sesId) {
		baseUrl = url;
		sesionId = sesId;
	}
	
	function cargaPrecios(callback) {
		$.getJSON(baseUrl + '/rest/entrada/' + sesionId + '/precios', function(respuesta){
			
			for (var i=0; i<respuesta.data.length; i++)
			{
				var sesion = respuesta.data[i];
				precios[sesion.localizacion.codigo] = {normal:sesion.precio, descuento:sesion.descuento, invitacion:sesion.invitacion};
			}
			
			callback(precios);
		});
	}
	
	
	function ocultaButacasSeleccionadas() {
		$('.butaca-seleccionada').remove();
	}
	
	function iguales(butaca1, butaca2) {
		return butaca1.localizacion == butaca2.localizacion
				&& butaca1.fila == butaca2.fila
				&& butaca1.numero == butaca2.numero;
	}
	
	function estaSeleccionada(butaca) {
		for ( var i = 0; i < butacasSeleccionadas.length; i++)
			if (iguales(butaca, butacasSeleccionadas[i]))
				return true;
		
		return false;
	}
	
	function imagenButaca(butaca)
	{
		if (esDiscapacitado(butaca.localizacion))
			return "seleccionadaDiscapacitado.png";
		else
			return "seleccionada.png";
	}
	
	function muestraButacaSeleccionada(butaca) {
		var butacaSeleccionada = $('<img src="' + baseUrl + '/img/' + imagenButaca(butaca) + '" class="butaca-seleccionada"/>');
		butacaSeleccionada.css("left", butaca.x + "px");
		butacaSeleccionada.css("top", butaca.y + "px");
		
		console.log(butacaSeleccionada);
		
		var idDiv = idDivLocalizacion(butaca.localizacion);
		
		$('#localizacion_' + idDiv).append(butacaSeleccionada);
	
		butacaSeleccionada.click(function() {
			// console.log("Click sobre seleccionada: ", butaca);
			selecciona(butaca.localizacion, butaca.fila, butaca.numero,
					butaca.x, butaca.y);
		});
		butacaSeleccionada.show();
	}
	
	function muestraButacasSeleccionadas() {
		for ( var i = 0; i < butacasSeleccionadas.length; i++) {
			muestraButacaSeleccionada(butacasSeleccionadas[i]);
		}
	}
	
	function redibujaButacasSeleccionadas() {
		ocultaButacasSeleccionadas();
		muestraButacasSeleccionadas();
	}
	
	function anyadeButacaSeleccionada(butaca) {
		butacasSeleccionadas.push(butaca);
	}
	
	function eliminaButacaSeleccionada(butaca) {
		for ( var i = 0; i < butacasSeleccionadas.length; i++) {
			if (iguales(butaca, butacasSeleccionadas[i])) {
				butacasSeleccionadas.splice(i, 1);
				return;
			}
		}
	}
	
	function refrescaImagen(localizacion)
	{
		var imagen = $("#imagen_" + idDivLocalizacion(localizacion));
		
		var url = imagen.attr("src").replace(/\?.*/, "");
		
		imagen.attr("src", url + "?" + (new Date()).getTime());
	}
	
	function muestraDetallesSeleccionadas() {
		var detalles = "";
	
		$('#detallesSeleccionadas').empty();
	
		for ( var i = 0; i < butacasSeleccionadas.length; i++) {
			var fila = $('<div class="entrada-seleccionada">'
					+ butacasSeleccionadas[i].localizacion
					+ ', <span>' + UI.i18n.butacas.fila + '</span>'
					+ '<b>' + butacasSeleccionadas[i].fila + '</b>'
					+ ', <span>' + UI.i18n.butacas.butaca + '</span>'
					+ '<b>' + butacasSeleccionadas[i].numero + '</b><br>'
					+ getSelectTipoButaca(i)
					+ '<span>(<b>' + butacasSeleccionadas[i].precio.toFixed(2) + '</b> €)</span></div>');
			$('#detallesSeleccionadas').append(fila);
		}
	}
	
	function getSelectTipoButaca(posicion)
	{
		var st = '<select onchange="Butacas.cambiaTipoButaca(' + posicion + ', this.value)">';
		
		var selecNormal = 'selected',
			selecDescuento = '';
		
		if (butacasSeleccionadas[posicion]['tipo'] == 'descuento')
		{
			selecNormal = '';
			selecDescuento = 'selected';
		}
		
		st += '<option ' + selecNormal + ' value="normal">' + UI.i18n.butacas.tipoNormal + '</option><option ' + selecDescuento + ' value="descuento">' + UI.i18n.butacas.tipoDescuento + '</option></select>';
		
		return st;
	}
	
	function actualizaTotal()
	{
		var total = 0;
		
		for (var i=0; i<butacasSeleccionadas.length; i++)
		{
			total += butacasSeleccionadas[i].precio;
		}
		
		$('#totalSeleccionadas').text(total.toFixed(2));
	}
	
	function cambiaTipoButaca(posicion, tipo)
	{
		var butaca = butacasSeleccionadas[posicion];
		
		butaca.tipo = tipo;
		butaca.precio = precios[butaca.localizacion][tipo];
		
		refrescaEstadoButacas();
	}
	
	function selecciona(localizacion, fila, numero, x, y) {

		var tipoEntrada = 'normal';
		
		var butaca = {
			localizacion : localizacion,
			fila : fila,
			numero : numero,
			x : x,
			y : y,
			tipo : tipoEntrada,
			precio: precios[localizacion][tipoEntrada]
		};
	
		if (estaSeleccionada(butaca)) {
			// console.log('Ya está seleccionada:', butaca);
			eliminaButacaSeleccionada(butaca);
		} else {
			// console.log('Añade a seleccionadas:', butaca);
			anyadeButacaSeleccionada(butaca);
			compruebaEstadoButaca(butaca);
		}
	
		refrescaEstadoButacas();
	}
	
	function esDiscapacitado(localizacion)
	{
		return localizacion.indexOf('discapacitados') == 0;
	}
	
	function idDivLocalizacion(localizacion)
	{
		if (localizacion == 'discapacitados1')
			return 'platea1';
		else if (localizacion == 'discapacitados2')
			return 'platea2';
		else if (localizacion == 'discapacitados3')
			return 'anfiteatro';
		else 
			return localizacion;
	}
	
	function refrescaEstadoButacas()
	{
		muestraBotonLimpiarSeleccion();
		redibujaButacasSeleccionadas();
		muestraDetallesSeleccionadas();
		guardaButacasEnHidden();
		actualizaTotal();
	}
	
	function muestraBotonLimpiarSeleccion()
	{
		var boton = $('#limpiarSeleccion');
		
		if (butacasSeleccionadas.length == 0)
			boton.hide();
		else
			boton.show();
	}
	
	function limpiaSeleccion()
	{
		butacasSeleccionadas = [];
		refrescaEstadoButacas();
	}
	
	function guardaButacasEnHidden()
	{
		$('input[name=butacasSeleccionadas]').val($.toJSON(butacasSeleccionadas));
	}
	
	function ocupadasSuccess(ocupadas) {
		
		// console.log("ocupadas:", ocupadas);
		if (ocupadas.length > 0) {
			
			for (var i=0; i<ocupadas.length; i++)
			{
				eliminaButacaSeleccionada(ocupadas[i]);
				refrescaImagen(ocupadas[i].localizacion);
			}
			
			refrescaEstadoButacas();
			
			/* [[#{butacasOcupadas}]] */
			var msj = UI.i18n.butacas.ocupadas; 
			alert(msj);
		}
	}
	
	function compruebaEstadoButaca(butaca) {
		
		var path = baseUrl + "/rest/entrada/" + $("input[name=idSesion]").val() + "/ocupadas";
		
		$.ajax({
			  url: path,
			  type:"POST",
			  data: $.toJSON(butacasSeleccionadas),
			  contentType: "application/json; charset=utf-8",
			  dataType: "json",
			  success: ocupadasSuccess});
	}
	
	function muestraPrecios() {
		var value = $('input[name=tipo]:checked').val();
	
		// console.log('Precio:', value);
	
		if (value == 'normal') {
			$('.precio-normal').show();
			$('.precio-descuento').hide();
		} else {
			$('.precio-descuento').show();
			$('.precio-normal').hide();
		}
	}
	
	function muestraLocalizacion() {
		var localizacion = $('#localizacion').val();
		
		$('div[id^=localizacion_]').hide();
		
		if (localizacion == 'platea')
		{
			$('#localizacion_platea1').show();
			$('#localizacion_platea2').show();
		}
		else
		{
			$('#localizacion_anfiteatro').show();
		}
	}
	
	$(document).ready(function() {
		$('input[name=tipo]').click(function() {
			muestraPrecios();
		});
		
		$('#localizacion').change(function() {
			muestraLocalizacion();
		});
		
		$('#limpiarSeleccion').click(function(){
			limpiaSeleccion();
		});

		muestraPrecios();
		muestraLocalizacion();
	});
	
	// Desde fuera del iframe nos han pedido que le pasemos las butacas seleccionadas 
	pm.bind("butacas", function(data){
		 pm({
			   target: parent,
			   type:"respuestaButacas", 
			   data:butacasSeleccionadas
		 });
	});
	
	return {
		selecciona:selecciona,
		init:init,
		cargaPrecios:cargaPrecios,
		cambiaTipoButaca: cambiaTipoButaca
	};
	
}());