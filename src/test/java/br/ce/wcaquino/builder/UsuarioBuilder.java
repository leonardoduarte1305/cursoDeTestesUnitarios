package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {

	private Usuario usuario;

	public UsuarioBuilder() {

	}

	public static UsuarioBuilder novoUsuario(String nomeDoUsuario) {
		UsuarioBuilder builder = new UsuarioBuilder();
		builder.usuario = new Usuario(nomeDoUsuario);
		return builder;
	}

	public UsuarioBuilder novoNome(String nome) {
		usuario.setNome(nome);
		return this;
	}

	public Usuario constroi() {
		return this.usuario;
	}
}
