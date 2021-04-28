package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.dao.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class ParameterizerTest {

	@Parameter(value = 0)
	public List<Filme> filmes;

	@Parameter(value = 1)
	public double valorLocacao;

	@Parameter(value = 2)
	public String string;

	@InjectMocks
	private LocacaoService service;

	@Mock
	private LocacaoDao dao;

	@Mock
	private SPCService spc;

	@Before
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);

//		Com as Anotações @Mock, @InjectMocks e a declaração de "MockitoAnnotations.initMocks(this);" neste setup
//		não precisaremos mais destas injeções de dependências
//	
//		service = new LocacaoService();
//		dao = Mockito.mock(LocacaoDao.class);
//		service.setLocacaoDao(dao);
//		spc = Mockito.mock(SPCService.class);
//		service.setSPCService(spc);
	}

	static Filme filme1 = new Filme("Filme1", 3, 4.0);
	static Filme filme2 = new Filme("Filme2", 3, 4.0);
	static Filme filme3 = new Filme("Filme3", 3, 4.0);
	static Filme filme4 = new Filme("Filme4", 3, 4.0);
	static Filme filme5 = new Filme("Filme5", 3, 4.0);
	static Filme filme6 = new Filme("Filme6", 3, 4.0);
	static Filme filme7 = new Filme("Filme7", 3, 4.0);

	@Parameters(name = "Teste {index} = {2}")
	public static List<Object[]> getParametros() {
		return Arrays.asList(new Object[][] { { Arrays.asList(filme1, filme2), 8.0, "2 Filmes: 0%" },
				{ Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes: 100%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 Filmes: 0%" } });
	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");

		// acao
		Locacao resultado = service.alugarFilmes(usuario, filmes);

		// verificacao
		assertThat(resultado.getValor(), is(valorLocacao));
	}
}
