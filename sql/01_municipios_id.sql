USE hoja_vida;

-- La estructura principal ya usa MUNICIPIOS.id como clave artificial.
-- Este script es idempotente y no altera columnas ni elimina claves foráneas.
CREATE TABLE IF NOT EXISTS MUNICIPIOS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    UNIQUE KEY uq_municipio (nombre, departamento, pais)
);
