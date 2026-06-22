-- Script de creación de Base de Datos para EcoRide - UPN 2026
CREATE DATABASE IF NOT EXISTS ecoride_db;
USE ecoride_db;

-- Tabla de Usuarios (Soporte Spring Security / Roles)
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    rol ENUM('ADMIN', 'OPERADOR', 'CLIENTE') NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de Bicicletas (Gestión de Flota e Inventario)
CREATE TABLE IF NOT EXISTS bicicletas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio_hora DECIMAL(10,2) NOT NULL,
    estado ENUM('DISPONIBLE', 'EN_USO', 'MANTENIMIENTO') NOT NULL DEFAULT 'DISPONIBLE',
    url_imagen VARCHAR(255),
    fecha_adquisicion DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de Reservas y Alquileres (Lógica transaccional)
CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    bicicleta_id BIGINT NOT NULL,
    fecha_inicio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP NULL,
    monto_total DECIMAL(10,2) NULL,
    estado_reserva ENUM('ACTIVA', 'COMPLETADA', 'CANCELADA') NOT NULL DEFAULT 'ACTIVA',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (bicicleta_id) REFERENCES bicicletas(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inserciones iniciales para pruebas locales semilla (Seed Data)
INSERT INTO usuarios (username, password, nombre, correo, rol) VALUES
('jhon.caqui', '$2a$10$e0myVEW49gZ...hash_ejemplo', 'Jhon Daniel Caqui', 'jhon.caqui@upn.pe', 'ADMIN'),
('luis.marroquin', '$2a$10$e0myVEW49gZ...hash_ejemplo', 'Luis Erick Marroquin', 'luis.marroquin@upn.pe', 'CLIENTE');

INSERT INTO bicicletas (nombre, descripcion, precio_hora, estado, url_imagen) VALUES
('Eco City Express', 'Perfecta para trayectos rápidos diarios con canasta integrada.', 4.50, 'DISPONIBLE', 'https://images.unsplash.com/...'),
('Eco E-Bike Pro', 'Asistencia eléctrica inteligente para pendientes complejas.', 8.00, 'DISPONIBLE', 'https://images.unsplash.com/...'),
('Eco Mountain Heavy', 'Estructura reforzada para terrenos irregulares.', 6.00, 'EN_USO', 'https://images.unsplash.com/...');
