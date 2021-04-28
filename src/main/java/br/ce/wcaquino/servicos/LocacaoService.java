package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.dao.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {

	private LocacaoDao dao;
	private SPCService spcService;
	private EmailService email;

	public Locacao alugarFilmes(Usuario usuario, List<Filme> filmes)
			throws FilmeSemEstoqueException, LocadoraException {

		if (usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}

		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Sem filme no estoque");
			}
		}

		boolean negativado;
		try {
			negativado = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new LocadoraException("Problemas com o SPC, fora do ar.");
		}

		if (negativado) {
			throw new LocadoraException("Usuário negativado.");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(Calendar.getInstance().getTime());

		double valorLocacao = 0d;
		for (Filme filme : filmes) {
			valorLocacao += filme.getPrecoLocacao();
		}

		if (filmes.size() >= 3) {
			double desconto = 0.25;
			for (int i = 2; i < totalDeFilmes(filmes); i++) {
				valorLocacao -= filmes.get(i).getPrecoLocacao() * desconto;
				desconto = desconto + 0.25;
			}
		}

		locacao.setValor(valorLocacao);

		// Entrega no dia seguinte
		Date dataEntrega = Calendar.getInstance().getTime();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}

		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		dao.salvar(locacao);

		return locacao;
	}

	public void notificarAtraso() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		for (Locacao locacao : locacoes) {
			if (locacao.getDataRetorno().before(new Date())) {
				email.notificarAtraso(locacao.getUsuario());
			}
		}
	}

	private int totalDeFilmes(List<Filme> filmes) {
		if (filmes.size() > 5) {
			return 6;
		} else {
			return filmes.size();
		}
	}

	public void prorogarLocacaoDa(Locacao antiga, int dias) {
		Locacao locacao = new Locacao();
		locacao.setUsuario(antiga.getUsuario());
		locacao.setFilmes(antiga.getFilmes());
		locacao.setDataLocacao(antiga.getDataLocacao());
		locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		locacao.setValor(antiga.getValor() * dias);
		dao.salvar(locacao);
	}

//	Com as Anotações @Mock, @InjectMocks e a declaração de "MockitoAnnotations.initMocks(this);" no setup
//	da classe de testes não precisaremos mais destes métodos de injeção de dependências
//	
//	public void setEmailService(EmailService email) {
//		this.email = email;
//	}
//
//	public void setLocacaoDao(LocacaoDao dao) {
//		this.dao = dao;
//	}
//
//	public void setSPCService(SPCService spc) {
//		this.spc = spc;
//	}

}