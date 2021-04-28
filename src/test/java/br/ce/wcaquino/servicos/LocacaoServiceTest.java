package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.novoFilme;
import static br.ce.wcaquino.builder.LocacaoBuilder.novaLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.novoUsuario;
import static br.ce.wcaquino.matchers.MeusMatchers.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import br.ce.wcaquino.dao.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

//@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocacaoService.class, DataUtils.class })
public class LocacaoServiceTest {

	@Rule
	public ErrorCollector coletor = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private Locacao locacao;

	@InjectMocks
	public LocacaoService service;

	@Mock
	public SPCService spc;

	@Mock
	public LocacaoDao dao;

	@Mock
	public EmailService email;

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
//		email = Mockito.mock(EmailService.class);
//		service.setEmailService(email);
	}

	@After
	public void afterEach() {
	}

	@BeforeClass
	public static void beforeClass() {
	}

	@AfterClass
	public static void afterClass() {
	}

	@Test
	public void DeveAlugarFilmeComSucesso() throws Exception {
//		Assume.assumeFalse(DataUtils.verificarDiaSemana(new java.util.Date(), Calendar.SUNDAY));

		// cenario
		whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(31, 12, 2020));

		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filme = new ArrayList<Filme>();
		filme.add(new Filme("Filme 1", 2, 5.0));

		// acao
		locacao = service.alugarFilmes(usuario, filme);

		// verificacao
		coletor.checkThat(locacao.getFilmes().size(), is(1));
		coletor.checkThat(locacao.getFilmes().get(0).getPrecoLocacao(), is(5.0));
//		coletor.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
//		coletor.checkThat(locacao.getDataRetorno(), ehHoje());
	}

	@Test
	public void testePrimeiraAula2() throws Exception {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filme = new ArrayList<Filme>();
		filme.add(new Filme("Filme 1", 2, 5.0));

		// acao
		locacao = service.alugarFilmes(usuario, filme);

		// verificacao
		coletor.checkThat(locacao.getFilmes().size(), is(1));
		assertThat(filme.get(0).getEstoque(), CoreMatchers.anything());
	}

//	Maneira Elegante de testar uma exceção esperada
	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExcecaoAoAlugarFilmeSemEstoqueELEGANTE() throws Exception {

		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		// acao
		locacao = service.alugarFilmes(usuario, Arrays.asList(filme));

	}

//	Maneira Robusta de testar uma exceção esperada
	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemEstoqueROBUSTA() {

		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		// acao
		try {
			locacao = service.alugarFilmes(usuario, Arrays.asList(filme));
			Assert.fail("Deveria ter lançado uma exceção.");
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), is("Sem filme no estoque"));
		}
	}

//	Maneira Elegante de testar uma exceção esperada
	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemEstoqueNOVA() throws Exception {

		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		exception.expect(Exception.class);
		exception.expectMessage("Sem filme no estoque");
		// acao
		locacao = service.alugarFilmes(usuario, Arrays.asList(filme));

	}

	@Test
	public void deveLancarExcecaoQuandoOUsuarioForNulo() throws FilmeSemEstoqueException {

		// cenario
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// acao
		try {
			locacao = service.alugarFilmes(null, Arrays.asList(filme));
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario vazio"));
		}

	}

	@Test
	public void deveLancarExcecaoQuandoOFilmeForNulo() throws FilmeSemEstoqueException, LocadoraException {

		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filme = new ArrayList<Filme>();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// acao
		locacao = service.alugarFilmes(usuario, filme);
		List vazia = new ArrayList();

		assertThat(locacao.getFilmes().size(), is(0));
	}

	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws FilmeSemEstoqueException, LocadoraException {

		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		exception.expect(FilmeSemEstoqueException.class);
		exception.expectMessage("Sem filme no estoque");

		// acao
		locacao = service.alugarFilmes(usuario, Arrays.asList(filme));
	}

	@Test
	public void aplicarDescontoDe25_3Filmes() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 10, 4.0);

		locacao = service.alugarFilmes(usuario, Arrays.asList(filme, filme, filme));

		assertThat(locacao.getValor(), is(11.0));
	}

	@Test
	public void aplicarDescontoDe50_4Filmes() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 10, 4.0);

		locacao = service.alugarFilmes(usuario, Arrays.asList(filme, filme, filme, filme));

		assertThat(locacao.getValor(), is(13.0));

	}

	@Test
	public void aplicarDescontoDe75_5Filmes() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 10, 4.0);

		locacao = service.alugarFilmes(usuario, Arrays.asList(filme, filme, filme, filme, filme));

		assertThat(locacao.getValor(), is(14.0));

	}

	@Test
	public void aplicarDescontoDe100_6Filmes() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 10, 4.0);

		locacao = service.alugarFilmes(usuario, Arrays.asList(filme, filme, filme, filme, filme, filme));

		assertThat(locacao.getValor(), is(14.0));

	}

	@Test
	public void aplicarDescontoDe100_7Filmes() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 10, 4.0);

		locacao = service.alugarFilmes(usuario, Arrays.asList(filme, filme, filme, filme, filme, filme, filme));

		assertThat(locacao.getValor(), is(18.0));

	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 10, 4.0));

//		whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(1, 1, 2021));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 29);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2017);
		mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

		Locacao retorno = service.alugarFilmes(usuario, filmes);

		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {

		// cenario
		Usuario usuario = novoUsuario("Usuario1").constroi();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 10, 4.0));

		when(spc.possuiNegativacao(any(Usuario.class))).thenReturn(true);

		try {
			service.alugarFilmes(usuario, filmes);

			// verificacao
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário negativado."));
		}

		// verificacao
		verify(spc).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		// cenario
		Usuario usuario = novoUsuario("José Da Silva").constroi();
		List<Filme> filmes = Arrays.asList(novoFilme("filme qualquer").comEstoqueDe(5).comPreco(4.0).constroi());

		List<Locacao> locacoes = Arrays
				.asList(novaLocacao().comOsFilmes(filmes).paraUsuario(usuario).atrasado().constroi());

		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		// acao
		service.notificarAtraso();

		assertThat(locacoes.size(), is(1));
		verify(email, times(1)).notificarAtraso(usuario);
	}

	@Test
	public void naoDeveEnviarEmailParaLocacoesEmDia() {
		// cenario
		List<Filme> filmes = Arrays.asList(novoFilme("filme qualquer").comEstoqueDe(5).comPreco(4.0).constroi());

		Usuario usuario = novoUsuario("José Da Silva").constroi();
		Usuario usuario2 = novoUsuario("João Da Silva").constroi();
		Usuario usuario3 = novoUsuario("Maria Da Silva").constroi();

		List<Locacao> locacoes = Arrays.asList(
				novaLocacao().paraUsuario(usuario).comOsFilmes(filmes).atrasado().constroi(),
				novaLocacao().paraUsuario(usuario2).comOsFilmes(filmes).atrasado().constroi(),
				novaLocacao().paraUsuario(usuario3).comOsFilmes(filmes).naData(new Date()).constroi());

		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		// acao
		service.notificarAtraso();

		verify(email, times(2)).notificarAtraso(any(Usuario.class));
		verify(email, times(1)).notificarAtraso(usuario);
		verify(email, atLeastOnce()).notificarAtraso(usuario2);
		verify(email, never()).notificarAtraso(usuario3);
		verifyNoMoreInteractions(email);
	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {
		// cenario
		Usuario usuario = novoUsuario("Zé da Silva").novoNome("José Da Silva").constroi();
		List<Filme> filmes = Arrays.asList(novoFilme("filme qualquer").comEstoqueDe(5).comPreco(4.0).constroi());

		// verificacao
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com o SPC, fora do ar.");

		when(spc.possuiNegativacao(any(Usuario.class))).thenThrow(new Exception("Falha catastrófica."));

		// acao
		service.alugarFilmes(usuario, filmes);
	}

	@Test
	public void deveProrrogarUmaLocacao() {
		// cenario
		Usuario usuario = novoUsuario("Zé da Silva").constroi();

		List<Filme> filmes = Arrays.asList(novoFilme("Filme1").comEstoqueDe(5).comPreco(4.0).constroi());
		Locacao locacao = novaLocacao().paraUsuario(usuario).comOsFilmes(filmes).constroi();

		// acao
		service.prorogarLocacaoDa(locacao, 10);

		ArgumentCaptor<Locacao> argCaptor = ArgumentCaptor.forClass(Locacao.class);
		verify(dao).salvar(argCaptor.capture());
		Locacao locacaoRetornada = argCaptor.getValue();

		assertThat(locacaoRetornada.getFilmes().get(0).getNome(), is(filmes.get(0).getNome()));
		assertThat(locacaoRetornada.getUsuario(), is(usuario));

	}

}
