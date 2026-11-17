USE if0_43338979_entrevista_db;

SET FOREIGN_KEY_CHECKS = 0;

-- Quitar las FK que apuntan a municipios(municipio)
ALTER TABLE candidatos DROP FOREIGN KEY candidatos_ibfk_1;
ALTER TABLE domicilios_candidatos DROP FOREIGN KEY domicilios_candidatos_ibfk_1;
ALTER TABLE empresas DROP FOREIGN KEY empresas_ibfk_1;

-- Nueva tabla municipios con id autoincremental
DROP TABLE IF EXISTS municipios;
CREATE TABLE municipios (
  id_municipio INT AUTO_INCREMENT PRIMARY KEY,
  nombre       VARCHAR(50) NOT NULL,
  departamento VARCHAR(50) NOT NULL,
  pais         VARCHAR(50) NOT NULL,
  UNIQUE KEY uq_municipio (nombre, departamento, pais)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Las columnas que guardaban el nombre pasan a guardar el id
ALTER TABLE candidatos
  CHANGE municipio_nacimiento id_municipio_nacimiento INT NOT NULL,
  ADD CONSTRAINT fk_candidato_municipio FOREIGN KEY (id_municipio_nacimiento) REFERENCES municipios(id_municipio);

ALTER TABLE domicilios_candidatos
  CHANGE municipio id_municipio INT NOT NULL,
  ADD CONSTRAINT fk_domicilio_municipio FOREIGN KEY (id_municipio) REFERENCES municipios(id_municipio);

ALTER TABLE empresas
  CHANGE municipio id_municipio INT NOT NULL,
  ADD CONSTRAINT fk_empresa_municipio FOREIGN KEY (id_municipio) REFERENCES municipios(id_municipio);

SET FOREIGN_KEY_CHECKS = 1;
