package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

	public int somar(int a, int b) {
		return a + b;
	}

	public int subtrair(int a, int b) {
		return a - b;
	}

	public int divide(int a, int b) throws NaoPodeDividirPorZeroException {
		if (b == 0) {
			throw new NaoPodeDividirPorZeroException("N�o � poss�vel a divis�o por zero.");
		}
		return a / b;
	}

	public int divide(String a, String b) {
		return Integer.valueOf(a) / Integer.valueOf(b);
	}

	public void imprime() {
		System.out.println("Passei aqui");
	}

	public static void main(String[] args) {
		new Calculadora().divide("a", "b");
	}

}