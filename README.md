# Proyecto Hoja de Vida - Fase 1: Estructuración Arquitectónica del Dominio (Patrón Modelo-Vista)

## 📌 Descripción del Proyecto
Este proyecto implementa el sistema de gestión del **Formato Único de Hoja de Vida de Persona Natural** (Gobierno de Colombia) bajo el paradigma de **Programación Orientada a Objetos (POO)** en Java. Siguiendo las directrices académicas y los lineamientos de la asignatura, el sistema se estructura estrictamente bajo el **Patrón Modelo-Vista** utilizando almacenamiento en memoria con colecciones dinámicas (`ArrayList`) y persistencia en archivos de objetos (`Serializable`).

---

## ⚠️ ADVERTENCIA DE SEGURIDAD Y RESTRICCIÓN DEL 100% (CRÍTICA)

> **RESTRICCIÓN ABSOLUTA:**
> **Queda estrictamente prohibido modificar, editar o alterar manualmente una sola coma o línea del código autogenerado del diseñador visual de NetBeans (`jPanel`, `initComponents()`, `@SuppressWarnings("unchecked")`, etc.).**
>
> 🛑 **Riesgo:** El código de la interfaz gráfica dentro del bloque `// GEN-FIRST:event_...` o `initComponents()` es autogenerado por el entorno NetBeans. Cualquier cambio manual directo en esas secciones resultará en **incompatibilidad irreversible del archivo `.form`**, corrupción del diseño y fallas fatales en el compilador.
>
> 📌 **Consecuencia para la Fase 1:** En esta **Fase 1 NO se construirá ni modificará la interfaz gráfica (GUI)**. Esta fase se limita exclusivamente al diseño conceptual, declaración de paquetes, inventario completo de firmas de métodos y atributos de las clases de entidad en el paquete `Modelo` y la estructura cliente `USACandidato` en el paquete `Vista`.

---

## 📁 Estructura de Paquetes
El proyecto se dividirá exclusivamente en **dos paquetes principales**:

```text
src/
├── Modelo/       # Clases de dominio, enumeraciones y reglas de negocio
└── Vista/        # Clase cliente ejecutable y ventana principal (USACandidato)
```

---

## 📑 Inventario Completo de Clases, Atributos y Métodos (Fase 1)

Below is the complete architectural inventory of names for attributes and methods across all classes.

### 1. Paquete `Modelo`

#### 1.1 Enumeraciones (Dominios Restringidos)
* **`TipoDocumento`**: `{ CC, CE, PAS }`
* **`Sexo`**: `{ F, M }`
* **`Nacionalidad`**: `{ COLOMBIA, EXTRANJERO }`
* **`ClaseLibreta`**: `{ PRIMERA_CLASE, SEGUNDA_CLASE, NINGUNA }`
* **`ModalidadAcademica`**: `{ TC, TL, TE, UN, ES, MG, DOC }`
* **`NivelCompetencia`**: `{ R, B, MB }`
* **`TipoEmpresa`**: `{ PUBLICA, PRIVADA }`

---

#### 1.2 Clase `Domicilio`
* **Atributos:**
  * `direccion : String`
  * `pais : String`
  * `departamento : String`
  * `municipio : String`
* **Métodos (Firmas):**
  * `Domicilio()`
  * `Domicilio(String direccion, String pais, String departamento, String municipio)`
  * `getDireccion() : String`
  * `setDireccion(String direccion) : void`
  * `getPais() : String`
  * `setPais(String pais) : void`
  * `getDepartamento() : String`
  * `setDepartamento(String departamento) : void`
  * `getMunicipio() : String`
  * `setMunicipio(String municipio) : void`
  * `toString() : String`

---

#### 1.3 Clase `LibretaMilitar`
* **Atributos:**
  * `numero : String`
  * `clase : ClaseLibreta`
  * `distritoMilitar : int`
* **Métodos (Firmas):**
  * `LibretaMilitar()`
  * `LibretaMilitar(String numero, ClaseLibreta clase, int distritoMilitar)`
  * `getNumero() : String`
  * `setNumero(String numero) : void`
  * `getClase() : ClaseLibreta`
  * `setClase(ClaseLibreta clase) : void`
  * `getDistritoMilitar() : int`
  * `setDistritoMilitar(int distritoMilitar) : void`
  * `toString() : String`

---

#### 1.4 Clase `Idioma`
* **Atributos:**
  * `nombreIdioma : String`
  * `habla : NivelCompetencia`
  * `lectura : NivelCompetencia`
  * `escritura : NivelCompetencia`
* **Métodos (Firmas):**
  * `Idioma()`
  * `Idioma(String nombreIdioma, NivelCompetencia habla, NivelCompetencia lectura, NivelCompetencia escritura)`
  * `getNombreIdioma() : String`
  * `setNombreIdioma(String nombreIdioma) : void`
  * `getHabla() : NivelCompetencia`
  * `setHabla(NivelCompetencia habla) : void`
  * `getLectura() : NivelCompetencia`
  * `setLectura(NivelCompetencia lectura) : void`
  * `getEscritura() : NivelCompetencia`
  * `setEscritura(NivelCompetencia escritura) : void`
  * `toString() : String`

---

#### 1.5 Clase `Empresa`
* **Atributos:**
  * `nombre : String`
  * `tipo : TipoEmpresa`
  * `pais : String`
  * `departamento : String`
  * `municipio : String`
  * `email : String`
  * `telefono : String`
  * `direccion : String`
* **Métodos (Firmas):**
  * `Empresa()`
  * `Empresa(String nombre, TipoEmpresa tipo, String pais, String departamento, String municipio, String email, String telefono, String direccion)`
  * `getNombre() : String`
  * `setNombre(String nombre) : void`
  * `getTipo() : TipoEmpresa`
  * `setTipo(TipoEmpresa tipo) : void`
  * `getPais() : String`
  * `setPais(String pais) : void`
  * `getDepartamento() : String`
  * `setDepartamento(String departamento) : void`
  * `getMunicipio() : String`
  * `setMunicipio(String municipio) : void`
  * `getEmail() : String`
  * `setEmail(String email) : void`
  * `getTelefono() : String`
  * `setTelefono(String telefono) : void`
  * `getDireccion() : String`
  * `setDireccion(String direccion) : void`
  * `toString() : String`

---

#### 1.6 Clase `ExperienciaLaboral`
* **Atributos:**
  * `empresa : Empresa`
  * `cargo : String`
  * `dependencia : String`
  * `fechaIngreso : LocalDate`
  * `fechaRetiro : LocalDate`
* **Métodos (Firmas):**
  * `ExperienciaLaboral()`
  * `ExperienciaLaboral(Empresa empresa, String cargo, String dependencia, LocalDate fechaIngreso, LocalDate fechaRetiro)`
  * `getEmpresa() : Empresa`
  * `setEmpresa(Empresa empresa) : void`
  * `getCargo() : String`
  * `setCargo(String cargo) : void`
  * `getDependencia() : String`
  * `setDependencia(String dependencia) : void`
  * `getFechaIngreso() : LocalDate`
  * `setFechaIngreso(LocalDate fechaIngreso) : void`
  * `getFechaRetiro() : LocalDate`
  * `setFechaRetiro(LocalDate fechaRetiro) : void`
  * `calcularTiempoServicio() : Period`
  * `toString() : String`

---

#### 1.7 Superclase Abstracta `Educacion`
* **Atributos (Protegidos):**
  * `tituloObtenido : String`
  * `mesTerminacion : Integer`
  * `anioTerminacion : Integer`
* **Métodos (Firmas):**
  * `Educacion()`
  * `Educacion(String tituloObtenido, Integer mesTerminacion, Integer anioTerminacion)`
  * `getTituloObtenido() : String`
  * `setTituloObtenido(String tituloObtenido) : void`
  * `getMesTerminacion() : Integer`
  * `setMesTerminacion(Integer mesTerminacion) : void`
  * `getAnioTerminacion() : Integer`
  * `setAnioTerminacion(Integer anioTerminacion) : void`
  * `toString() : String`

---

#### 1.8 Subclase `EducacionBasica` (extends `Educacion`)
* **Atributos:**
  * `ultimoGradoAprobado : int`
* **Métodos (Firmas):**
  * `EducacionBasica()`
  * `EducacionBasica(String tituloObtenido, Integer mesTerminacion, Integer anioTerminacion, int ultimoGradoAprobado)`
  * `getUltimoGradoAprobado() : int`
  * `setUltimoGradoAprobado(int ultimoGradoAprobado) : void`
  * `toString() : String`

---

#### 1.9 Subclase `EducacionSuperior` (extends `Educacion`)
* **Atributos:**
  * `modalidadAcademica : ModalidadAcademica`
  * `semestresAprobados : int`
  * `graduado : boolean`
  * `numeroTarjetaProfesional : String`
* **Métodos (Firmas):**
  * `EducacionSuperior()`
  * `EducacionSuperior(String tituloObtenido, Integer mesTerminacion, Integer anioTerminacion, ModalidadAcademica modalidadAcademica, int semestresAprobados, boolean graduado, String numeroTarjetaProfesional)`
  * `getModalidadAcademica() : ModalidadAcademica`
  * `setModalidadAcademica(ModalidadAcademica modalidadAcademica) : void`
  * `getSemestresAprobados() : int`
  * `setSemestresAprobados(int semestresAprobados) : void`
  * `isGraduado() : boolean`
  * `setGraduado(boolean graduado) : void`
  * `getNumeroTarjetaProfesional() : String`
  * `setNumeroTarjetaProfesional(String numeroTarjetaProfesional) : void`
  * `toString() : String`

---

#### 1.10 Clase Raíz `Candidato` (implements `Serializable`)
* **Atributos:**
  * `numeroIdentificacion : String`
  * `tipoIdentificacion : TipoDocumento`
  * `nombre : String`
  * `primerApellido : String`
  * `segundoApellido : String`
  * `sexo : Sexo`
  * `nacionalidad : Nacionalidad`
  * `fechaNacimiento : LocalDate`
  * `paisNacimiento : String`
  * `deptoNacimiento : String`
  * `municipioNacimiento : String`
  * `email : String`
  * `telefono : String`
  * `manifiestaJuramento : boolean`
  * `observacionesRRHH : String`
  * `domicilio : Domicilio`
  * `libretaMilitar : LibretaMilitar`
  * `educaciones : List<Educacion>`
  * `experiencias : List<ExperienciaLaboral>`
  * `idiomas : List<Idioma>`
* **Métodos (Firmas):**
  * `Candidato()`
  * `Candidato(String numeroIdentificacion, TipoDocumento tipoIdentificacion, String nombre, String primerApellido, String segundoApellido, Sexo sexo, Nacionalidad nacionalidad, LocalDate fechaNacimiento, String email, String telefono)`
  * `getNumeroIdentificacion() : String`
  * `setNumeroIdentificacion(String numeroIdentificacion) : void`
  * `getTipoIdentificacion() : TipoDocumento`
  * `setTipoIdentificacion(TipoDocumento tipoIdentificacion) : void`
  * `getNombre() : String`
  * `setNombre(String nombre) : void`
  * `getPrimerApellido() : String`
  * `setPrimerApellido(String primerApellido) : void`
  * `getSegundoApellido() : String`
  * `setSegundoApellido(String segundoApellido) : void`
  * `getSexo() : Sexo`
  * `setSexo(Sexo sexo) : void`
  * `getNacionalidad() : Nacionalidad`
  * `setNacionalidad(Nacionalidad nacionalidad) : void`
  * `getFechaNacimiento() : LocalDate`
  * `setFechaNacimiento(LocalDate fechaNacimiento) : void`
  * `getEmail() : String`
  * `setEmail(String email) : void`
  * `getTelefono() : String`
  * `setTelefono(String telefono) : void`
  * `getDomicilio() : Domicilio`
  * `setDomicilio(Domicilio domicilio) : void`
  * `getLibretaMilitar() : LibretaMilitar`
  * `setLibretaMilitar(LibretaMilitar libretaMilitar) : void`
  * `getEducaciones() : List<Educacion>`
  * `setEducaciones(List<Educacion> educaciones) : void`
  * `getExperiencias() : List<ExperienciaLaboral>`
  * `setExperiencias(List<ExperienciaLaboral> experiencias) : void`
  * `getIdiomas() : List<Idioma>`
  * `setIdiomas(List<Idioma> idiomas) : void`
  * `calcularEdad() : int`
  * `calcularTiempoTotalExperiencia() : String`
  * `desplegarInformacionCompleta() : String`
  * `toString() : String`

---

### 2. Paquete `Vista`

#### 2.1 Clase Cliente `USACandidato` (extends `JFrame`)
* **Atributos:**
  * `losCandidatos : ArrayList<Candidato>`
* **Métodos (Firmas):**
  * `main(String[] args) : void`
  * `USACandidato()`
  * `agregarCandidato : void`
  * `guardarArchivoCandidato : void`
  * `cargarArchivoCandidato : void`
  * `limpiarCampos : void`
  * `salir : void`
  * `mostrarTodos(ArrayList<Candidato> datos) : String`

---

## 🚀 Plan de Trabajo por Fases

1. **Fase 1 (Actual):**
   * Creación de este `README.md`.
   * Declaración de clases vacías / stubs de firma de métodos en NetBeans.
   * Revisión de concordancia con la rúbrica y restricciones del `jPanel`.
2. **Fase 2:**
   * Codificación interna de los constructores, getters, setters y métodos de negocio (`calcularEdad()`, `calcularTiempoTotalExperiencia()`).
   * Implementación de la persistencia de datos mediante `ObjectOutputStream` y `ObjectInputStream`.
3. **Fase 3:**
   * Ensamblaje visual en NetBeans Swing para `USACandidato` en modo `Design` sin tocar el código autogenerado.
   * Conexión de los eventos de botones (`agregarCandidatoActionPerformed`, `guardarArchivoActionPerformed`) con los métodos del Modelo.
