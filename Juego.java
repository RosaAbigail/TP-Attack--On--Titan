package juego;

import java.awt.*;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
  private Entorno entorno;
  private Proyectil p;
  private Mikasa m;
  private Suero s;
  private Kyojin j;
  private Kyojin[] k;
  private Obstaculo[] o;
  private int vidaJefe, proyectilesRestantes, kyojinesAsesinados, puntaje, vidas;
  private boolean tomarSuero, finDelJuego;
	Image fondo_juego, r;

	public Juego() {
    this.entorno = new Entorno(this, "Attack on Titan: Wings of Freedom", 1280, 700);
		m = new Mikasa(entorno.ancho()/2, entorno.alto()/2, 75);
		k = new Kyojin[6];
		o = new Obstaculo[6];
		vidaJefe = 0;
		proyectilesRestantes = 100;
		kyojinesAsesinados = 0;
    puntaje = 0;
    vidas = 3;
    tomarSuero = false;
		finDelJuego = false;
		fondo_juego = Herramientas.cargarImagen("fondo_juego.jpg");
		r = Herramientas.cargarImagen("r.png");
	  
    Obstaculo.crearObstaculos(entorno, o);
	  Kyojin.crearKyojines(entorno, k, m);
		
    this.entorno.iniciar();
	}

	public void tick() {
		entorno.dibujarImagen(fondo_juego, entorno.ancho()/2, entorno.alto()/2, 0);
		
		// Obstaculos
		for (int i = 0; i < o.length; i++) {
			if (o[i] != null) {
				entorno.dibujarCirculo(o[i].getX(), o[i].getY(), o[i].getRadio(), Color.BLACK);
				o[i].dibujar(entorno);
			}
		}
		
		// Kyojines
		for (int i = 0; i < k.length; i++) {
			if (k[i] != null) {
				entorno.dibujarCirculo(k[i].getX(), k[i].getY(), k[i].getRadio(), Color.BLACK);
				k[i].dibujar(entorno);
				k[i].avanzar(entorno, m);
				if (m.colisionConKyojin(entorno, k[i])) { // Colision Mikasa-Kyojin dependiendo del estado de Mikasa
					if (tomarSuero == true) {
						k[i] = null;
						tomarSuero = false;
						m = new Mikasa (m.getX(), m.getY(), 75);
					}
					if (tomarSuero == false) {
						finDelJuego = true;
					}
				}
			}
			if (k[i] == null) { // Reaparicion de los kyojines
				k[i] = new Kyojin(335, 635, m.getOrientacion(), 125, 0.5);
			}
			for (int j = 0; j < k.length - 1; j++) { // Colision kyojin-kyojin (Modificar)
				if (k[i] != null && k[j] != null && i != j) {
					if (k[i].colisionEntreKyojines(entorno, k[i], k[j])) {
						k[i].noSuperponer(entorno, k[i], k[j]);
					}
				}
			}
		}
		
		// Jefe
		if (kyojinesAsesinados == 6 ) { // Creacion del jefe
			j = new Kyojin(1025, 100, m.getOrientacion(), 150, 1.5);
		}
		
		if (j != null) {
			entorno.dibujarCirculo(j.getX(), j.getY(), j.getRadio(), Color.BLACK);
			j.dibujar(entorno);
			j.avanzar(entorno, m);
			if (m.colisionConKyojin(entorno, j)) { // Colision Mikasa-Kyojin dependiendo del estado de Mikasa
				if (tomarSuero == true) {
					if (vidaJefe < 5) {
						vidaJefe += 1;
						tomarSuero = false;
						m = new Mikasa (m.getX(), m.getY(), 75);
					}
					if (vidaJefe == 5) {
						j = null;
						vidaJefe = 0;
						tomarSuero = false;
						m = new Mikasa (m.getX(), m.getY(), 75);
						kyojinesAsesinados += 5;
						puntaje += 30;
					}
				}
				if (tomarSuero == false) {
					finDelJuego = true;
				}
			}
		}
		
		// Mikasa
		entorno.dibujarCirculo(m.getX(), m.getY(), m.getRadio(), Color.BLACK);
		m.dibujar(entorno);
		entorno.dibujarTriangulo(m.getX(), m.getY(), 15, 15, m.getOrientacion(), Color.BLUE);
		entorno.dibujarImagen(r, m.getX(), m.getY(), m.getOrientacion());
		
		if (entorno.estaPresionada('w')) {
			m.avanzar(entorno);
		}
		if (entorno.estaPresionada('a')) {
			m.girar((-1/360.0) * (Math.PI*5));
		}
		if (entorno.estaPresionada('d')) {
			m.girar((1/360.0) * (Math.PI*5));
		}
		
		for (int i = 0; i < o.length; i++) { // Colision con entorno u obstaculo
			if (m.colisionConEntorno(entorno) || m.colisionConObstaculo(entorno, o[i])) {
				m.noAvanzar(entorno, o[i]);
			}
		}
		
		// Suero
		if (tomarSuero == false) {
			s = new Suero(150, 150);
			entorno.dibujarCirculo(s.getX(), s.getY(), s.getRadio(), Color.BLACK);
			s.dibujar(entorno);
			if (m.colisionConSuero(entorno, s)) {
				m = new Mikasa (s.getX(), s.getY(), 125);
				entorno.dibujarCirculo(s.getX(), s.getY(), 125, Color.BLACK);
				m.dibujar(entorno);
				tomarSuero = true;
				s = null;
			}
		}
		
		// Proyectiles
		if (p == null && proyectilesRestantes > 0 && tomarSuero == false) { // Crear proyectil
			if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
				p = new Proyectil(entorno, m.getX(), m.getY(), m.getOrientacion());
				proyectilesRestantes -= 1;
			}
		}
		
		if (p != null) {
			entorno.dibujarCirculo(p.getX(), p.getY(), p.getRadio(), Color.BLACK);
			p.dibujar(entorno);
			p.desplazamiento();
			if (p != null && j != null && p.colisionConKyojin(j)) { // Colision con jefe
				if (vidaJefe < 5) {
					vidaJefe += 1;
					p = null;
				}
				if (vidaJefe == 5) {
					j = null;
					p = null;
					vidaJefe = 0;
					kyojinesAsesinados += 1;
					puntaje += 30;
				}
			}
			for (int i = 0; i < k.length; i++) { // Colision con kyojines
				if (p != null && k[i] != null && p.colisionConKyojin(k[i])) {
					k[i] = null;
					p = null;
					kyojinesAsesinados += 1;
					puntaje += 10;
				}
			}
			for (int i = 0; i < o.length; i++) { // Colision con obstaculos
				if (p != null && (p.colisionConObstaculo(o[i]) || p.colisionConEntorno(entorno))) {
					p = null;
				}
			}
		}
				
		// Texto en pantalla
		entorno.cambiarFont("sans", 20, Color.WHITE);
	  entorno.escribirTexto("Proyectiles restantes: " + proyectilesRestantes, 50, 25);
	  entorno.escribirTexto("Titanes eliminados: " + kyojinesAsesinados, 50, 50);
	  entorno.escribirTexto("Puntaje: " + puntaje, 50, 75);
	  entorno.escribirTexto("Vidas: " + vidas, 50, 100);

	  // Fin del juego
	  // ...	    
	}
	
	// Inicia el juego
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
