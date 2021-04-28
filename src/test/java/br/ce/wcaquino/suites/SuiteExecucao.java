package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;
import br.ce.wcaquino.servicos.ParameterizerTest;

//@RunWith(Suite.class)
@SuiteClasses(value = { 
		CalculadoraTest.class,
		LocacaoServiceTest.class,
		ParameterizerTest.class })
public class SuiteExecucao {
	// Remova se puder!

	@BeforeClass
	public static void beforeClass() {
//		Pra saber que podemos colocar isso aqui
	}

	@AfterClass
	public static void afterClass() {
//		Pra saber que podemos colocar isso aqui
	}
}
