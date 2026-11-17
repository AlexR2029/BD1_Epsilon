CREATE DATABASE IF NOT EXISTS hoja_vida CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hoja_vida;

CREATE TABLE IF NOT EXISTS TIPOS_DOCUMENTO (
    codigo VARCHAR(10) PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS MUNICIPIOS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    UNIQUE KEY uq_municipio (nombre, departamento, pais)
);

CREATE TABLE IF NOT EXISTS CANDIDATOS (
    numero_identificacion BIGINT PRIMARY KEY,
    tipo_identificacion VARCHAR(10) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    primer_apellido VARCHAR(100) NOT NULL,
    segundo_apellido VARCHAR(100),
    sexo VARCHAR(2) NOT NULL,
    nacionalidad VARCHAR(20) NOT NULL,
    telefono VARCHAR(30),
    email VARCHAR(150),
    manifiesta_juramento BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_nacimiento DATE NOT NULL,
    pais_nacimiento VARCHAR(100),
    departamento_nacimiento VARCHAR(100),
    municipio_nacimiento VARCHAR(100),
    observaciones_rrhh TEXT,
    CONSTRAINT fk_candidato_documento FOREIGN KEY (tipo_identificacion)
        REFERENCES TIPOS_DOCUMENTO(codigo)
);

CREATE TABLE IF NOT EXISTS DOMICILIOS_CANDIDATOS (
    numero_identificacion_candidato BIGINT PRIMARY KEY,
    direccion VARCHAR(200) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    municipio VARCHAR(100) NOT NULL,
    FOREIGN KEY (numero_identificacion_candidato) REFERENCES CANDIDATOS(numero_identificacion)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS LIBRETAS_MILITARES (
    numero BIGINT PRIMARY KEY,
    clase VARCHAR(20) NOT NULL,
    distrito_militar VARCHAR(30) NOT NULL,
    numero_identificacion_candidato BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (numero_identificacion_candidato) REFERENCES CANDIDATOS(numero_identificacion)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS EMPRESAS (
    nombre VARCHAR(150) PRIMARY KEY,
    tipo_empresa VARCHAR(20) NOT NULL,
    pais VARCHAR(100),
    departamento VARCHAR(100),
    municipio VARCHAR(100),
    email_empresa VARCHAR(150),
    telefono VARCHAR(30),
    direccion VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS EMPLEOS_CANDIDATOS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion_candidato BIGINT NOT NULL,
    nombre_empresa VARCHAR(150) NOT NULL,
    cargo VARCHAR(150) NOT NULL,
    dependencia VARCHAR(150),
    fecha_ingreso DATE NOT NULL,
    fecha_retiro DATE,
    FOREIGN KEY (numero_identificacion_candidato) REFERENCES CANDIDATOS(numero_identificacion)
        ON DELETE CASCADE,
    FOREIGN KEY (nombre_empresa) REFERENCES EMPRESAS(nombre)
);

CREATE TABLE IF NOT EXISTS EDUCACION_BASICA_CANDIDATOS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion_candidato BIGINT NOT NULL,
    ultimo_grado_aprobado TINYINT NOT NULL,
    titulo_obtenido VARCHAR(150),
    mes_grado TINYINT,
    anio_grado SMALLINT,
    FOREIGN KEY (numero_identificacion_candidato) REFERENCES CANDIDATOS(numero_identificacion)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS EDUCACION_SUPERIOR_CANDIDATOS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion_candidato BIGINT NOT NULL,
    titulo_obtenido VARCHAR(150) NOT NULL,
    numero_tarjeta_profesional VARCHAR(50),
    modalidad_academica VARCHAR(10) NOT NULL,
    semestres_aprobados SMALLINT NOT NULL,
    graduado BOOLEAN NOT NULL,
    mes_terminacion TINYINT,
    anio_terminacion SMALLINT,
    FOREIGN KEY (numero_identificacion_candidato) REFERENCES CANDIDATOS(numero_identificacion)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS IDIOMAS_CANDIDATOS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion_candidato BIGINT NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    habla VARCHAR(3) NOT NULL,
    lectura VARCHAR(3) NOT NULL,
    escritura VARCHAR(3) NOT NULL,
    FOREIGN KEY (numero_identificacion_candidato) REFERENCES CANDIDATOS(numero_identificacion)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS EXPERIENCIAS_OCUPACIONES_CANDIDATOS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion_candidato BIGINT NOT NULL,
    ocupacion VARCHAR(150) NOT NULL,
    anio_experiencia SMALLINT NOT NULL,
    meses_experiencia TINYINT NOT NULL,
    FOREIGN KEY (numero_identificacion_candidato) REFERENCES CANDIDATOS(numero_identificacion)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS VERIFICADORES (
    identificacion BIGINT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    telefono VARCHAR(30),
    cargo VARCHAR(80) NOT NULL DEFAULT 'Jefe recursos humanos'
);

CREATE TABLE IF NOT EXISTS VERIFICACIONES_HOJA_VIDA (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion_candidato BIGINT NOT NULL,
    identificacion_verificador BIGINT NOT NULL,
    fecha_verificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    aprobada BOOLEAN NOT NULL,
    observaciones TEXT,
    FOREIGN KEY (numero_identificacion_candidato) REFERENCES CANDIDATOS(numero_identificacion),
    FOREIGN KEY (identificacion_verificador) REFERENCES VERIFICADORES(identificacion)
);

DELIMITER //
DROP TRIGGER IF EXISTS trg_fecha_empleo//
CREATE TRIGGER trg_fecha_empleo
BEFORE INSERT ON EMPLEOS_CANDIDATOS
FOR EACH ROW
BEGIN
    IF NEW.fecha_retiro IS NOT NULL AND NEW.fecha_retiro < NEW.fecha_ingreso THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La fecha de retiro no puede ser anterior al ingreso';
    END IF;
END//

DROP PROCEDURE IF EXISTS sp_hojas_vida_resumidas//
CREATE PROCEDURE sp_hojas_vida_resumidas()
BEGIN
    SELECT numero_identificacion, tipo_identificacion, nombre, primer_apellido,
           segundo_apellido, sexo, nacionalidad, email, telefono
    FROM CANDIDATOS ORDER BY primer_apellido, nombre;
END//

DROP PROCEDURE IF EXISTS sp_hoja_vida_detallada//
CREATE PROCEDURE sp_hoja_vida_detallada(IN p_identificacion BIGINT)
BEGIN
    SELECT c.*, d.direccion, d.pais AS pais_domicilio, d.departamento AS departamento_domicilio,
           d.municipio AS municipio_domicilio
    FROM CANDIDATOS c
    LEFT JOIN DOMICILIOS_CANDIDATOS d
      ON d.numero_identificacion_candidato = c.numero_identificacion
    WHERE c.numero_identificacion = p_identificacion;
END//

DROP PROCEDURE IF EXISTS sp_registrar_municipio//
CREATE PROCEDURE sp_registrar_municipio(
    IN p_nombre VARCHAR(100),
    IN p_departamento VARCHAR(100),
    IN p_pais VARCHAR(100),
    OUT p_id INT
)
BEGIN
    INSERT INTO MUNICIPIOS (nombre, departamento, pais)
    VALUES (TRIM(p_nombre), TRIM(p_departamento), TRIM(p_pais))
    ON DUPLICATE KEY UPDATE id = LAST_INSERT_ID(id);
    SET p_id = LAST_INSERT_ID();
END//

DROP PROCEDURE IF EXISTS sp_hoja_vida_formacion//
CREATE PROCEDURE sp_hoja_vida_formacion(IN p_identificacion BIGINT)
BEGIN
    SELECT c.numero_identificacion, c.nombre, c.primer_apellido, c.segundo_apellido,
           eb.ultimo_grado_aprobado, eb.titulo_obtenido AS titulo_basico,
           eb.mes_grado, eb.anio_grado, es.titulo_obtenido AS titulo_superior,
           es.modalidad_academica, es.semestres_aprobados, es.graduado,
           es.mes_terminacion, es.anio_terminacion
    FROM CANDIDATOS c
    LEFT JOIN EDUCACION_BASICA_CANDIDATOS eb
      ON eb.numero_identificacion_candidato = c.numero_identificacion
    LEFT JOIN EDUCACION_SUPERIOR_CANDIDATOS es
      ON es.numero_identificacion_candidato = c.numero_identificacion
    WHERE c.numero_identificacion = p_identificacion;
END//

DROP PROCEDURE IF EXISTS sp_hoja_vida_experiencia//
CREATE PROCEDURE sp_hoja_vida_experiencia(IN p_identificacion BIGINT)
BEGIN
    SELECT c.numero_identificacion, c.nombre, c.primer_apellido,
           c.segundo_apellido, e.nombre_empresa, e.tipo_empresa,
           ec.cargo, ec.dependencia, ec.fecha_ingreso, ec.fecha_retiro
    FROM CANDIDATOS c
    LEFT JOIN EMPLEOS_CANDIDATOS ec
      ON ec.numero_identificacion_candidato = c.numero_identificacion
    LEFT JOIN EMPRESAS e ON e.nombre = ec.nombre_empresa
    WHERE c.numero_identificacion = p_identificacion;
END//
DELIMITER ;

INSERT IGNORE INTO TIPOS_DOCUMENTO (codigo, nombre) VALUES
('CC', 'Cédula de ciudadanía'), ('CE', 'Cédula de extranjería'), ('PAS', 'Pasaporte');
