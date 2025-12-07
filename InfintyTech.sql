CREATE DATABASE IF NOT EXISTS TallerReparaciones;
USE TallerReparaciones;

-- =========================
-- 1) Roles y usuarios internos
-- =========================
CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    usuario VARCHAR(40) NOT NULL UNIQUE,
    correo VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,   -- hash o texto según tu app
    id_rol INT NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- Roles base
INSERT INTO roles(nombre) VALUES ('ADMIN'), ('TECNICO'), ('RECEPCION');


-- =========================
-- 2) Clientes
-- =========================
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(60) NOT NULL,
    apellidos VARCHAR(80) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    direccion VARCHAR(200),
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    activo TINYINT(1) NOT NULL DEFAULT 1
);

CREATE INDEX idx_cliente_cedula ON clientes(cedula);


-- =========================
-- 2.1) Solicitudes web (NUEVO)
-- =========================
CREATE TABLE solicitudes_servicio (
    id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NULL,
    nombre VARCHAR(80) NOT NULL,
    correo VARCHAR(120) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    tipo_servicio ENUM('MANTENIMIENTO','REPARACION','REVISION') NOT NULL,
    tipo_equipo VARCHAR(50) NOT NULL,      -- laptop, celular, tv, etc
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(60),
    descripcion TEXT NOT NULL,
    estado ENUM('RECIBIDA','EN_REVISION','CONVERTIDA_A_TICKET','CANCELADA') NOT NULL DEFAULT 'RECIBIDA',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

CREATE INDEX idx_solicitud_estado ON solicitudes_servicio(estado);
CREATE INDEX idx_solicitud_fecha ON solicitudes_servicio(fecha_creacion);
CREATE INDEX idx_solicitud_correo ON solicitudes_servicio(correo);


-- =========================
-- 3) Equipos ingresados
-- =========================
CREATE TABLE equipos (
    id_equipo INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    tipo_equipo VARCHAR(50) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(60),
    serie VARCHAR(80),
    accesorios TEXT,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

CREATE INDEX idx_equipo_cliente ON equipos(id_cliente);
CREATE INDEX idx_equipo_serie ON equipos(serie);
CREATE INDEX idx_equipo_marca_modelo ON equipos(marca, modelo);


-- =========================
-- 4) Estados de Ticket
-- =========================
CREATE TABLE estados_ticket (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO estados_ticket(nombre) VALUES
('RECIBIDO'),
('DIAGNOSTICO'),
('PRESUPUESTO'),
('REPARACION'),
('LISTO_PARA_RETIRO'),
('ENTREGADO'),
('CANCELADO'),
('CANCELADO_POR_PRESUPUESTO');


-- =========================
-- 5) Tickets de reparación
-- =========================
CREATE TABLE tickets (
    id_ticket INT AUTO_INCREMENT PRIMARY KEY,
    folio VARCHAR(30) NOT NULL UNIQUE,
    id_cliente INT NOT NULL,
    id_equipo INT NOT NULL,
    id_estado INT NOT NULL,
    descripcion_falla TEXT NOT NULL,
    accesorios_recibidos TEXT,
    firma_entrega VARCHAR(120),
    id_tecnico_asignado INT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_ultimo_estado DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo_cancelacion VARCHAR(200),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo),
    FOREIGN KEY (id_estado) REFERENCES estados_ticket(id_estado),
    FOREIGN KEY (id_tecnico_asignado) REFERENCES usuarios(id_usuario)
);

CREATE INDEX idx_ticket_cliente ON tickets(id_cliente);
CREATE INDEX idx_ticket_estado ON tickets(id_estado);
CREATE INDEX idx_ticket_tecnico ON tickets(id_tecnico_asignado);
CREATE INDEX idx_ticket_fecha ON tickets(fecha_creacion);


-- =========================
-- 6) Bitácora / auditoría
-- =========================
CREATE TABLE bitacora (
    id_bitacora INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_usuario INT NOT NULL,
    accion VARCHAR(60) NOT NULL,
    detalle TEXT,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE INDEX idx_bitacora_ticket ON bitacora(id_ticket);
CREATE INDEX idx_bitacora_usuario ON bitacora(id_usuario);


-- =========================
-- 7) Diagnóstico técnico
-- =========================
CREATE TABLE diagnosticos (
    id_diagnostico INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL UNIQUE,
    id_tecnico INT NOT NULL,
    hallazgos TEXT NOT NULL,
    pruebas_realizadas TEXT,
    fotos_url TEXT,
    horas_estimadas DECIMAL(6,2) DEFAULT 0,
    fecha_diagnostico DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_tecnico) REFERENCES usuarios(id_usuario)
);

-- Items/trabajos sugeridos por diagnóstico (CAMBIADO)
CREATE TABLE items_trabajo (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    id_diagnostico INT NOT NULL,
    descripcion VARCHAR(180) NOT NULL,
    horas_estimadas DECIMAL(6,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_diagnostico) REFERENCES diagnosticos(id_diagnostico)
);

CREATE INDEX idx_items_diag ON items_trabajo(id_diagnostico);


-- =========================
-- 8) Repuestos e inventario
-- =========================
CREATE TABLE repuestos (
    id_repuesto INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(40) NOT NULL UNIQUE,
    descripcion VARCHAR(160) NOT NULL,
    costo_unitario DECIMAL(10,2) NOT NULL,
    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 0,
    activo TINYINT(1) NOT NULL DEFAULT 1
);

CREATE INDEX idx_repuesto_codigo ON repuestos(codigo);

-- Movimientos por ticket
CREATE TABLE ticket_repuestos (
    id_tr_ticket_rep INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_repuesto INT NOT NULL,
    cantidad INT NOT NULL,
    estado ENUM('RESERVADO','CONSUMIDO','DEVUELTO') NOT NULL DEFAULT 'RESERVADO',
    fecha_mov DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_repuesto) REFERENCES repuestos(id_repuesto)
);

CREATE INDEX idx_ticket_rep_ticket ON ticket_repuestos(id_ticket);
CREATE INDEX idx_ticket_rep_rep ON ticket_repuestos(id_repuesto);

-- Evita duplicados del mismo repuesto para el mismo ticket y estado
ALTER TABLE ticket_repuestos
ADD UNIQUE KEY uq_ticket_repuesto_estado (id_ticket, id_repuesto, estado);


-- =========================
-- 9) Presupuesto y aprobación
-- =========================
CREATE TABLE presupuestos (
    id_presupuesto INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL UNIQUE,
    mano_obra_horas DECIMAL(6,2) NOT NULL DEFAULT 0,
    tarifa_hora DECIMAL(10,2) NOT NULL DEFAULT 0,
    subtotal_repuestos DECIMAL(10,2) NOT NULL DEFAULT 0,
    subtotal_mano_obra DECIMAL(10,2) NOT NULL DEFAULT 0,
    impuestos DECIMAL(10,2) NOT NULL DEFAULT 0,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado ENUM('GENERADO','ENVIADO','APROBADO','RECHAZADO') NOT NULL DEFAULT 'GENERADO',
    fecha_generacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_respuesta DATETIME NULL,
    notas VARCHAR(200),
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket)
);

CREATE INDEX idx_presupuesto_estado ON presupuestos(estado);

CREATE TABLE presupuesto_detalle (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_presupuesto INT NOT NULL,
    concepto ENUM('REPUESTO','MANO_OBRA','OTRO') NOT NULL,
    descripcion VARCHAR(180) NOT NULL,
    id_repuesto INT NULL,
    cantidad INT DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL DEFAULT 0,
    total_linea DECIMAL(10,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_presupuesto) REFERENCES presupuestos(id_presupuesto),
    FOREIGN KEY (id_repuesto) REFERENCES repuestos(id_repuesto)
);

CREATE INDEX idx_presupuesto_detalle_pres ON presupuesto_detalle(id_presupuesto);
CREATE INDEX idx_presupuesto_detalle_resp ON presupuesto_detalle(id_repuesto);


-- =========================
-- 10) Notificaciones
-- =========================
CREATE TABLE notificaciones (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_cliente INT NOT NULL,
    canal ENUM('EMAIL','SMS') NOT NULL,
    destino VARCHAR(120) NOT NULL,
    mensaje VARCHAR(250) NOT NULL,
    estado ENUM('ENVIADA','FALLIDA','REINTENTO') NOT NULL DEFAULT 'ENVIADA',
    fecha_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

CREATE INDEX idx_notif_estado ON notificaciones(estado);


-- =========================
-- 11) Entrega, factura y pagos
-- =========================
CREATE TABLE entregas (
    id_entrega INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL UNIQUE,
    id_usuario_recepcion INT NOT NULL,
    fecha_entrega DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    monto_cobrado DECIMAL(10,2) NOT NULL DEFAULT 0,
    firma_cliente VARCHAR(120),
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_usuario_recepcion) REFERENCES usuarios(id_usuario)
);

CREATE TABLE facturas (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL UNIQUE,
    id_cliente INT NOT NULL,
    fecha_factura DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,
    impuestos DECIMAL(10,2) NOT NULL DEFAULT 0,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    pdf_url VARCHAR(200),
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

CREATE INDEX idx_facturas_cliente ON facturas(id_cliente);

CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_factura INT NOT NULL,
    metodo ENUM('EFECTIVO','TRANSFERENCIA','SINPE') NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    referencia VARCHAR(80),
    fecha_pago DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('APLICADO','FALLIDO') NOT NULL DEFAULT 'APLICADO',
    FOREIGN KEY (id_factura) REFERENCES facturas(id_factura)
);

CREATE INDEX idx_pagos_factura ON pagos(id_factura);


-- =========================
-- 12) Garantías
-- =========================
CREATE TABLE garantias (
    id_garantia INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket_original INT NOT NULL,
    id_ticket_garantia INT NOT NULL UNIQUE,
    dias_garantia INT NOT NULL DEFAULT 30,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado ENUM('VIGENTE','EXPIRADA') NOT NULL DEFAULT 'VIGENTE',
    FOREIGN KEY (id_ticket_original) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_ticket_garantia) REFERENCES tickets(id_ticket)
);


-- =========================
-- 13) Control de tiempos
-- =========================
CREATE TABLE tiempos_actividad (
    id_tiempo INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_tecnico INT NOT NULL,
    tipo ENUM('DIAGNOSTICO','REPARACION') NOT NULL,
    inicio DATETIME NOT NULL,
    fin DATETIME NULL,
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_tecnico) REFERENCES usuarios(id_usuario)
);

CREATE INDEX idx_tiempos_ticket ON tiempos_actividad(id_ticket);


-- =========================
-- 14) Encuesta satisfacción
-- =========================
CREATE TABLE encuestas (
    id_encuesta INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL UNIQUE,
    id_cliente INT NOT NULL,
    estrellas TINYINT NOT NULL,
    comentarios VARCHAR(250),
    fecha_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_respuesta DATETIME NULL,
    estado ENUM('ENVIADA','RESPONDIDA','CADUCA') NOT NULL DEFAULT 'ENVIADA',
    CHECK (estrellas BETWEEN 1 AND 5),
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);


-- =========================
-- 15) Seguimiento post-reparación
-- =========================
CREATE TABLE seguimientos_post (
    id_seguimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL UNIQUE,
    fecha_programada DATE NOT NULL,
    fecha_envio DATETIME NULL,
    respuesta ENUM('FUNCIONA_BIEN','PROBLEMAS','SIN_RESPUESTA') DEFAULT 'SIN_RESPUESTA',
    comentarios VARCHAR(200),
    FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket)
);