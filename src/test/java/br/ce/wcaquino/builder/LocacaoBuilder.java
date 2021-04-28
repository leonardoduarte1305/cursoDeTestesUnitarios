package br.ce.wcaquino.builder;

import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoBuilder {

	private Locacao locacao;

	private LocacaoBuilder() {
	}

	public static LocacaoBuilder novaLocacao() {
		LocacaoBuilder locacaoBulder = new LocacaoBuilder();
		locacaoBulder.locacao = new Locacao();
		return locacaoBulder;
	}

	public LocacaoBuilder naData(Date dataDaLocacao) {
		locacao.setDataLocacao(dataDaLocacao);
		return this;
	}

	public LocacaoBuilder paraUsuario(Usuario usuario) {
		locacao.setUsuario(usuario);
		return this;
	}

	public LocacaoBuilder atrasado() {
		locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
		locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));
		return this;
	}

	public Locacao constroi() {
		if (locacao.getDataLocacao() == null) {
			locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(0));
		}

		if (locacao.getDataRetorno() == null) {
			locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(+2));
		}

		double valorTotalLocacao = 0;
		for (Filme filme : locacao.getFilmes()) {
			valorTotalLocacao += filme.getPrecoLocacao();
		}
		locacao.setValor(valorTotalLocacao);

		return this.locacao;
	}

	public LocacaoBuilder comOsFilmes(List<Filme> filmes) {
		locacao.setFilmes(filmes);
		return this;
	}
}
