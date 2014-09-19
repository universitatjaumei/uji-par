$(document).ready(function($) {
    $("form").submit(function( event ) {
        var email = $('input[name="email"]');
        var emailVerificacion = $('input[name="emailVerificacion"]');

        if (email && emailVerificacion && email.val() !== emailVerificacion.val())
        {
            $("#errorEmailVerificacion").show();
            event.preventDefault();
        }
    });
});