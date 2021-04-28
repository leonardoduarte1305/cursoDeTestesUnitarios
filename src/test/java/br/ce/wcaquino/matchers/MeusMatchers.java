package br.ce.wcaquino.matchers;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;

public class MeusMatchers {

	public static DiaSemanaMatcher caiEm(Integer dataEsperada) {
		return new DiaSemanaMatcher(dataEsperada);
	}

	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}

	public static DataDiferencaDiasMatcher ehHoje() {
		return new DataDiferencaDiasMatcher(new Date().getDay());
	}

	public static DataDiferencaDiasMatcher ehHojeComDiferencaDeDias(Integer diferenca) {
		Date novaData = adicionarDias(new Date(), diferenca);

		return new DataDiferencaDiasMatcher(novaData.getDay());
	}

}