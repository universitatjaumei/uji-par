#!/usr/bin/env python
# -*- coding: utf-8 -*-

from docopt import docopt

USO = """
Usage:
    generaMap.py [options] <anchoImagen> <altoImagen> <anchoCelda> <altoCelda>
    generaMap.py --help

Options:
    --help               Muestra esta pantalla

    -x <xini>                                   Desplazamiento inicial X [default: 0]
    -y <yini>                                   Desplazamiento inicial Y [default: 0]
    -d --descendente                            Los número de butacas van en orden descendente
    -i <incremento> --incremento <incremento>   Los números de butaca van incrementandose en este numero [default: 1]
    <anchoImagen>                               Ancho de imagen
    <altoImagen>                                Alto de imagen
    <anchoCelda>                                Ancho de celda
    <altoCelda>                                 Alto de celda
"""

def generate_map(x_ini, y_ini, ancho_imagen, alto_imagen, ancho_celda, alto_celda, descendente, inc_butaca):

    print '<map name="map">'

    fila = (alto_imagen-y_ini) / alto_celda

    for y in range(y_ini, alto_imagen, alto_celda):

        if descendente: 
            numero = (ancho_imagen-x_ini) / ancho_celda
            inc_butaca = -inc_butaca
        else:    
            numero = 1

        for x in range(x_ini, ancho_imagen, ancho_celda):
            print '<area shape="rect" coords="%d,%d,%d,%d" href="javascript:selecciona(%d, %d, %d, %d)" />' % (x, y, x+ancho_celda, y+alto_celda, fila, numero, x, y)
            numero += inc_butaca
        
        fila -= 1

    print '</map>'

if __name__ == "__main__":
    arguments = docopt(USO)

    #print arguments

    generate_map(int(arguments["-x"]), int(arguments["-y"]), int(arguments["<anchoImagen>"]), int(arguments["<altoImagen>"]), int(arguments["<anchoCelda>"]), int(arguments["<altoCelda>"]), arguments["--descendente"], int(arguments['--incremento']))
