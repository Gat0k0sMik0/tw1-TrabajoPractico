const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/spring/wschat'
});

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
    const chatMessagesDiv = document.getElementById('chat-messages');

    // Crear un elemento para mostrar el mensaje
    const messageElement = document.createElement('div');
    messageElement.classList.add('message'); // Puedes usar esto para aplicar estilos

    // Mostrar quién envió el mensaje y el contenido
    messageElement.innerHTML = `
        <strong>${messageData.idUsuario1 === idUsuario1 ? 'Tú' : 'Usuario ' + messageData.idUsuario2}:</strong>
        <span>${messageData.content}</span>
    `;

    // Agregar el mensaje al div de mensajes
    chatMessagesDiv.appendChild(messageElement);

    // Hacer scroll automático hacia el último mensaje
    chatMessagesDiv.scrollTop = chatMessagesDiv.scrollHeight;
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
