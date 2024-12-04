/* USUARIOS */
INSERT INTO Usuario(nombreUsuario, email, password, rol, activo, id, urlFotoPerfil, victorias)
VALUES
('test', 'test@unlam.edu.ar', 'test1234', 'ADMIN', true, 1, 'default.png', 0),
('admin', 'test@prueba.com', 'admin', 'ADMIN', true, 2, 'default.png', 0),
('user1', 'user1@user.com', 'user0000', 'USER', true, 3, 'default.png', 0),
('user2', 'user2@user.com', 'user0000', 'USER', true, 4, 'default.png', 0),
('user3', 'user3@user.com', 'user0000', 'USER', true, 5, 'default.png', 0),
('user4', 'user4@user.com', 'user0000', 'USER', true, 6, 'default.png', 0),
('user5', 'user5@user.com', 'user0000', 'USER', true, 7, 'default.png', 0);

/* FOTO DE PERFIL DEFAULT */
INSERT INTO FotoPerfil(id, imagen)
VALUES
(1, 'default.png');

/* INSERT DE CARTAS PARA EL TRUCO */
INSERT INTO Carta(valor, numero, palo, img)
VALUES
-- Cuatro, cinco, seis
(0, 4, 'Oro', '/img/cartas-truco/oro/4_Oro.png'),
(0, 4, 'Copa', '/img/cartas-truco/copa/4_Copa.png'),
(0, 4, 'Espada', '/img/cartas-truco/espada/4_Espada.png'),
(0, 4, 'Basto', '/img/cartas-truco/basto/4_Basto.png'),

(1, 5, 'Oro', '/img/cartas-truco/oro/5_Oro.png'),
(1, 5, 'Copa', '/img/cartas-truco/copa/5_Copa.png'),
(1, 5, 'Espada', '/img/cartas-truco/espada/5_Espada.png'),
(1, 5, 'Basto', '/img/cartas-truco/basto/5_Basto.png'),

(2, 6, 'Oro', '/img/cartas-truco/oro/6_Oro.png'),
(2, 6, 'Copa', '/img/cartas-truco/copa/6_Copa.png'),
(2, 6, 'Espada', '/img/cartas-truco/espada/6_Espada.png'),
(2, 6, 'Basto', '/img/cartas-truco/basto/6_Basto.png'),

-- Siete (Copa y Basto) = 3
(3, 7, 'Copa', '/img/cartas-truco/copa/7_Copa.png'),
(3, 7, 'Basto', '/img/cartas-truco/basto/7_Basto.png'),

-- Diez, Once, Doce
(4, 10, 'Oro', '/img/cartas-truco/oro/10_Oro.png'),
(4, 10, 'Copa', '/img/cartas-truco/copa/10_Copa.png'),
(4, 10, 'Espada', '/img/cartas-truco/espada/10_Espada.png'),
(4, 10, 'Basto', '/img/cartas-truco/basto/10_Basto.png'),

(5, 11, 'Oro', '/img/cartas-truco/oro/11_Oro.png'),
(5, 11, 'Copa', '/img/cartas-truco/copa/11_Copa.png'),
(5, 11, 'Espada', '/img/cartas-truco/espada/11_Espada.png'),
(5, 11, 'Basto', '/img/cartas-truco/basto/11_Basto.png'),

(6, 12, 'Oro', '/img/cartas-truco/oro/12_Oro.png'),
(6, 12, 'Copa', '/img/cartas-truco/copa/12_Copa.png'),
(6, 12, 'Espada', '/img/cartas-truco/espada/12_Espada.png'),
(6, 12, 'Basto', '/img/cartas-truco/basto/12_Basto.png'),

-- Uno (Copa y Oro) = 7
(7, 1, 'Oro', '/img/cartas-truco/oro/1_Oro.png'),
(7, 1, 'Copa', '/img/cartas-truco/copa/1_Copa.png'),

-- Dos, Tres
(8, 2, 'Oro', '/img/cartas-truco/oro/2_Oro.png'),
(8, 2, 'Copa', '/img/cartas-truco/copa/2_Copa.png'),
(8, 2, 'Espada', '/img/cartas-truco/espada/2_Espada.png'),
(8, 2, 'Basto', '/img/cartas-truco/basto/2_Basto.png'),

(9, 3, 'Oro', '/img/cartas-truco/oro/3_Oro.png'),
(9, 3, 'Copa', '/img/cartas-truco/copa/3_Copa.png'),
(9, 3, 'Espada', '/img/cartas-truco/espada/3_Espada.png'),
(9, 3, 'Basto', '/img/cartas-truco/basto/3_Basto.png'),

-- Siete (Oro) = 10, Siete (Espada) = 11
(10, 7, 'Oro', '/img/cartas-truco/oro/7_Oro.png'),
(11, 7, 'Espada', '/img/cartas-truco/espada/7_Espada.png'),

-- Uno (Basto) = 12, Uno (Espada) = 13
(12, 1, 'Basto', '/img/cartas-truco/basto/1_Basto.png'),
(13, 1, 'Espada', '/img/cartas-truco/espada/1_Espada.png');