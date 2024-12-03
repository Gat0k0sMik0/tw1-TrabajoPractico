const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/spring/wschat'
});

function handleEnter(event) {
    if (event.key === 'Enter') {  // Verifica si se presionó Enter
        sendMessage();
        event.preventDefault(); // Evita el salto de línea en el input
    }
}


// Accede a las variables globales definidas en el HTML
const idUsuario1 = window.idUsuario1;
const idUsuario2 = window.idUsuario2;

console.log("USUARIO 1: " + idUsuario1);
console.log("USUARIO 2: " + idUsuario2);

stompClient.debug = function (str) {
    console.log(str)
};

stompClient.onConnect = (frame) => {
    console.log('Conectado: ' + frame);
    const titulo = document.getElementById('chattingWith');
    titulo.textContent = "Chateando con: " + idUsuario2;
    console.log(titulo);

    const chatChannel = `/queue/chat/${Math.min(idUsuario1, idUsuario2)}-${Math.max(idUsuario1, idUsuario2)}`;

    stompClient.subscribe(chatChannel, (m) => {
        console.log('Mensaje recibido del servidor:', m.body);
        const messageData = JSON.parse(m.body)// Aquí ves lo que el servidor envió de vuelta
        mostrarMensaje(messageData); // Parsear el mensaje


    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

stompClient.activate();

function mostrarMensaje(messageData) {
    const remitente = messageData.idUsuario1.toString() === idUsuario1.toString()
    const chatMessagesDiv = document.getElementById('chat-messages');

    // Contenedor mensaje
    const contenedorMensaje = document.createElement('div');
    // Aplico estilos
    contenedorMensaje.classList.add(
        "d-flex", "styledContenedorMensaje",
        remitente ? "align-self-end" : "align-self-start");

    // Contendor hijo
    const messageElement = document.createElement('div');
    messageElement.classList.add(
        'message',
        remitente ? "bg-success-subtle" : "bg-primary-subtle",
        remitente ? "rightMessage" : "leftMessage",
        remitente ? "pe-4" : "ps-4",
        remitente ? "ps-3" : "pe-3"); // pongo estilos

    // Mostrar quién envió el mensaje y el contenido
    messageElement.innerHTML = `
        <strong>${remitente ? 'Tú' : 'Usuario ' + messageData.idUsuario2}:</strong>
        <span>${messageData.content}</span>
    `;

    // Agrego etiquetas donde corresponde
    contenedorMensaje.append(messageElement);
    chatMessagesDiv.appendChild(contenedorMensaje);

    // Hacer scroll automático hacia el último mensaje
    chatMessagesDiv.scrollTop = chatMessagesDiv.scrollHeight;

    /*
    <div>
        <div>
            <strong></strong>
            <span></span>
        </div>
    </div>
    */
}


// Función para enviar mensajes
function sendMessage() {
    const input = document.getElementById("message");
    const content = input.value.trim();

    if (content === "") {
        alert("No puedes enviar un mensaje vacío.");
        return;
    }

    if (stompClient.connected) {
        stompClient.publish({
            destination: `/app/chat`,
            body: JSON.stringify({
                idUsuario1: idUsuario1,
                idUsuario2: idUsuario2,
                content: content
            }),
        });

        input.value = ""; // Limpiar el campo de entrada después de enviar
    } else {
        console.error("No se pudo enviar el mensaje. Cliente no conectado.");
        alert("Error al enviar el mensaje. Intenta nuevamente.");
    }
}
