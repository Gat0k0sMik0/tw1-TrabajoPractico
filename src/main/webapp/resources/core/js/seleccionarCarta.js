
    function seleccionarCarta(element) {
    // Obtener el ID de la carta desde el atributo "data-id"
    var idCarta = element.getAttribute("data-id");

    // Asignar el ID de la carta al input oculto
    document.getElementById("idCartaSeleccionada").value = idCarta;

    // Resaltar la carta seleccionada
    var cartas = document.getElementsByClassName("carta");
    for (var i = 0; i < cartas.length; i++) {
    cartas[i].style.border = '';  // quitar borde de las demás
}
    element.style.border = '2px solid lightblue';  // borde celeste para la seleccionada
}
