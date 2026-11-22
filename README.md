# 🏥 Meditourism — Backend para Plataforma de Turismo Médico

**Meditourism** es una API REST desarrollada en Java con Spring Boot, pensada como backend para una plataforma de turismo médico enfocada inicialmente en odontología. El sistema permite la gestión de clínicas, tratamientos, reseñas y usuarios autenticados mediante JWT y roles diferenciados.

---

## 🚀 Funcionalidades principales

### 🔐 Autenticación y seguridad
- Registro de usuarios.
- Verificación de correo electrónico antes de permitir el login.
- Login con JWT (token seguro).
- Envío de correos para recuperación de contraseña con token seguro.
- Contraseñas encriptadas con **BCrypt**.

### 👤 Roles y permisos
- **USER**
  - Puede registrarse, iniciar sesión y consultar datos públicos (GET).
  - Luego de confirmar correo, puede:
    - Crear reseñas sobre tratamientos o clínicas.
    - Comentar reseñas.
    - Responder a otros comentarios (estructura jerárquica).
    - Borrar **solo** sus propios comentarios y reseñas.
  - Puede solicitar cambio de contraseña por correo.

- **ADMIN**
  - Puede agregar y gestionar clínicas y tratamientos.
  - Puede eliminar cualquier reseña o comentario.
  - Puede **bloquear usuarios** para que no puedan reseñar ni comentar.
  - Tiene acceso completo a todas las funcionalidades.

---

## ⚙️ Tecnologías utilizadas

| Componente       | Versión      |
|------------------|--------------|
| Java             | 17           |
| Spring Boot      | 3.4.4        |
| Spring Security  | Integrado    |
| Spring Data JPA  | Integrado    |
| PostgreSQL       | Cloud (Railway) |
| JWT (jjwt)       | 0.11.5       |
| BCrypt           | Integrado    |
| Spring Mail      | Integrado    |
| Swagger UI       | 2.7.0        |
| Maven            | build tool   |

---

## Diagrama de clases y casos de uso

El proyecto incluye:
- Diagrama de clases UML
- Diagrama entidad relación
- Casos de uso documentados

> Si deseas visualizar los diagramas, consulta la carpeta `/documentation` del repositorio (si aplica).

---

## Ejecución local

### 🔧 Requisitos
- Java 17
- Docker

### ▶️ Pasos
```bash
git clone https://github.com/David26044/meditourism.git
cd meditourism
cp .env.example .env        # y completa tus valores
docker compose up -d postgres
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

O todo dockerizado (API + base de datos):
```bash
docker compose up -d --build
```

Documentación de los endpoints: http://localhost:8080/system/api/swagger-ui/index.html

### 👤 Usuario administrador

Al arrancar, la app crea un admin con los valores de `ADMIN_EMAIL` y `ADMIN_PASSWORD` de tu `.env` (ver `.env.example`). Si ya existe, no hace nada.
