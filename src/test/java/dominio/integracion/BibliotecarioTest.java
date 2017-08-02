package dominio.integracion;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.Prestamo;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {

	private static final String CRONICA_DE_UNA_MUERTA_ANUNCIADA = "Cronica de una muerta anunciada";
	private static final String USUARIO = "CEIBA Software";

	private SistemaDePersistencia sistemaPersistencia;

	private RepositorioLibro repositorioLibros;
	private RepositorioPrestamo repositorioPrestamo;

	@Before
	public void setUp() {

		sistemaPersistencia = new SistemaDePersistencia();

		repositorioLibros = sistemaPersistencia.obtenerRepositorioLibros();
		repositorioPrestamo = sistemaPersistencia.obtenerRepositorioPrestamos();

		sistemaPersistencia.iniciar();
	}

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void prestarLibroTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		Prestamo prestamo = new Prestamo(libro);
		prestamo.setNombreUsuario(USUARIO);
		blibliotecario.prestar(prestamo);

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Assert.assertNotNull(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn()));

	}

	@Test
	public void prestarLibroNoDisponibleTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		Prestamo prestamo = new Prestamo(libro);
		prestamo.setNombreUsuario(USUARIO);
		blibliotecario.prestar(prestamo);
		try {

			blibliotecario.prestar(prestamo);
			fail();

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE, e.getMessage());
		}
	}

	@Test
	public void prestarLibroConIsbnPalindromoTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).conIsbn("12321").build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		try {
			Prestamo prestamo = new Prestamo(libro);
			blibliotecario.prestar(prestamo);

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_SE_UTILIZA_EN_BIBLIOTECA, e.getMessage());
		}
	}

	@Test
	public void prestarLibroConIsbnNOPalindromoTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).conIsbn("344445").build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		try {
			Prestamo prestamo = new Prestamo(libro);
			prestamo.setNombreUsuario(USUARIO);
			blibliotecario.prestar(prestamo);

		} catch (PrestamoException e) {
			fail();
		}
	}

	@Test
	public void prestarLibroSINNombreUsuarioTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		try {
			Prestamo prestamo = new Prestamo(libro);
			blibliotecario.prestar(prestamo);

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_NOMBRE_USUARIO_ES_REQUERIDO, e.getMessage());
		}
	}
	
	@Test
	public void prestarLibroConIsbnDigitosNumericosMenorA30Test() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conIsbn("Q12F5S5").build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		try {
			Prestamo prestamo = new Prestamo(libro);
			prestamo.setNombreUsuario(USUARIO);
			blibliotecario.prestar(prestamo);
			Assert.assertNull(prestamo.getFechaEntregaMaxima());
		} catch (PrestamoException e) {
			fail();
		}
	}
	
	@Test
	public void prestarLibroConIsbnDigitosNumericosMayorA30Test() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conIsbn("Q12F59999S5").build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		try {
			Prestamo prestamo = new Prestamo(libro);
			prestamo.setNombreUsuario(USUARIO);
			blibliotecario.prestar(prestamo);
			Assert.assertNotNull(prestamo.getFechaEntregaMaxima());
		} catch (PrestamoException e) {
			fail();
		}
	}
}
