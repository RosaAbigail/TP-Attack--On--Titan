package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.*;

public class Mikasa {	
	// Variables	
	private double x;
	private double y;
	private double orientacion;
	private double radio;
	private double velocidad;
	Image m1_d;
	Image m1_i;
	Image m2_d;
	Image m2_i;
	
	// Constructores
	public Mikasa(double x, double y, double radio) {
		this.x = x;
		this.y = y;
		this.orientacion = orientacion;
		this.radio = radio;
		this.velocidad = 5;
		this.m1_d = Herramientas.cargarImagen("m1_d.png");
		this.m1_i = Herramientas.cargarImagen("m1_i.png");
		this.m2_d = Herramientas.cargarImagen("m2_d.png");
		this.m2_i = Herramientas.cargarImagen("m2_i.png");
	}
	
	// Metodos
	public void dibujar(Entorno e) { // Modificar
		if (this.radio == 75) {
			if (this.orientacion > -Math.PI/2 || this.orientacion > Math.PI/2) {
				e.dibujarImagen(m1_d, this.x, this.y, 0);
			}
			if (this.orientacion > Math.PI/2 || this.orientacion > -Math.PI/2) {
				e.dibujarImagen(m1_i, this.x, this.y, 0);
			}
		}
		if (this.radio == 125) {
			if (this.orientacion > -Math.PI/2 || this.orientacion > Math.PI/2) {
				e.dibujarImagen(m2_d, this.x, this.y, 0);
			}
			if (this.orientacion > Math.PI/2 || this.orientacion > -Math.PI/2) {
				e.dibujarImagen(m2_i, this.x, this.y, 0);
			}
		}
	}
	
  public void avanzar(Entorno e) {
		this.x += Math.cos(this.orientacion) * this.velocidad;
		this.y += Math.sin(this.orientacion) * this.velocidad;
	}	
	
  public void noAvanzar(Entorno e, Obstaculo o) { // Modificar
		// Colision con entorno
//		if (this.x < this.radio/2) {
//			this.x += this.velocidad;
//		}
//		if (this.y < this.radio/2) {
//	      	this.y += this.velocidad;
//		}
//		if (this.x > e.ancho() - this.radio/2) {
//		    this.x -= this.velocidad;
//		}
//		if (this.y > e.alto() - this.radio/2) {
//		    this.y -= this.velocidad;
//		}
		if (this.x < this.radio/2 || this.y < this.radio/2 || this.x > e.ancho() - this.radio/2 || this.y > e.alto() - this.radio/2) {
			this.orientacion += Math.PI/2;
		}
		
		// Colision con obstaculos
		if ((this.x - o.getX()) * (this.x - o.getX()) + (this.y - o.getY()) * (this.y - o.getY()) <= this.radio * o.getRadio()) { // Colision con obstaculos
			this.orientacion += Math.PI/2;
		}
	}	

  public void girar(double direccion) {
		this.orientacion += direccion;
		if (this.orientacion < 0) {
			this.orientacion += Math.PI * 2;
		}
		if (this.orientacion > Math.PI * 2) {
			this.orientacion -= Math.PI * 2;
		}
	}	
	
  public boolean colisionConKyojin(Entorno e, Kyojin k) {
		return (this.x - k.getX()) * (this.x - k.getX()) + (this.y - k.getY()) * (this.y - k.getY()) <= this.radio * k.getRadio();
	}
	
  public boolean colisionConObstaculo(Entorno e, Obstaculo o) {
		return (this.x - o.getX()) * (this.x - o.getX()) + (this.y - o.getY()) * (this.y - o.getY()) <= this.radio * o.getRadio();
	}	
	
  public boolean colisionConSuero(Entorno e, Suero s) {
		return (this.x - s.getX()) * (this.x - s.getX()) + (this.y - s.getY()) * (this.y - s.getY()) <= this.radio * s.getRadio();
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
	
  public double getOrientacion() {
		return this.orientacion;
	}
	
  public double getRadio() {
		return this.radio;
	}
  
}
