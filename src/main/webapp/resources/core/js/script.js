let stompCliente = null;

const onConnectSocket = () => {
    stompCliente.subscribe('/tema/mensajes', (mensaje) => {
        mostrarMensaje(mensaje.body);
    })
}

const onWebSocketClose =  () => {
    if (stompCliente != null) {
        stompCliente.deactivate();
    }
}

const conectarWebSocket = () => {
    onWebSocketClose(); // si no conecta chau, le sacamos la conexión
    stompCliente = new StompJs.Client({
        webSocketFactory: () => new WebSocket('ws://localhost:8080/websocket')})
    // navegadores nuevos
    // navegadores viejos -> stockJS

    // al conectar, nos subscribimos al broker
    stompCliente.onConnect = onConnectSocket;
    stompCliente.onWebSocketClose = onWebSocketClose;
    stompCliente.activate();
}

// Enviar mensaje para que se transmita en el  canal
// Los que se subscribieron al canal, lo reciben
const enviarMensaje = () => {
    let txtMensaje = document.getElementById('txtMensaje')
    let txtNombre = document.getElementById('txtNombre');

    stompCliente.publish({
        destination: '/app/envio',
        body: JSON.stringify({
            nombre: txtNombre,
            contendo: txtMensaje
        }),
    })
}

const mostrarMensaje =  (mensaje) => {
    const body = JSON.parse(mensaje);
    const ULmensajes = document.getElementById('ulmensajes');
    const mensajeLI = document.createElement('li');
    mensajeLI.classList.add('list-group-item');
    mensajeLI.innerHTML = `<strong>${body.nombre}</strong>: ${body.contendo}`;
    ULmensajes.appendChild(mensajeLI);
}

document.addEventListener('DOMContentLoaded', () => {
    const btnEnviarMensaje = document.querySelector('#btnEnviarMensaje');
    btnEnviarMensaje.addEventListener('click', (e) => {
        e.preventDefault();
        enviarMensaje();
    });
    conectarWebSocket();
})

// --------------------------------

// function setConnected(connected) {
//     $("#connect").prop("disabled", connected);
//     $("#disconnect").prop("disabled", !connected);
//     $("#conversation").toggle(connected);
//     $("#greetings").html(""); // Limpiar el área de mensajes
// }
//
// function showGreeting(message) {
//     $("#greetings").append("<tr><td>" + message + "</td></tr>");
// }
//
// // Conectar al WebSocket
// function connect() {
//     // Usamos SockJS para manejar el WebSocket fallback.
//     const socket = new SockJS('/spring/chat'); // Asegúrate de que este endpoint esté bien definido en tu configuración de Spring.
//     stompClient = Stomp.over(socket);
//
//     stompClient.connect({}, function (frame) {
//         setConnected(true);
//         console.log('Connected: ' + frame);
//
//         // Suscripción al canal del chat
//         stompClient.subscribe('/topic/canal1', function (greeting) {
//             showGreeting(JSON.parse(greeting.body).content);
//         });
//     }, function (error) {
//         console.error('Error de conexión: ', error); // Manejo de errores de conexión
//         alert('No se pudo conectar al WebSocket. Verifica la consola para más detalles.');
//     });
// }
//
// // Desconectar del WebSocket
// function disconnect() {
//     if (stompClient !== null) {
//         stompClient.disconnect();
//     }
//     setConnected(false);
//     console.log("Disconnected");
// }
//
// // Enviar un mensaje al WebSocket
// function sendName() {
//     const messageContent = $("#message").val();
//     stompClient.publish({
//         destination: "/app/chat", // Asegúrate de que este endpoint esté bien definido en tu controlador
//         body: JSON.stringify({'content': messageContent})
//     });
//     $("#message").val(""); // Limpiar el campo de entrada
// }
//
// // Esperar que el DOM esté completamente cargado
// $(document).ready(function () {
//     $("#connect").click(function () {
//         connect();
//     });
//     $("#disconnect").click(function () {
//         disconnect();
//     });
//     $("#send").click(function () {
//         sendName();
//     });
// });
