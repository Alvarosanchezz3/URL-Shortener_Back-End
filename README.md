# URL Shortener App 🚀

¡Bienvenido a la aplicación de acortamiento de URL! Esta aplicación te permite acortar URLs largas y gestionarlas de manera sencilla.

## Configuración del Entorno 🛠️

Antes de ejecutar la aplicación, asegúrate de configurar las variables de entorno en archivos `.env`. Crea dos archivos: `.env` para la aplicación y `.env.db` para la base de datos PostgreSQL.

**Contenido de `.env` para la aplicación:**

```env
# .env
DATABASE_URL=jdbc:postgresql://nombre_servicio:puerto/nombre_bd
DATABASE_USERNAME=usuario
DATABASE_PASSWORD=contraseña
```
```env.db
POSTGRES_USER=usuario_postgres
POSTGRES_PASSWORD=contraseña
POSTGRES_DB=nombre_bd
```

## Ejecutar la Aplicación 🚀

Asegúrate de tener Docker instalado. Luego, ejecuta el siguiente comando:

--> docker-compose up

## Endpoints 🌐

- GET /api/url/email/{email}: Obtener todas las URL asociadas a un usuario por correo electrónico.
- DELETE /api/url/delete: Eliminar una URL asociada a un usuario.
- PUT /api/url/update: Actualizar la información de una URL.
- POST /api/url/shorten: Acortar una nueva URL asociada a un usuario.
- GET /api/url/redirect/{shortUrlID}: Redirigir a la URL original a partir de un código corto.

## Contribuciones 🤝
¡Contribuciones bienvenidas! Si tienes alguna mejora, corrección de errores o nuevas características, no dudes en abrir un problema o enviar una solicitud de extracción.
