# productos-dominio

Módulo de dominio y casos de uso del contexto **Productos** para el sistema de supermercado.

## Por qué no tiene dependencias de Spring

Este módulo implementa la regla fundamental de la arquitectura hexagonal: **el núcleo del dominio no depende de la infraestructura**. Spring Boot es un framework de infraestructura; hacer que el dominio lo conozca invertiría la dirección de dependencia correcta.

Beneficios concretos:
- Los tests de dominio y de aplicación son instantáneos (JVM pura, sin contexto de Spring).
- El modelo de negocio puede evolucionar sin que cambios de framework lo afecten.
- El jar puede ser consumido por cualquier tecnología de entrega (Spring Boot, Quarkus, CLI, etc.).

## Estructura de paquetes

```
com.supermercado.productos
├── domain
│   ├── model               # Entidades y value objects (Producto, Sku, Precio, Stock, …)
│   └── exception           # Excepciones de dominio (runtime)
└── application
    ├── port
    │   ├── in              # Interfaces de casos de uso (driving ports)
    │   └── out             # Interfaces que el dominio necesita de infraestructura (driven ports)
    └── service             # ProductoService: implementación de los casos de uso
```

### Modelo de dominio

| Clase | Tipo | Descripción |
|---|---|---|
| `Producto` | Aggregate root | Entidad central; encapsula todas las reglas de negocio |
| `ProductoId` | Value object | Envuelve `UUID` |
| `Sku` | Value object | String normalizado en mayúsculas, validado con regex |
| `Precio` | Value object | `BigDecimal` + `Currency`; inmutable; método `aplicarDescuento` |
| `Stock` | Value object | Cantidad entera ≥ 0; `incrementar`/`decrementar` devuelven nuevas instancias |
| `CategoriaId` | Value object | Referencia por id a la categoría (otro agregado futuro) |

### Puertos

| Puerto | Dirección | Descripción |
|---|---|---|
| `CrearProductoUseCase` | Entrada | Alta de un producto nuevo |
| `ActualizarPrecioUseCase` | Entrada | Cambio de precio |
| `AjustarStockUseCase` | Entrada | Incremento / decremento de stock |
| `BuscarProductoUseCase` | Entrada | Consulta por id y listado |
| `EliminarProductoUseCase` | Entrada | Baja de producto |
| `ProductoRepositoryPort` | Salida | Persistencia (implementado en el adaptador JPA) |

## Cómo dependerán los módulos de infraestructura

```
productos-dominio.jar  ←  productos-api (Spring Boot / REST)
                       ←  productos-persistencia (Spring Data JPA)
```

**`productos-persistencia`** implementará `ProductoRepositoryPort`:

```java
// En el módulo productos-persistencia
@Repository
public class ProductoJpaRepository implements ProductoRepositoryPort {

    private final SpringDataProductoRepository jpa;

    @Override
    public Producto guardar(Producto producto) {
        ProductoEntity entity = ProductoMapper.toEntity(producto);
        return ProductoMapper.toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Producto> buscarPorId(ProductoId id) {
        return jpa.findById(id.getValor()).map(ProductoMapper::toDomain);
    }
    // ...
}
```

**`productos-api`** recibirá `ProductoService` por inyección de Spring y lo expondrá como REST:

```java
// En el módulo productos-api
@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final CrearProductoUseCase crearProductoUseCase;

    public ProductoController(CrearProductoUseCase crearProductoUseCase) {
        this.crearProductoUseCase = crearProductoUseCase;
    }
    // ...
}
```

La dependencia de Gradle en cada módulo de infraestructura:

```groovy
dependencies {
    implementation project(':productos-dominio')
}
```

## Build

Requiere Java 21 y Gradle 8+.

```bash
# Desde la raíz del proyecto multi-módulo
./gradlew :productos-dominio:build

# Solo tests del módulo
./gradlew :productos-dominio:test

# Publicar el jar en el repositorio local de Maven (para otros módulos locales)
./gradlew :productos-dominio:publishToMavenLocal
```

> Si prefieres Maven Wrapper, agrega el plugin `maven-publish` al `build.gradle` y ejecuta `./mvnw clean install` desde la raíz una vez configurado el `pom.xml` equivalente.
