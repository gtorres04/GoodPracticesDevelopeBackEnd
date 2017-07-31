package dominio;

import java.util.Date;

public class Prestamo {

	private Date fechaSolicitud;
	private Libro libro;
	private Date fechaEntregaMaxima;
	private String nombreUsuario;

	public Prestamo(Libro libro) {
		this.fechaSolicitud = new Date();
		this.libro = libro;
	}
	
	public Prestamo(Date fechaSolicitud, Libro libro, Date fechaEntregaMaxima, String nombreUsuario) {
		this.fechaSolicitud = fechaSolicitud;
		this.libro = libro;
		this.fechaEntregaMaxima = fechaEntregaMaxima;
		this.nombreUsuario = nombreUsuario;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public Libro getLibro() {
		return libro;
	}

	public Date getFechaEntregaMaxima() {
		return fechaEntregaMaxima;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * @param libro the libro to set
	 */
	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	/**
	 * @param fechaEntregaMaxima the fechaEntregaMaxima to set
	 */
	public void setFechaEntregaMaxima(Date fechaEntregaMaxima) {
		this.fechaEntregaMaxima = fechaEntregaMaxima;
	}

	/**
	 * @param nombreUsuario the nombreUsuario to set
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

}
