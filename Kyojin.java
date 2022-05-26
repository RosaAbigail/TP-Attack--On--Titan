package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.*;

public class Kyojin {
	// Variables
	private double x;
	private double y;
	private double orientacion;
	private double radio;
	private double velocidad;
	Image k_d;
	Image k_i;
	Image j_d;
	Image j_i;
	
	// Constructores
	public Kyojin(double x, double y, double orientacion, double radio, double velocidad) {
		this.x = x;
		this.y = y;
		this.orientacion = orientacion;
		this.radio = radio;
		this.velocidad = velocidad;
		this.k_d = Herramientas.cargarImagen("k_d.png");
		this.k_i = Herramientas.cargarImagen("k_i.png");
		this.j_d = Herramientas.cargarImagen("j_d.png");
		this.j_i = Herramientas.cargarImagen("j_i.png");
	}
	
	// Metodos
	public void dibujar(Entorno e) { // Modificar 
		if (this.radio == 125) {
			if (this.orientacion > -Math.PI/2 || this.orientacion < Math.PI/2) {
				e.dibujarImagen(k_d, this.x, this.y, 0);
			}
			if (this.orientacion > Math.PI/2 || this.orientacion < -Math.PI/2) {
				e.dibujarImagen(k_i, this.x, this.y, 0);
			}
		}
		if (this.radio == 150) {
			if (this.orientacion > -Math.PI/2 || this.orientacion < Math.PI/2) {
				e.dibujarImagen(j_d, this.x, this.y, 0);
			}
			if (this.orientacion > Math.PI/2 || this.orientacion < -Math.PI/2) {
				e.dibujarImagen(j_i, this.x, this.y, 0);
			}
		}
	}	
	
  public static void crearKyojines(Entorno e, Kyojin[] k, Mikasa m) {
		for (int i = 0; i < k.length; i++) {
			double x = 0;
			double y = 0;
			k[i] = new Kyojin(x, y, m.getOrientacion(), 125, 0.5);
		}
		k[0] =  new Kyojin(900, 75, m.getOrientacion(), 125, 0.5);
		k[1] =  new Kyojin(275, 290, m.getOrientacion(), 125, 0.5);
		k[2] =  new Kyojin(500, 100, m.getOrientacion(), 125, 0.5);
		k[3] =  new Kyojin(465, 615, m.getOrientacion(), 125, 0.5);
		k[4] =  new Kyojin(990, 560, m.getOrientacion(), 125, 0.5);
		k[5] =  new Kyojin(1125, 375, m.getOrientacion(), 125, 0.5);
	}
	
  public void avanzar(Entorno e, Mikasa m) {
		double dx = m.getX() - this.x;
		double dy = m.getY() - this.y;
		double h = Math.sqrt((dx * dx) + (dy * dy));
		this.x += (dx / h) * this.velocidad;
		this.y += (dy / h) * this.velocidad;
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
			this.orientacion += Math.PI;
		}

		// Colision con obstaculos
		if ((this.x - o.getX()) * (this.x - o.getX()) + (this.y - o.getY()) * (this.y - o.getY()) <= this.radio * o.getRadio()) { // Colision con obstaculos
			this.orientacion += Math.PI;
		}
	}	
	
  public void noSuperponer(Entorno e, Kyojin k1, Kyojin k2) { // Modificar
		if ((k1.getX() - k2.getX()) * (k1.getX() - k2.getX()) + (k1.getY() - k2.getY()) * (k1.getY() - k2.getY()) <= k1.getRadio() * k2.getRadio()) {
			this.orientacion += Math.PI/2;
		}
	}
	
  public boolean colisionEntreKyojines(Entorno e, Kyojin k1, Kyojin k2) {
		return (k1.getX() - k2.getX()) * (k1.getX() - k2.getX()) + (k1.getY() - k2.getY()) * (k1.getY() - k2.getY()) <= k1.getRadio() * k2.getRadio();
	}
	
  public boolean colisionConObstaculos(Entorno e, Obstaculo o) {
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
	
  public double getOrientacion() {
		return this.orientacion;
	}

  public double getRadio() {
		return this.radio;
	}

}
