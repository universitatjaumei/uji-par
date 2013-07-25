#!/usr/bin/env python
# -*- coding: utf-8 -*-

from docopt import docopt
import json

USO = """
Usage:
    generaMap.py [options] json <localizacion> <anchoImagen> <altoImagen> <anchoCelda> <altoCelda>
    generaMap.py map <fichero_json>
    generaMap.py --help

Options:
    --help               Muestra esta pantalla

    json                                        Genera fichero json con butacas

    -x <xini>                                   Desplazamiento inicial X [default: 0]
    -y <yini>                                   Desplazamiento inicial Y [default: 0]
    -d --descendente                            Los número de butacas van en orden descendente
    -i <incremento> --incremento <incremento>   Los números de butaca van incrementandose en este numero [default: 1]
    <anchoImagen>                               Ancho de imagen
    <altoImagen>                                Alto de imagen
    <anchoCelda>                                Ancho de celda
    <altoCelda>                                 Alto de celda

    map                                         Genera map para incluir en el HTML
"""

def genera_json(localizacion, x_ini, y_ini, ancho_imagen, alto_imagen, ancho_celda, alto_celda, descendente, inc_butaca):

    butacas = []

    fila = (alto_imagen-y_ini) / alto_celda

    if descendente:
        inc_butaca = -inc_butaca

    for y in range(y_ini, alto_imagen, alto_celda):

        if descendente: 
            numero = ((ancho_imagen-x_ini) / ancho_celda) * abs(inc_butaca)
        else:    
            numero = 1

        for x in range(x_ini, ancho_imagen, ancho_celda):
            butacas.append({"localizacion":localizacion, "xIni":x, "yIni":y, "xFin":x+ancho_celda, "yFin":y+alto_celda, "fila":fila, "numero":numero})
            numero += inc_butaca
        
        fila -= 1

    return json.dumps(butacas, sort_keys=True, indent=4, separators=(',', ': '))


def genera_map(fichero):

    #st = '<map name="map">\n'

    st = ''

    for butaca in json.load(open(fichero)):
        st += '<area shape="rect" coords="%d,%d,%d,%d" href="javascript:Butacas.selecciona(\'%s\', %d, %d, %d, %d)" />\n' % (butaca['xIni'], butaca['yIni'], butaca['xFin'], butaca['yFin'], butaca['localizacion'], butaca['fila'], butaca['numero'], butaca['xIni'], butaca['yIni'])


    #st += '</map>\n'

    return st

if __name__ == "__main__":
    arguments = docopt(USO)

    #print arguments

    if arguments['json']:

        print genera_json(arguments["<localizacion>"], int(arguments["-x"]), int(arguments["-y"]), int(arguments["<anchoImagen>"]), int(arguments["<altoImagen>"]), int(arguments["<anchoCelda>"]), int(arguments["<altoCelda>"]), arguments["--descendente"], int(arguments['--incremento']))
    
    else:
        
        print genera_map(arguments['<fichero_json>'])


