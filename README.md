# Arcadia Xone 🎮📱
### Escuela de Tecnologías de la Información | CIBERTEC
**Carrera:** Computación e Informática  
**Curso:** Desarrollo de Aplicaciones Móviles I (5to Ciclo - Período 2025)

---

## 📝 Resumen del Proyecto
**Arcadia Xone** es una aplicación móvil nativa para el sistema operativo Android que actúa como una plataforma de distribución y gestión de catálogos de videojuegos, inspirada en ecosistemas comerciales como Steam Mobile. 

La aplicación permite a los entusiastas del gaming registrarse, autenticarse de manera segura, explorar un catálogo interactivo con listados de precios en tiempo real, interactuar con carritos de compras y administrar una biblioteca o almacén personal de títulos adquiridos. El enfoque del proyecto radica en la modularidad del código, el ciclo de vida estricto de componentes Android y la persistencia de datos distribuida.

---

## ⚙️ Características Principales (Product Backlog)
El desarrollo se estructuró a través de historias de usuario ágiles divididas en sprints funcionales:

*   **Autenticación en la Nube:** Registro e inicio de sesión integrados con Firebase Authentication (Email/Password) y flujos nativos de recuperación.
*   **Catálogo Dinámico:** Navegación fluida por colecciones de juegos utilizando vistas modulares de Android Studio (Activities/Fragments) y componentes de Diseño Material.
*   **E-Commerce Core:** Mecánica completa de agregar, visualizar totales y remover elementos de un carrito de compras digital de juegos.
*   **Biblioteca Personal:** Gestión e ingesta lógica de compras simuladas para registrar los títulos propiedad de la cuenta del usuario.
*   **Panel Administrativo:** Módulo interno integrado para que perfiles con accesos elevados (`ADMIN` / `Editor`) gestionen la disponibilidad de títulos, precios y URL de imágenes de portada.

---

## 🏗️ Arquitectura y Persistencia

### Modelo de Datos Relacional y Lógico
El backend y la estructuración local de datos se rigen bajo un esquema relacional estricto, modelado para asegurar la integridad transaccional a nivel de persistencia local y en la nube. El esquema lógico cuenta con las siguientes tablas clave:
*   `tb_usuario` / `tb_administrador`: Gestión de credenciales, roles, nivel de acceso (`SuperAdmin`, `Administrador`, `Editor`) y estados de cuenta.
*   `tb_juego`: Almacén del catálogo (Nombre, descripción, precio, fecha de lanzamiento, URL de imagen).
*   `tb_compra`: Registro transaccional unívoco que asocia la adquisición de juegos a un perfil.
*   `tb_resena`: Tabla relacional intermedia destinada a almacenar puntuaciones y comentarios de la comunidad.

### Suite de Integración Firebase 🔥
El proyecto migra la lógica distribuida hacia los servicios en la nube de Google:
1.  **Firebase Authentication:** Control estricto de accesos y tokens de sesión de usuarios autenticados.
2.  **Cloud Firestore (NoSQL):** Base de datos documental utilizada para la sincronización inmediata del perfil del usuario, controlando variables booleanas de rol (ej: `editor: true`, `superAdmin: false`) junto con marcas de tiempo (*timestamps*).

---
## 🛠️ Stack Tecnológico
*   **Lenguaje Principal:** Kotlin (100% Nativo).
*   **IDE:** Android Studio.
*   **Diseño de UI/UX:** Material Design Framework (XML layouts con View Binding incorporado).
*   **Ecosistema Cloud / BaaS:** Firebase Auth & Cloud Firestore.
*   **Gestor de Dependencias:** Gradle (Kotlin DSL - `build.gradle.kts`).
*   **Compatibilidad Mínima:** Soportado en dispositivos Android 7.0 (API 24) en adelante.

---
## 📋 Requerimientos del Sistema

### Requerimientos Funcionales (RF)
*   **RF01:** El sistema debe permitir registrar nuevos clientes.
*   **RF02:** El cliente debe iniciar sesión para acceder a las secciones protegidas de la aplicación.
*   **RF03/RF05:** Registro y gestión centralizada de videojuegos para la venta.
*   **RF04:** Visualización dinámica de los juegos adquiridos en una biblioteca exclusiva.
*   **RF06:** Permitir que los usuarios escriban e inyecten reseñas en el detalle del juego.

### Requerimientos No Funcionales (RFN)
*   **RFN01:** Autenticación fluida y segura mediante Gmail y contraseña validada.
*   **RFN02:** Interfaz interactiva y moderna basada en los lineamientos estéticos de Material Design.
*   **RFN05:** Rendimiento optimizado, garantizando la carga completa de Actividades en un tiempo máximo de 3 segundos.

---
## 👥 Equipo de Desarrollo (Cibertec SCRUM Team)
El proyecto se ejecutó utilizando el marco ágil SCRUM, dividiendo las responsabilidades técnicas del prototipo de la siguiente manera:

*   **Daniela Angiolina Bello Gutierrez:** Gestión de Base de Datos, Suite de Firebase, Seguridad e Implementación del Login.
*   **Aldo Eduardo Diaz Gayoso:** Codificación Limpia y Lógica de Negocio.
*   **Brian Yamir Coila Enriquez:** Diseño de Prototipos en Figma y UI Layouts.
*   **Jerson Tunque Quispe:** Creación de Documentación Técnica y Casos de Uso.
*   **Jesus Adrian Calle Salvador:** Suite de Pruebas Unitarias y Revisión de Código.
*   **Jonathan Jean Pierre Siesquen Zuloaga:** Aseguramiento de Compatibilidad y Rendimiento.

**Docente Asesor del Curso:** Sigfrido Erwin Alva Peralta
