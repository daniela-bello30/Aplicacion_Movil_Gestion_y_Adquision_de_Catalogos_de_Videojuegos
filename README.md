# Arcadia Xone 🎮📱
### Escuela de Tecnologías de la Información | CIBERTEC
**Carrera:** Computación e Informática  
**Curso:** Desarrollo de Aplicaciones Móviles I (5to Ciclo - Período 2025)[cite: 6]  

---

## 📝 Resumen del Proyecto
**Arcadia Xone** es una aplicación móvil nativa para el sistema operativo Android que actúa como una plataforma de distribución y gestión de catálogos de videojuegos, inspirada en ecosistemas comerciales como Steam Mobile[cite: 6]. 

La aplicación permite a los entusiastas del gaming registrarse, autenticarse de manera segura, explorar un catálogo interactivo con listados de precios en tiempo real, interactuar con carritos de compras y administrar una biblioteca o almacén personal de títulos adquiridos[cite: 6, 7]. El enfoque del proyecto radica en la modularidad del código, el ciclo de vida estricto de componentes Android y la persistencia de datos distribuida[cite: 6].

---

## ⚙️ Características Principales (Product Backlog)
El desarrollo se estructuró a través de historias de usuario ágiles divididas en sprints funcionales:

*   **Autenticación en la Nube:** Registro e inicio de sesión integrados con Firebase Authentication (Email/Password) y flujos nativos de recuperación[cite: 5, 7].
*   **Catálogo Dinámico:** Navegación fluida por colecciones de juegos utilizando vistas modulares de Android Studio (Activities/Fragments) y componentes de Diseño Material[cite: 6, 7].
*   **E-Commerce Core:** Mecánica completa de agregar, visualizar totales y remover elementos de un carrito de compras digital de juegos[cite: 5, 7].
*   **Biblioteca Personal:** Gestión eインgestión lógica de compras simuladas para registrar los títulos propiedad de la cuenta del usuario[cite: 6, 7].
*   **Panel Administrativo:** Módulo interno integrado para que perfiles con accesos elevados (`ADMIN` / `Editor`) gestionen la disponibilidad de títulos, precios y URL de imágenes de portada[cite: 5, 7].

---

## 🏗️ Arquitectura y Persistencia

### Modelo de Datos Relacional y Lógico
El backend y la estructuración local de datos se rigen bajo un esquema relacional estricto, modelado para asegurar la integridad transaccional[cite: 7]. Puede consultar la topología, llaves primarias y la relación de entidades revisando la estructura completa detallada en el archivo visual adjunto `image_8e6d1b.png`.

El esquema lógico cuenta con las siguientes tablas clave[cite: 7]:
*   `tb_usuario` / `tb_administrador`: Gestión de credenciales, roles, nivel de acceso (`SuperAdmin`, `Administrador`, `Editor`) y estados de cuenta[cite: 7].
*   `tb_juego`: Almacén del catálogo (Nombre, descripción, precio, fecha de lanzamiento, URL de imagen)[cite: 7].
*   `tb_compra`: Registro transaccional unívoco que asocia la adquisición de juegos a un perfil[cite: 7].
*   `tb_resena`: Tabla relacional intermedia destinada a almacenar puntuaciones y comentarios de la comunidad[cite: 7].

### Suite de Integración Firebase 🔥
El proyecto migra la lógica distribuida hacia los servicios en la nube de Google[cite: 7]:
1.  **Firebase Authentication:** Control estricto de accesos y tokens de sesión de usuarios autenticados[cite: 7].
2.  **Cloud Firestore (NoSQL):** Base de datos documental utilizada para la sincronización inmediata del perfil del usuario, controlando variables booleanas de rol (ej: `editor: true`, `superAdmin: false`) junto con marcas de tiempo (*timestamps*)[cite: 7].

---

## 🛠️ Stack Tecnológico
*   **Lenguaje Principal:** Kotlin (100% Nativo).
*   **IDE:** Android Studio[cite: 6].
*   **Diseño de UI/UX:** Material Design Framework (XML layouts con View Binding incorporado)[cite: 6, 7].
*   **Ecosistema Cloud / BaaS:** Firebase Auth & Cloud Firestore[cite: 7].
*   **Gestor de Dependencias:** Gradle (Kotlin DSL - `build.gradle.kts`)[cite: 5].
*   **Compatibilidad Mínima:** Soportado en dispositivos Android 7.0 (API 24) en adelante[cite: 7].

---

## 📋 Requerimientos del Sistema

### Requerimientos Funcionales (RF)
*   **RF01:** El sistema debe permitir registrar nuevos clientes[cite: 7].
*   **RF02:** El cliente debe iniciar sesión para acceder a las secciones protegidas de la aplicación[cite: 7].
*   **RF03/RF05:** Registro y gestión centralizada de videojuegos para la venta[cite: 7].
*   **RF04:** Visualización dinámica de los juegos adquiridos en una biblioteca exclusiva[cite: 7].
*   **RF06:** Permitir que los usuarios escriban e inyecten reseñas en el detalle del juego[cite: 7].

### Requerimientos No Funcionales (RFN)
*   **RFN01:** Autenticación fluida y segura mediante Gmail y contraseña validada[cite: 7].
*   **RFN02:** Interfaz interactiva y moderna basada en los lineamientos estéticos de Material Design[cite: 6, 7].
*   **RFN05:** Rendimiento optimizado, garantizando la carga completa de Actividades en un tiempo máximo de 3 segundos[cite: 7].

---

## 👥 Equipo de Desarrollo (Cibertec SCRUM Team)
El proyecto se ejecutó utilizando el marco ágil SCRUM, dividiendo las responsabilidades técnicas del prototipo de la siguiente manera[cite: 7]:

*   **Daniela Angiolina Bello Gutierrez:** Gestión de Base de Datos, Suite de Firebase, Seguridad e Implementación del Login[cite: 6, 7].
*   **Aldo Eduardo Diaz Gayoso:** Codificación Limpia y Lógica de Negocio[cite: 6, 7].
*   **Brian Yamir Coila Enriquez:** Diseño de Prototipos en Figma y UI Layouts[cite: 6, 7].
*   **Jerson Tunque Quispe:** Creación de Documentación Técnica y Casos de Uso[cite: 6, 7].
*   **Jesus Adrian Calle Salvador:** Suite de Pruebas Unitarias y Revisión de Código[cite: 6, 7].
*   **Jonathan Jean Pierre Siesquen Zuloaga:** Aseguramiento de Compatibilidad y Rendimiento[cite: 6, 7].

**Docente Asesor del Curso:** Sigfrido Erwin Alva Peralta[cite: 6]
