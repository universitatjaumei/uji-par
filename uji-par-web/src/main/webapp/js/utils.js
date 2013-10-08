function descuentoNoDisponible(tipoEvento, precioDescuento)
{
	return precioDescuento == 0.0 || 
		   ((tipoEvento.toLowerCase()=='cine' || tipoEvento.toLowerCase()=='teatro') && precioDescuento < 8.0);
}