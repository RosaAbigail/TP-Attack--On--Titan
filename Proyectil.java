package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.*;

public class Proyectil {
	// Variables
	private double x;
	private double y;
	private double orientacion;
	private double velocidad;
	private double radio;
	Image p;
	
	// Constructores
	public Proyectil(Entorno e, double x, double y, double orientacion) {
		this.x = x;
    this.y = y;
    this.orientacion = orientacion;
    this.velocidad = 5;
    this.radio = 25;
		this.p = Herramientas.cargarImagen("p.png");
	}
	
	// Metodos
	public void dibujar(Entorno e) {
		e.dibujarImagen(this.p, this.x, this.y, 0);
	}
	
  public void desplazamiento() {
        this.x += Math.cos(this.orientacion) * this.velocidad;
        this.y += Math.sin(this.orientacion) * this.velocidad;
	}			
	
  public boolean colisionConKyojin(Kyojin k) {
		return (this.x - k.getX()) * (this.x - k.getX()) + (this.y - k.getY()) * (this.y - k.getY()) <= this.radio * k.getRadio();
	}
	
  public boolean colisionConObstaculo (Obstaculo o) {
		return (this.x - o.getX()) * (this.x - o.getX()) + (this.y - o.getY()) * (this.y - o.getY()) <= this.radio * o.getRadio();
	}
	
  public boolean colisionConEntorno(Entorno e) {
		return this.x <= this.radio || this.y <= this.radio || this.x >= e.ancho() - this.radio || this.y >= e.alto() - this.radio;
	}
	
  public double getX() {
		return this.x;
	}
	
  public double getY() {
		return this.y;
	}
	
  public double getRadio() {
		return this.radio;
	}
}
