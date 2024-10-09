INSERT INTO Usuario(id, email, password, rol, activo)
VALUES (null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

/* USUARIO DE PRUEBA */
    INSERT INTO Usuario(nombreUsuario, email, password)
    VALUES ('admin', 'test@prueba.com', 'admintest')

/* AMIGOS DE PRUEBA */
INSERT INTO Usuario(nombreUsuario, email, password)
VALUES ('user1', 'user1@user.com', 'user1234'),('user2', 'user2@user.com', 'user1234');


/* INSERT DE CARTAS PARA EL TRUCO */
INSERT INTO Carta(valor, numero, palo, img)
VALUES
-- Cuatro, cinco, seis
(0, 4, 'Oro', '4_Oro'),
(0, 4, 'Copa', '4_Copa'),
(0, 4, 'Espada', '4_Espada'),
(0, 4, 'Basto', '4_Basto'),

(1, 5, 'Oro', '5_Oro'),
(1, 5, 'Copa', '5_Copa'),
(1, 5, 'Espada', '5_Espada'),
(1, 5, 'Basto', '5_Basto'),

(2, 6, 'Oro', '6_Oro'),
(2, 6, 'Copa', '6_Copa'),
(2, 6, 'Espada', '6_Espada'),
(2, 6, 'Basto', '6_Basto'),

-- Siete (Copa y Basto) = 3
(3, 7, 'Copa', '7_Copa'),
(3, 7, 'Basto', '7_Basto'),

-- Diez, Once, Doce
(4, 10, 'Oro', '10_Oro'),
(4, 10, 'Copa', '10_Copa'),
(4, 10, 'Espada', '10_Espada'),
(4, 10, 'Basto', '10_Basto'),

(5, 11, 'Oro', '11_Oro'),
(5, 11, 'Copa', '11_Copa'),
(5, 11, 'Espada', '11_Espada'),
(5, 11, 'Basto', '11_Basto'),

(6, 12, 'Oro', '12_Oro'),
(6, 12, 'Copa', '12_Copa'),
(6, 12, 'Espada', '12_Espada'),
(6, 12, 'Basto', '12_Basto'),

-- Uno (Copa y Oro) = 7
(7, 1, 'Oro', '1_Oro'),
(7, 1, 'Copa', '1_Copa'),

-- Dos, Tres
(8, 2, 'Oro', '2_Oro'),
(8, 2, 'Copa', '2_Copa'),
(8, 2, 'Espada', '2_Espada'),
(8, 2, 'Basto', '2_Basto'),

(9, 3, 'Oro', '3_Oro'),
(9, 3, 'Copa', '3_Copa'),
(9, 3, 'Espada', '3_Espada'),
(9, 3, 'Basto', '3_Basto'),

-- Siete (Oro) = 10, Siete (Espada) = 11
(10, 7, 'Oro', '7_Oro'),
(11, 7, 'Espada', '7_Espada'),

-- Uno (Basto) = 12, Uno (Espada) = 13
(12, 1, 'Basto', '1_Basto'),
(13, 1, 'Espada', '1_Espada');