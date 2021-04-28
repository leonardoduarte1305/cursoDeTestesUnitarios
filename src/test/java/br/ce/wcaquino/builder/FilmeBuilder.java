package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

	private Filme filme;

	private FilmeBuilder() {
	}

	public static FilmeBuilder novoFilme(String nome) {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		return builder;
	}

	public FilmeBuilder comEstoqueDe(Integer estoque) {
		filme.setEstoque(estoque);
		return this;
	}

	public FilmeBuilder comPreco(double preco) {
		filme.setPrecoLocacao(preco);
		return this;
	}

	public Filme constroi() {
		return filme;
	}

}
