package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.*;

public class EnergiaExtra {
	// Variables
	private double x;
	private double y;
	private double radio;
	Image ee;
	
	// Constructores
	public EnergiaExtra(double x, double y) {
		this.x = x;
		this.y = y;
		this.radio = 25;
		this.ee = Herramientas.cargarImagen("ee.png"); 
	}
	
	// Metodos
	public void dibujar(Entorno e) {
		e.dibujarImagen(this.ee, this.x, this.y, 0);
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
