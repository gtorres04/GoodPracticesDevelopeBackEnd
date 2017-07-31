package dominio;

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

	public void prestar(Prestamo prestamo) {
		if(esPalindromo(prestamo.getLibro().getIsbn())) {
			throw new PrestamoException(EL_LIBRO_SE_UTILIZA_EN_BIBLIOTECA);
		}
		if(null != prestamo.getNombreUsuario() && !prestamo.getNombreUsuario().isEmpty()) {
			if(esPrestado(prestamo.getLibro().getIsbn())){
				throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
			}else {
				this.repositorioPrestamo.agregar(prestamo);
			}
			
		}else {
			throw new PrestamoException(EL_NOMBRE_USUARIO_ES_REQUERIDO);
		}
	}

	public boolean esPrestado(String isbn) {
		Libro libro = this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
		return null != libro ? true : false;
	}

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

}
