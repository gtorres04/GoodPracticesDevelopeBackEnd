package dominio;

import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String EL_LIBRO_SE_UTILIZA_EN_BIBLIOTECA = "los libros palíndromos solo se pueden utilizar en la biblioteca";
	public static final String EL_NOMBRE_USUARIO_ES_REQUERIDO = "El nombre del usuario es requerido";

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	/**
	 * Realiza el proceso de prestamo de un libro.
	 * 
	 * @param prestamo
	 */
	public void prestar(Prestamo prestamo) {
		if (esPalindromo(prestamo.getLibro().getIsbn())) {
			throw new PrestamoException(EL_LIBRO_SE_UTILIZA_EN_BIBLIOTECA);
		}
		if (null != prestamo.getNombreUsuario() && !prestamo.getNombreUsuario().isEmpty()) {
			if (esPrestado(prestamo.getLibro().getIsbn())) {
				throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
			} else {
				if (digitosNumericosIsbnSumanMasDe30Dias(prestamo.getLibro().getIsbn())) {
					prestamo.setFechaEntregaMaxima(this.getFechaEntregaLibroPrestado(Calendar.getInstance()));
				}
				this.repositorioPrestamo.agregar(prestamo);
			}
		} else {
			throw new PrestamoException(EL_NOMBRE_USUARIO_ES_REQUERIDO);
		}
	}

	/**
	 * Valida si el libro esta prestado (true) o si no esta prestado (falso)
	 * 
	 * @param isbn
	 * @return
	 */
	public boolean esPrestado(String isbn) {
		Libro libro = this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
		return null != libro ? true : false;
	}

	/**
	 * Valida si el isbn es palindromo (true) o no es palindromo (false)
	 * 
	 * @param isbn
	 * @return
	 */
	public boolean esPalindromo(String isbn) {
		int cantidadCaracteres = isbn.length();
		if (0 == (cantidadCaracteres % 2)) {
			return new StringBuilder(isbn.substring(cantidadCaracteres / 2, isbn.length())).reverse().toString()
					.equals(isbn.substring(0, (cantidadCaracteres / 2)));
		} else {
			return new StringBuilder(isbn.substring((cantidadCaracteres / 2) + 1, isbn.length())).reverse().toString()
					.equals(isbn.substring(0, (cantidadCaracteres / 2)));
		}
	}

	/**
	 * Valida si los digitos numerico del isbn suman mas de 30 (true) o si no suman
	 * mas de 30 (false)
	 * 
	 * @param isbn
	 * @return
	 */
	public boolean digitosNumericosIsbnSumanMasDe30Dias(String isbn) {
		int sumatoria = 0;
		for (int i = 0; i < isbn.length(); i++) {
			if (Character.isDigit(isbn.charAt(i))) {
				sumatoria += Integer.parseInt(String.valueOf(isbn.charAt(i)));
			}
		}
		return sumatoria > 30 ? true : false;
	}

	/**
	 * Se obtiene la fecha 15 dias despues incluyendo la actual y excluyendo los
	 * domingos.
	 * 
	 * @param cal
	 * @return
	 */
	public Date getFechaEntregaLibroPrestado(Calendar cal) {
		for (int i = 0; i < 14; i++) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			if (1 == cal.get(Calendar.DAY_OF_WEEK)) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		return cal.getTime();
	}
}
