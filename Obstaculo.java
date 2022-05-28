package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.*;

public class Obstaculo {
	// Variables
	private double x;
	private double y;
	private double radio;
	Image o;
	
	// Constructores
	public Obstaculo (double x, double y) {
		this.x = x;
		this.y = y;
		this.radio = 100;
		this.o = Herramientas.cargarImagen("o.png"); 
	}
	
	// Metodos
	public static void crearObstaculos(Entorno e, Obstaculo[] o) {
	    for (int i = 0; i < o.length; i++) {
	    	double x = 0;
			double y = 0;
			o[i] = new Obstaculo(x, y);
		}
		o[0] =  new Obstaculo(100, 100);
		o[1] =  new Obstaculo(100, 400);
		o[2] =  new Obstaculo(400, 625);
		o[3] =  new Obstaculo(500, 100);
		o[4] =  new Obstaculo(800, 550);
		o[5] =  new Obstaculo(1150, 350);
	}
	
	public void dibujar(Entorno e) {
		e.dibujarImagen(this.o, this.x, this.y, 0);
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
