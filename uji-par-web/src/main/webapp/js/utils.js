function descuentoNoDisponible(tipoEvento, precioNormal, precioDescuento)
{
	return precioDescuento == 0.0 || 
		   ((tipoEvento.toLowerCase()=='cine' || tipoEvento.toLowerCase()=='teatro') && precioNormal < 8.0);
}