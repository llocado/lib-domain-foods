package com.foodstore.domain.productos.domain.model;

import java.util.Objects;

public class Producto {

    private final ProductoId id;
    private Sku sku;
    private String nombre;
    private String descripcion;
    private Precio precio;
    private Stock stock;
    private CategoriaId categoriaId;
    private boolean activo;

    private Producto(ProductoId id, Sku sku, String nombre, String descripcion,
                     Precio precio, Stock stock, CategoriaId categoriaId, boolean activo) {
        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
        this.activo = activo;
    }

    public static Producto crear(Sku sku, String nombre, String descripcion,
                                 Precio precio, CategoriaId categoriaId) {
        validarNombre(nombre);
        return new Producto(
                ProductoId.nuevo(),
                Objects.requireNonNull(sku, "El SKU no puede ser nulo"),
                nombre,
                descripcion,
                Objects.requireNonNull(precio, "El precio no puede ser nulo"),
                Stock.cero(),
                Objects.requireNonNull(categoriaId, "La categoría no puede ser nula"),
                true
        );
    }

    public static Producto reconstruir(ProductoId id, Sku sku, String nombre, String descripcion,
                                       Precio precio, Stock stock, CategoriaId categoriaId, boolean activo) {
        validarNombre(nombre);
        return new Producto(
                Objects.requireNonNull(id, "El id no puede ser nulo"),
                Objects.requireNonNull(sku, "El SKU no puede ser nulo"),
                nombre,
                descripcion,
                Objects.requireNonNull(precio, "El precio no puede ser nulo"),
                Objects.requireNonNull(stock, "El stock no puede ser nulo"),
                Objects.requireNonNull(categoriaId, "La categoría no puede ser nula"),
                activo
        );
    }

    public void cambiarPrecio(Precio nuevoPrecio) {
        this.precio = Objects.requireNonNull(nuevoPrecio, "El nuevo precio no puede ser nulo");
    }

    public void aumentarStock(int unidades) {
        this.stock = this.stock.incrementar(unidades);
    }

    public void reducirStock(int unidades) {
        this.stock = this.stock.decrementar(unidades);
    }

    public void renombrar(String nuevoNombre) {
        validarNombre(nuevoNombre);
        this.nombre = nuevoNombre;
    }

    public void actualizarDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
    }

    public void reclasificar(CategoriaId nuevaCategoriaId) {
        this.categoriaId = Objects.requireNonNull(nuevaCategoriaId, "La nueva categoría no puede ser nula");
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }

    public boolean tieneDisponibilidad(int unidadesRequeridas) {
        return activo && stock.esSuficientePara(unidadesRequeridas);
    }

    private static void validarNombre(String nombre) {
        Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 150) {
            throw new IllegalArgumentException("El nombre no puede superar los 150 caracteres");
        }
    }

    public ProductoId getId() { return id; }
    public Sku getSku() { return sku; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Precio getPrecio() { return precio; }
    public Stock getStock() { return stock; }
    public CategoriaId getCategoriaId() { return categoriaId; }
    public boolean isActivo() { return activo; }
}
