USE hoja_vida;

DROP PROCEDURE IF EXISTS sp_registrar_municipio;
DELIMITER $$
CREATE PROCEDURE sp_registrar_municipio(
  IN  p_nombre       VARCHAR(50),
  IN  p_departamento VARCHAR(50),
  IN  p_pais         VARCHAR(50),
  OUT p_id           INT
)
BEGIN
  -- Si ya existe (la collation ci ignora mayúsculas), devuelve su id sin duplicar
  INSERT INTO municipios (nombre, departamento, pais)
  VALUES (TRIM(p_nombre), TRIM(p_departamento), TRIM(p_pais))
  ON DUPLICATE KEY UPDATE id_municipio = LAST_INSERT_ID(id_municipio);

  SET p_id = LAST_INSERT_ID();
END$$
DELIMITER ;
