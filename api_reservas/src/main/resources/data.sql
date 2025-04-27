-- Datos para la tabla salon
INSERT INTO salon (id, numeroDeSalon) VALUES (1, 101);
INSERT INTO salon (id, numeroDeSalon) VALUES (2, 102);
INSERT INTO salon (id, numeroDeSalon) VALUES (3, 103);

-- Datos para la tabla reserva
INSERT INTO reserva (id, nombres, apellidos, cantidadDePersonas, salon, fecha, horaInicio, horaFin, estado)
VALUES (1, 'Juan', 'Pérez', 5, 1, '2025-04-27', '10:00:00', '12:00:00', 'PENDIENTE');

INSERT INTO reserva (id, nombres, apellidos, cantidadDePersonas, salon, fecha, horaInicio, horaFin, estado)
VALUES (2, 'María', 'Gómez', 3, 2, '2025-04-28', '14:00:00', '16:00:00', 'ACEPTADA');

INSERT INTO reserva (id, nombres, apellidos, cantidadDePersonas, salon, fecha, horaInicio, horaFin, estado)
VALUES (3, 'Carlos', 'López', 10, 3, '2025-04-29', '09:00:00', '11:00:00', 'RECHAZADA');