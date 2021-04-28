package br.ce.wcaquino.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {

	private Integer dataEsperada;

	public DiaSemanaMatcher(Integer diaSemana) {
		this.dataEsperada = diaSemana;
	}

	public void describeTo(Description description) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_WEEK, dataEsperada);
		String dataPorExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
		description.appendText(dataPorExtenso);
	}

	@Override
	protected boolean matchesSafely(Date dataAtual) {
		return DataUtils.verificarDiaSemana(dataAtual, dataEsperada);
	}

}
