package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	private Calculadora calc;

	@Before
	public void setup() {
		calc = new Calculadora();
	}

	@Test
	public void deveSomarDoisNumeros() {
		// ação
		int resultado = calc.somar(9, 10);

		// avaliação
		assertEquals(19, resultado);
	}

	@Test
	public void deveSubtrairDoisNumeros() {
		// cenário
		Calculadora calc = new Calculadora();

		// ação
		int resultado = calc.subtrair(9, 10);

		// avaliação
		assertEquals(-1, resultado);
	}

	@Test
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		exception.expect(NaoPodeDividirPorZeroException.class);
		exception.expectMessage("Não é possível a divisão por zero.");

		calc.divide(9, 0);
	}

	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		// ação
		int resultado = calc.divide(10, 5);

		assertThat(resultado, is(2));
	}
}
