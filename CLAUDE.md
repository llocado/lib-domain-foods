# lib-domain-foods

Dominio **privado** de `productos-service` (dejó de ser una librería
"compartida entre servicios" al pasar el proyecto a microservicios — ver
`../app-productos/docs/ROADMAP.md`, sección sobre esta decisión). Reglas
compartidas con el resto del proyecto en `../CLAUDE.md`.

## Qué es y qué no es

- Contiene el modelo de dominio de productos: `Producto`, `Sku`, puertos de
  paginación (`Pagina`, `CriterioPaginacion`, `listarPaginado`), etc.
- **Ningún servicio nuevo** (`carrito-service`, `pedidos-service`, etc.)
  debería agregarla como dependencia — cada servicio es dueño de su propio
  modelo de dominio. Es exclusiva de `productos-service`.

## Publicación

- Se publica a GitHub Packages vía un workflow de GitHub Actions que corre
  en cada push a `main`.
- La versión sigue siendo `0.0.1-SNAPSHOT` (el string no cambia); lo que
  cambia es el contenido del artefacto publicado. `app-productos` necesita
  `./gradlew build --refresh-dependencies` para tomar una versión nueva.

## Comandos

```bash
./gradlew build
./gradlew test
./gradlew pitest   # tests de mutacion del dominio -> build/reports/pitest/index.html
```

## Estado del repo

- Repo en GitHub (`llocado/lib-domain-foods`), rama `main`, pusheado y al
  día con `origin/main`.
- Puede haber cambios locales sin commitear en `gradle/wrapper/*` por
  actualizaciones del wrapper — no mezclar eso con cambios de dominio en el
  mismo commit.
