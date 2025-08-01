-- Tabla de usuarios
CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('administrador', 'veterinario', 'recepcionista'))
);

-- Tabla de propietarios
CREATE TABLE propietarios (
    id_propietario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    dui VARCHAR(10) UNIQUE NOT NULL,
    direccion TEXT,
    telefono VARCHAR(20),
    correo VARCHAR(100)
);

-- Tabla de mascotas
CREATE TABLE mascotas (
    id_mascota SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(50),
    edad INTEGER,
    sexo VARCHAR(10),
    color VARCHAR(30),
    peso DECIMAL(5,2),
    observaciones TEXT,
    id_propietario INT NOT NULL REFERENCES propietarios(id_propietario) ON DELETE CASCADE
);

-- Tabla de vacunas
CREATE TABLE vacunas (
    id_vacuna SERIAL PRIMARY KEY,
    tipo_vacuna VARCHAR(100) NOT NULL,
    lote VARCHAR(50),
    fecha_aplicacion DATE NOT NULL,
    proxima_dosis DATE,
    id_mascota INT NOT NULL REFERENCES mascotas(id_mascota) ON DELETE CASCADE,
    id_veterinario INT REFERENCES usuarios(id_usuario) ON DELETE SET NULL
);

-- Tabla de citas médicas
CREATE TABLE citas (
    id_cita SERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    motivo TEXT,
    estado VARCHAR(20) DEFAULT 'pendiente' CHECK (estado IN ('pendiente', 'realizada', 'cancelada')),
    notas TEXT,
    id_mascota INT NOT NULL REFERENCES mascotas(id_mascota) ON DELETE CASCADE,
    id_veterinario INT REFERENCES usuarios(id_usuario) ON DELETE SET NULL
);

-- Tabla de historial médico
CREATE TABLE historial_medico (
    id_historial SERIAL PRIMARY KEY,
    id_mascota INT NOT NULL REFERENCES mascotas(id_mascota) ON DELETE CASCADE,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    diagnostico TEXT,
    tratamiento TEXT,
    observaciones TEXT,
    id_veterinario INT REFERENCES usuarios(id_usuario) ON DELETE SET NULL
);

-- ==============================
-- DATOS DE EJEMPLO
-- ==============================

-- Usuarios
INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES
('Carlos Martínez', 'admin@sicome.com', 'admin123', 'administrador'),
('Dra. Laura Gómez', 'laura@sicome.com', 'veter123', 'veterinario'),
('Ana Pérez', 'ana@sicome.com', 'recep123', 'recepcionista');

-- Propietarios
INSERT INTO propietarios (nombre, dui, direccion, telefono, correo) VALUES
('José Ramírez', '01234567-8', 'Col. Escalón, San Salvador', '7890-1234', 'jose.ramirez@gmail.com'),
('Marta Hernández', '98765432-1', 'Santa Tecla, La Libertad', '7564-0987', 'marta_hdz@hotmail.com');

-- Mascotas
INSERT INTO mascotas (nombre, especie, raza, edad, sexo, color, peso, observaciones, id_propietario) VALUES
('Firulais', 'Perro', 'Labrador', 5, 'Macho', 'Marrón', 25.5, 'Muy activo y amigable.', 1),
('Michi', 'Gato', 'Criollo', 3, 'Hembra', 'Blanco con negro', 4.3, 'Tiene miedo a los extraños.', 2);

-- Vacunas
INSERT INTO vacunas (tipo_vacuna, lote, fecha_aplicacion, proxima_dosis, id_mascota, id_veterinario) VALUES
('Rabia', 'L001-RB', '2025-07-01', '2026-07-01', 1, 2),
('Triple Felina', 'L015-TF', '2025-06-15', '2025-09-15', 2, 2);

-- Citas médicas
INSERT INTO citas (fecha, hora, motivo, estado, notas, id_mascota, id_veterinario) VALUES
('2025-08-05', '09:00', 'Vacunación anual', 'pendiente', NULL, 1, 2),
('2025-08-10', '14:00', 'Control general', 'pendiente', NULL, 2, 2);

-- Historial médico
INSERT INTO historial_medico (id_mascota, fecha, diagnostico, tratamiento, observaciones, id_veterinario) VALUES
(1, '2025-06-20', 'Dermatitis leve', 'Champú medicado', 'Mejoró con tratamiento.', 2),
(2, '2025-05-30', 'Desparasitación interna', 'Dosis única de antiparasitario', 'Sin efectos adversos', 2);
