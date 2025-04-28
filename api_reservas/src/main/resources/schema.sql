CREATE TABLE salon (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numeroDeSalon INT NOT NULL
);

CREATE TABLE reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    cantidadDePersonas INT NOT NULL,
    salon INT NOT NULL,
    fecha DATE NOT NULL,
    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    estado VARCHAR(50) NOT NULL,
    CONSTRAINT fk_salon FOREIGN KEY (salon) REFERENCES salon(id)
);