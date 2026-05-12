package com.LoveCode;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String password;
    private String ciudad;
    private String descripcion;
    private java.util.List<Integer> tecnologiasIds;

    // Constructores
    public Usuario() {}
    
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public java.util.List<Integer> getTecnologiasIds() { return tecnologiasIds; }
    public void setTecnologiasIds(java.util.List<Integer> tecnologiasIds) { this.tecnologiasIds = tecnologiasIds; }
    
}