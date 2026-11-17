# Proyecto Epsilon: reglas para agentes

## Proyecto
- Aplicación Java de escritorio (Swing) para registro de hojas de vida de candidatos.
- Java 21, NetBeans/Ant. Código en `src/`, tests JUnit 4 en `test/`.
- Base de datos MySQL. Scripts en `db_procedures/`.
- Librería: `lib/mysql-connector-j-9.5.0.jar`. JUnit 4.13.2 y Hamcrest 1.3 están en
  `C:/Program Files/NetBeans-22/netbeans/platform/modules/ext`.
- Código, comentarios y mensajes en español.

## Credenciales
- Nunca escribas contraseñas en el código ni en archivos versionados.
- La conexión se lee de variables de entorno o de `.env` (ignorado por git): `DB_URL`, `DB_USER`, `DB_PASSWORD`.
- No abras, copies ni muestres el contenido de `.env`.

## Antes de empezar cualquier tarea
- Haz todas las preguntas que necesites para hacerla bien: alcance, archivos, criterios de
  aceptación, permiso para tocar la base. Si trabajas como sub agente, devuelve las preguntas
  como lista en tu primera respuesta, antes de cambiar nada. No asumas.
- Si la tarea toca más de un archivo, presenta un plan y espera aprobación.

## Antes de implementar
- Muestra el código propuesto (o el diff) y espera confirmación. No escribas archivos sin aprobación.
- Respeta el estilo del proyecto: nombres en español, 4 espacios de indentación, sin librerías nuevas sin preguntar.

## Base de datos
- Producción local: `if0_43338979_entrevista_db`. **Las pruebas usan `entrevista_db_test`**, nunca la anterior.
  Para probar, define `DB_URL=jdbc:mysql://localhost:3306/entrevista_db_test?useSSL=false&serverTimezone=UTC`.
- Antes de escribir un `INSERT`, `UPDATE` o procedimiento, revisa las columnas reales con `SHOW CREATE TABLE`.
- No ejecutes `DROP`, `TRUNCATE` ni `DELETE` masivo sin confirmación.
- Los municipios se registran con `sp_registrar_municipio` (`db_procedures/`).
- Los cambios de esquema van como scripts numerados en `db_procedures/`.

## Pruebas y reporte
- Después de cada cambio en `src/` o `test/`, compila y corre los tests antes de reportar.
- Cada tarea deja un reporte en `docs/pruebas/AAAA-MM-DD_tarea.md` (carpeta ignorada por git) con:
  qué se probó, comando o pasos, resultado esperado, resultado real (EXITOSA/FALLÓ) y los errores completos si falló.
- Reporta resultados tal cual: si algo falla, muéstralo. Nunca digas que algo pasó si no lo verificaste.

## Git
- **Antes de cada commit, muestra en el chat el reporte de pruebas y el diff.** Como `docs/pruebas/`
  no se versiona, el reporte solo vive en el chat. Haz commit solo si todas las pruebas salieron exitosas
  y el usuario lo confirmó. Si alguna falló, no hagas commit.
- No hagas push sin que el usuario lo pida.
- No toques `main` directamente. Trabaja en la rama activa (ahora `db_procedures`).
- No reescribas historial (`reset --hard`, `rebase`, `push --force`) sin confirmación.
- `build/`, `dist/`, `.env` y `docs/pruebas/` no se versionan.
- Mensajes de commit en español.
