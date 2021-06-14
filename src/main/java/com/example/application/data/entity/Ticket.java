package com.example.application.data.entity;

import javax.persistence.Entity;

import com.example.application.data.AbstractEntity;
import java.time.LocalDate;
import java.time.LocalDate;

@Entity
public class Ticket extends AbstractEntity {

    private String descripcion;
    private String estado;
    private String categoria;
    private String prioridad;
    private LocalDate fechaCreacion;
    private LocalDate fechaUltimoEstado;
    private String mailCOntacto;

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getPrioridad() {
        return prioridad;
    }
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public LocalDate getFechaUltimoEstado() {
        return fechaUltimoEstado;
    }
    public void setFechaUltimoEstado(LocalDate fechaUltimoEstado) {
        this.fechaUltimoEstado = fechaUltimoEstado;
    }
    public String getMailCOntacto() {
        return mailCOntacto;
    }
    public void setMailCOntacto(String mailCOntacto) {
        this.mailCOntacto = mailCOntacto;
    }

}
