package br.example;

import java.util.Objects;

public class Automovel {
    private String placa; // Chave única
    private String modelo;
    private String marca;
    private int ano;
    private double valor;

    public Automovel(String placa, String modelo, String marca, int ano, double valor) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.valor = valor;
    }

    // Getters
    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }

    public int getAno() {
        return ano;
    }

    public double getValor() {
        return valor;
    }

    // Setters (placa não deve ter setter público para manter unicidade via lógica de negócio)
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Automovel {" +
               "placa='" + placa + '\'' +
               ", modelo='" + modelo + '\'' +
               ", marca='" + marca + '\'' +
               ", ano=" + ano +
               ", valor=" + String.format("%.2f", valor) +
               '}';
    }

    // Formato CSV para persistência
    public String toCsvString() {
        return placa + "," + modelo + "," + marca + "," + ano + "," + valor;
    }

    // Para facilitar a busca e remoção no ArrayList
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Automovel automovel = (Automovel) o;
        return Objects.equals(placa, automovel.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placa);
    }
}