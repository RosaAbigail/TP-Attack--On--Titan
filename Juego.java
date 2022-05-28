package juego;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.sound.sampled.Clip;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	private Entorno entorno;
	private EnergiaExtra ee;
    	private Proyectil p;
	private Suero s;
	private Mikasa m;
	private Kyojin j;
    	private Kyojin[] k;
    	private Obstaculo[] o;
    	private int energia, vidaJefe, proyectilesRestantes, kyojinesAsesinados, puntaje;
    	private boolean energiaBaja, tomarSuero, finDelJuego;
    	private Clip ganaMikasa, juegoMusica, muereMikasa;
	Image fondo_juego, r;

	public Juego() {
		// Dibujar el entorno
		this.entorno = new Entorno(this, "Attack on Titan: Wings of Freedom", 1280, 700);
		m = new Mikasa(entorno.ancho()/2, entorno.alto()/2, 75);
		k = new Kyojin[6];
		o = new Obstaculo[6];
	    	Kyojin.crearKyojines(entorno, k, m);
		Obstaculo.crearObstaculos(entorno, o);
	    
	    	// Variables de control
		vidaJefe = 0;
		proyectilesRestantes = 100;
		kyojinesAsesinados = 0;
        	puntaje = 0;
        	energia = 300;
        
        	// Estados para el juego
        	energiaBaja = false;
        	tomarSuero = false;
		finDelJuego = false;
		
		// Extras
		fondo_juego = Herramientas.cargarImagen("fondo_juego.jpg");
		r = Herramientas.cargarImagen("r.png");
	    	this.ganaMikasa = Herramientas.cargarSonido("ganaMikasa.wav");
	    	this.juegoMusica = Herramientas.cargarSonido("juegoMusica.wav");
	    	this.muereMikasa = Herramientas.cargarSonido("muereMikasa.wav");
	    
	    	// Iniciar el juego
	    	this.entorno.iniciar();
	}
	
	ActionListener finDelTiempo = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			finDelJuego = true;
		}
	};
	
	public void tick() {
		Timer tiempoJuego = new Timer(85000, finDelTiempo);	
		tiempoJuego.start();
		juegoMusica.start();
		entorno.dibujarImagen(fondo_juego, entorno.ancho()/2, entorno.alto()/2, 0);
		
		// Obstaculos
		for (int i = 0; i < o.length; i++) {
			if (o[i] != null) {
				entorno.dibujarCirculo(o[i].getX(), o[i].getY(), o[i].getRadio(), Color.getColor(null, new Color(0,0,0,0)));
				o[i].dibujar(entorno);
				if (m.colisionConObstaculo(entorno, o[i])) { // Colision Mikasa-obstaculos
					m.rodearObstaculo(entorno, o[i]);
				}
			}
		}
		
		// Kyojines
		for (int i = 0; i < k.length; i++) {
			if (k[i] != null) {
				entorno.dibujarCirculo(k[i].getX(), k[i].getY(), k[i].getRadio(), Color.getColor(null, new Color(0,0,0,0)));
				k[i].dibujar(entorno);
				k[i].movimiento(entorno, m);
				if (m.colisionConKyojin(entorno, k[i])) { // Colision Kyojin-Mikasa dependiendo del estado de Mikasa
					if (energia > 0) {
						if (tomarSuero == true) {
							tomarSuero = false;
							k[i] = null;
							m = new Mikasa(m.getX(), m.getY(), 75);
							kyojinesAsesinados += 1;
							puntaje += 10;
						}
						if (tomarSuero == false) {
							energia -= 1;
						}
					}
					if (energia == 0) {
						finDelJuego = true;
					}
				}
			}
			for (int ii = 0; ii < k.length - 1; ii++) { // Colision Kyojin-Kyojin
				if (k[i] != null && k[ii] != null && i != ii) {
					if (k[i].colisionEntreKyojines(entorno, k[i], k[ii])) {
						k[i].noSuperponer(entorno, k[i], k[ii]);
					}
				}
			}
			for (int j = 0; j < o.length; j++) { // Colision Kyojin-Obstaculo
				if (k[i] != null) {
					if (k[i].colisionConObstaculos(entorno, o[j])) {
						k[i].rodearObstaculo(entorno, o[j]);
					}
				}
			}
			if (k[i] == null) { // Reaparicion de los kyojines
				k[i] = new Kyojin(1200, 150, m.getOrientacion(), 125, 1.5);
			}			
		}
		
		// Jefe
		if (kyojinesAsesinados > 0 && kyojinesAsesinados % 15 == 0) { // Creacion del jefe
			j = new Kyojin(75, 250, m.getOrientacion(), 150, 3);
		}
		
		if (j != null) {
			entorno.dibujarCirculo(j.getX(), j.getY(), j.getRadio(), Color.getColor(null, new Color(0,0,0,0)));
			j.dibujar(entorno);
			j.movimiento(entorno, m);
			if (m.colisionConKyojin(entorno, j)) { // Colision Jefe-Mikasa dependiendo del estado de Mikasa
				if (energia > 0) {
					if (tomarSuero == true) {
						if (vidaJefe < 5) {
							tomarSuero = false;
							vidaJefe += 1;
							m = new Mikasa(m.getX(), m.getY(), 75);
						}
						if (vidaJefe == 5) {
							tomarSuero = false;
							j = null;
							m = new Mikasa(m.getX(), m.getY(), 75);
							vidaJefe = 0;
							kyojinesAsesinados += 1;
							puntaje += 30;
						}
					}
					if (tomarSuero == false) {
						energia -= 1;
					}
				}
				if (energia == 0) {
					finDelJuego = true;
				}
			}
			for (int i = 0; i < k.length; i++) { // Colision Jefe-Kyojin
				if (k[i] != null) {
					if (j.colisionEntreKyojines(entorno, j, k[i])) {
						j.noSuperponer(entorno, j, k[i]);
					}
				}
			}
			for (int ii = 0; ii < o.length; ii++) { // Colision Jefe-Obstaculo o Jefe-Entorno
				if (j.colisionConObstaculos(entorno, o[ii])) {
					j.rodearObstaculo(entorno, o[ii]);
				}
			}
		}
		
		// Mikasa
		entorno.dibujarCirculo(m.getX(), m.getY(), m.getRadio(), Color.getColor(null, new Color(0,0,0,0)));
		m.dibujar(entorno);
		entorno.dibujarTriangulo(m.getX(), m.getY(), 15, 15, m.getOrientacion(), Color.getColor(null, new Color(0,0,0,0)));
		entorno.dibujarImagen(r, m.getX(), m.getY(), m.getOrientacion());
		
		if (entorno.estaPresionada('w')) {
			m.movimiento(entorno);
		}
		if (entorno.estaPresionada('a')) {
			m.girar((-1/360.0) * (Math.PI*5));
		}
		if (entorno.estaPresionada('d')) {
			m.girar((1/360.0) * (Math.PI*5));
		}
		
		// Energia Extra
		if (energia < 100) {
			energiaBaja = true;
		}
		
		if (energiaBaja == true) {
			ee = new EnergiaExtra(1150, 75);
			entorno.dibujarCirculo(ee.getX(), ee.getY(), ee.getRadio(), Color.getColor(null, new Color(0,0,0,0)));
			ee.dibujar(entorno);
			if (m.colisionConExtra(entorno, ee)) {
				energia = energia + 50;
				ee = null;
				energiaBaja = false;
			}
		}
		
		// Suero
		if (tomarSuero == false) {
			s = new Suero(75, 600);
			entorno.dibujarCirculo(s.getX(), s.getY(), s.getRadio(), Color.getColor(null, new Color(0,0,0,0)));
			s.dibujar(entorno);
			if (m.colisionConSuero(entorno, s)) {
				m = new Mikasa (s.getX(), s.getY(), 125);
				entorno.dibujarCirculo(s.getX(), s.getY(), 125, Color.getColor(null, new Color(0,0,0,0)));
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
			entorno.dibujarCirculo(p.getX(), p.getY(), p.getRadio(), Color.getColor(null, new Color(0,0,0,0)));
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
			for (int i = 0; i < o.length; i++) { // Colision con obstaculos o entorno
				if (p != null && (p.colisionConObstaculo(o[i]) || p.colisionConEntorno(entorno))) {
					p = null;
				}
			}
		}
				
		// Texto en pantalla
	    	entorno.cambiarFont("arial black", 20, Color.WHITE);
	    	entorno.escribirTexto("PROYECTILES RESTANTES: " + proyectilesRestantes, 50, 25);
	    	entorno.escribirTexto("KYOJINES ELIMINADOS: " + kyojinesAsesinados, 50, 50);
	    	entorno.escribirTexto("PUNTAJE: " + puntaje, 50, 75);
	    	entorno.escribirTexto("ENERGIA RESTANTE: " + energia, 50, 100);

	    	// Fin del juego
	    	if (finDelJuego && energia == 0) {
			juegoMusica.stop();
	    	    	this.entorno.removeAll();
            	    	muereMikasa.start();
            	    	entorno.dibujarImagen(fondo_juego, entorno.ancho()/2, entorno.alto()/2, 0);
            	    	entorno.cambiarFont("arial black", 40, Color.RED);
		    	entorno.escribirTexto("¡PERDISTE!", 500, 300);
		    	entorno.escribirTexto("Tu puntaje total fue: " + puntaje, 400, 400);
	    	}
	    	if (finDelJuego && energia > 0) {
		    	juegoMusica.stop();
		    	this.entorno.removeAll();
		    	ganaMikasa.start();
		    	entorno.dibujarImagen(fondo_juego, entorno.ancho()/2, entorno.alto()/2, 0);
		    	entorno.cambiarFont("arial black", 40, Color.BLUE);
		    	entorno.escribirTexto("¡GANASTE!", 500, 300);
		    	entorno.escribirTexto("Tu puntaje total fue: " + puntaje, 400, 400);
	    	}
	}
	
	// Inicia el juego
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
