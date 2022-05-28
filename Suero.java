package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.*;

public class Suero {
	// Variables
	private double x;
	private double y;
	private double radio;
	Image s;
	
	// Constructores
	public Suero(double x, double y) {
		this.x = x;
		this.y = y;
		this.radio = 25;
		this.s = Herramientas.cargarImagen("s.png"); 
	}
	
	// Metodos
	public void dibujar(Entorno e) {
		e.dibujarImagen(this.s, this.x, this.y, 0);
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
