var menu = (window.location.href.indexOf("#") != -1) ? window.location.href.substring(window.location.href.indexOf("#")+1) : 'Dashboard';
         
if (!window.console) console = {log: function() {}};

function seleccionaMenu(menuACambiar) {
   if (screens[menuACambiar] && document.getElementById("menu" + menuACambiar))
      document.getElementById("menu" + menuACambiar).className = "current";

   for (var key in screens) {
      if (document.getElementById("menu" + key) && key != menuACambiar)
         document.getElementById("menu" + key).className = "";
   }  
}
         
function muestraMenu(menuACambiar) {
   if (menuACambiar != menu) {
      menu = menuACambiar;
      document.getElementById("divCargador").style.display = 'block';
      seleccionaMenu(menuACambiar);
      window.location = "#" + menuACambiar;
   }
}

function cambiaIdioma(lang) {
   window.location = urlPrefix + '?lang=' + lang + window.location.hash;
}