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
	public static void crearKyojines(Entorno e, Kyojin[] k, Mikasa m) {
		for (int i = 0; i < k.length; i++) {
			double x = 0;
			double y = 0;
			k[i] = new Kyojin(x, y, m.getOrientacion(), 125, 1.5);
		}
		k[0] =  new Kyojin(175, 600, m.getOrientacion(), 125, 1.5);
		k[1] =  new Kyojin(300, 250, m.getOrientacion(), 125, 1.5);
		k[2] =  new Kyojin(550, 575, m.getOrientacion(), 125, 1.5);
		k[3] =  new Kyojin(700, 150, m.getOrientacion(), 125, 1.5);
		k[4] =  new Kyojin(950, 450, m.getOrientacion(), 125, 1.5);
		k[5] =  new Kyojin(1150, 200, m.getOrientacion(), 125, 1.5);
	}
	public void dibujar(Entorno e) {
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
	public void movimiento(Entorno e, Mikasa m) {
		// Movimiento
		this.orientacion = Math.atan2((m.getY()-this.y), (m.getX()-this.x));
		this.x += Math.cos(this.orientacion) * this.velocidad;
		this.y += Math.sin(this.orientacion) * this.velocidad;
		
		// Colision con el entorno
		if (this.x < this.radio/2) {
			this.x = this.radio/2;
		}
		if (this.y < this.radio/2) {
	      	this.y = this.radio/2;
		}
		if (this.x > e.ancho() - this.radio/2) {
		    this.x = e.ancho() - this.radio/2;
		}
		if (this.y > e.alto() - this.radio/2) {
		    this.y = e.alto() - this.radio/2;
		}
	}	
	
	public void rodearObstaculo(Entorno e, Obstaculo o) {
		if ((this.x - o.getX()) * (this.x - o.getX()) + (this.y - o.getY()) * (this.y - o.getY()) <= this.radio * o.getRadio()) {
			this.x += Math.cos(this.orientacion + Math.PI/2) * 2;
			this.y += Math.sin(this.orientacion + Math.PI/2) * 2;
		}
	}	
	
	public void noSuperponer(Entorno e, Kyojin k1, Kyojin k2) {
		this.orientacion = Math.atan2((k2.getY() - k1.getY()), (k2.getX() - k1.getX()));
		this.x += Math.cos(this.orientacion) * (-1);
		this.y += Math.sin(this.orientacion) * (-1);
	}
	
	public boolean colisionEntreKyojines(Entorno e, Kyojin k1, Kyojin k2) {
		return (k1.getX() - k2.getX()) * (k1.getX() - k2.getX()) + (k1.getY() - k2.getY()) * (k1.getY() - k2.getY()) <= k1.getRadio() * k2.getRadio();
	}
	
	public boolean colisionConObstaculos(Entorno e, Obstaculo o) {
		return (this.x - o.getX()) * (this.x - o.getX()) + (this.y - o.getY()) * (this.y - o.getY()) <= this.radio * o.getRadio();
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
