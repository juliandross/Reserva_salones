
-- Datos para la tabla salon
INSERT INTO salon (id, numeroDeSalon) VALUES (1, 101);
INSERT INTO salon (id, numeroDeSalon) VALUES (2, 102);
INSERT INTO salon (id, numeroDeSalon) VALUES (3, 103);

-- Datos para la tabla reserva
INSERT INTO reserva (nombres, apellidos, cantidadDePersonas, salon, fecha, horaInicio, horaFin, estado)
VALUES ('Juan', 'Pérez', 5, 1, '2025-05-27', '10:00:00', '12:00:00', 'PENDIENTE');

INSERT INTO reserva (nombres, apellidos, cantidadDePersonas, salon, fecha, horaInicio, horaFin, estado)
VALUES ('María', 'Gómez', 3, 2, '2025-05-28', '14:00:00', '16:00:00', 'ACEPTADA');

INSERT INTO reserva (nombres, apellidos, cantidadDePersonas, salon, fecha, horaInicio, horaFin, estado)
VALUES ('Carlos', 'López', 10, 3, '2025-05-29', '09:00:00', '11:00:00', 'RECHAZADA');
