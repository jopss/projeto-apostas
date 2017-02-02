package com.jopss.apostas.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Years;

/**
 * Classe de calculo de diferenca entre datas
 */
public final class DateUtilsApostas {

	public static final int SEGUNDO = 1;
	public static final int MINUTO = 2;
	public static final int HORA = 3;
	public static final int DIA = 4;
	public static final int SEMANA = 5;
	public static final int MES = 6;
	public static final int ANO = 7;

	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        private static final DateFormat dfComplete = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static final DateFormat dfCompleteWithoutSeconds = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	private static final Pattern datePattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{2}(\\d{2})?(\\s\\d{2}\\:\\d{2}(\\:\\d{2})?)?)");
	
	/**
	 * Arredonta a data desconsiderando a hora, minutos, segundos e milisegundos. Exemplo: 14/10/2012 10:11:12 -> 14/10/2012 00:00:00.
	 * @param data {@link Date}
	 * @return {@link Date}
	 */
	public static Date arredondaDataZerandoHora(Date data) {
		return DateUtils.truncate(data, Calendar.DAY_OF_MONTH);
	}

	/**
	 * Arredonta a data desconsiderando a hora, minutos, segundos e milisegundos. Exemplo: 14/10/2012 10:11:12 -> 14/10/2012 23:59:59.
	 * @param data {@link Date}
	 * @return {@link Date}
	 */
	public static Date arredondaDataComMaximaHora(Date data) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 23:59:59");
		DateFormat dateFormatTransf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                        return dateFormatTransf.parse(dateFormat.format(data));
                } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                }
	}
        
        /**
	 * Retorna a data desconsiderando de acordo com o formato passado
	 * @param data {@link Date}
	 * @return {@link Date}
	 */
	public static String retornaDataFormatada(Date data, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.format(data);
	}

	/**
	 * Retorna a diferenca em dias entre duas datas, desconsiderando horas, minutos e unidades menores
	 * <p>
	 * Caso a data inicial for maior que a data final a diferenca sera negativa
	 * </p>
	 * 
	 * @param dataInicial Data Inicial
	 * @param dataFinal Data Final
	 * @return Quantidade de dias entre as duas datas
	 */
	public static int getDiffDays(Date dataInicial, Date dataFinal) {
		Long dias = ((dataFinal.getTime() - dataInicial.getTime()) / DateUtils.MILLIS_PER_DAY);
		return dias.intValue();
	}

	/**
	 * Retorna a diferenca em horas entre duas datas
	 * <p>
	 * Caso a data inicial for maior que a data final a diferenca sera negativa
	 * </p>
	 * 
	 * @param dataInicial Data Inicial
	 * @param dataFinal Data Final
	 * @return Quantidade de dias entre as duas datas
	 */
	public static int getDiffHours(Date dataInicial, Date dataFinal) {
		Date diff = new Date();
		diff.setTime(dataFinal.getTime() - dataInicial.getTime());
		return (int) Math.floor(diff.getTime() / (1000 * 60 * 60));
	}

	/**
	 * @param data
	 * @return {@link String} em formato ptBR
	 */
	public static String FormatDate_ptBR(Date data) {
		return df.format(data);
	}
	
	/**
	 * Format uma data de acordo com o padrao passado
	 * 
	 * @param data
	 * @param pattern
	 * @return String
	 * @see {@link SimpleDateFormat}
	 */
	public static String FormatData_Pattern(Date data, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(data);
	}

	/**
	 * Soma a uma data (dataInicio) um tempo especificado por valor e unidade .Ex: Date dataAtualizada = DateUtilsTribunais.somaData(new Date(), 2, DateUtilsTribunais.MES); <br/>
	 * Pode-se subtrair uma data, passando o valor negativo.
	 * 
	 * @param dataInicio
	 * @param valor
	 * @param unidade
	 * @return {@link Date}
	 */
	public static Date somaSubtraiData(Date data, int valor, int unidade) {
		Date dataReturn = (Date) data.clone();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dataReturn);

		if (unidade == DateUtilsApostas.SEGUNDO) {
			calendar.add(Calendar.SECOND, valor);
		} else if (unidade == DateUtilsApostas.MINUTO) {
			calendar.add(Calendar.MINUTE, valor);
		} else if (unidade == DateUtilsApostas.HORA) {
			calendar.add(Calendar.HOUR_OF_DAY, valor);
		} else if (unidade == DateUtilsApostas.DIA) {
			calendar.add(Calendar.DAY_OF_YEAR, valor);
		} else if (unidade == DateUtilsApostas.SEMANA) {
			calendar.add(Calendar.WEEK_OF_YEAR, valor);
		} else if (unidade == DateUtilsApostas.MES) {
			calendar.add(Calendar.MONTH, valor);
		} else if (unidade == DateUtilsApostas.ANO) {
			calendar.add(Calendar.YEAR, valor);
		}

		dataReturn.setTime(calendar.getTimeInMillis());
		return dataReturn;
	}

	/**
	 * Compara duas datas por dia, ignorando as horas e unidades menores.
	 * <p>
	 * Retorna 0 se são iguais,
	 * </p>
	 * <p>
	 * &lt;0 se data1&lt;data2,
	 * </p>
	 * <p>
	 * e >0 se data1>data2.
	 * </p>
	 * 
	 * @param data1
	 * @param data2
	 * @return int
	 */
	public static int compararDatasPorDia(Date data1, Date data2) {
		Date dataTruncada1, dataTruncada2;

		dataTruncada1 = DateUtilsApostas.arredondaDataZerandoHora(data1);
		dataTruncada2 = DateUtilsApostas.arredondaDataZerandoHora(data2);

		return dataTruncada1.compareTo(dataTruncada2);
	}

	/**
	 * Retorna um Date atraves de uma String no formado dd/MM/yyyy
	 * 
	 * @param data objeto String com a data
	 * @return Date
	 */
	public static Date returnDate(String data) {
		try {
			return df.parse(data);
		} catch (ParseException ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um Date atraves de uma String no formato 'dd/MM/yyyy HH:mm:ss'
	 * ou 'dd/MM/yyyy HH:mm' ou  'dd/MM/yyyy'
	 * 
	 * @param data objeto String com a data ou nulo caso invalido.
	 * @return Date
	 */
	public static Date returnDateComplete(String data) {
                data = StringUtils.trim(data);
                if(ValidatorUtil.isNullOrEmpty(data)){
                        return null;
                }
                
		try {
                        data = data.replaceAll("\\-", "").trim();
			return dfComplete.parse(data);
		} catch (ParseException ex) {
			try {
				return dfCompleteWithoutSeconds.parse(data);
			} catch (ParseException ex2) {
				try {
					return df.parse(data);
				} catch (ParseException ex3) {
					return null;
				}
			}
		}
	}

	public static Date retornaData(String data, DateFormat df) {
		try {
			return df.parse(data);
		} catch (ParseException ex) {
			return null;
		}
	}

	/**
	 * Com a data e a unidade especifica, retorna um inteiro representando somente o DIA, MES ou ANO.
	 * 
	 * @param data Date referente
	 * @param unidade atributos estaticos desta classe, sendo poss�vel DIA, MES ou ANO.
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Integer retornaUnidade(Date data, int unidade) throws IllegalArgumentException {
		Calendar cale = GregorianCalendar.getInstance();
		cale.setTime(data);

		if (unidade == DateUtilsApostas.DIA) {
			return cale.get(Calendar.DAY_OF_MONTH);
		} else if (unidade == DateUtilsApostas.MES) {
			return cale.get(Calendar.MONTH) + 1;
		} else if (unidade == DateUtilsApostas.ANO) {
			return cale.get(Calendar.YEAR);
		} else {
			throw new IllegalArgumentException("Unidade especificada nao valida. Escolha: DIA, MES ou ANO");
		}

	}

	/**
	 * @return true se a data inicial é menor que a data final, false caso contrario. Se alguma data informada for nula, retorna false.
	 */
	public static boolean isDataInicialFinalValidas(Date dataInicial, Date dataFinal) {
		if (dataInicial != null && dataFinal != null) {
			return DateUtilsApostas.compararDatasPorDia(dataInicial, dataFinal) <= 0;
		}

		return false;
	}

	/**
	 * Retorna lista com os proximos anos a partir do atual.
	 * 
	 * @param numero de proximos anos a serem retornados.
	 * @return List<String>
	 */
	public static List<String> retornarListaProximosAnos(int qtdProximosAnos) {

		LocalDate dataAtual = new LocalDate();
		LocalDate dataFinal = dataAtual.withFieldAdded(DurationFieldType.years(), qtdProximosAnos);

		List<String> retorno = new ArrayList<String>();
		int anos = Years.yearsBetween(dataAtual, dataFinal).getYears();
		for (int i = 0; i < anos; i++) {
			retorno.add("" + dataAtual.withFieldAdded(DurationFieldType.years(), i).getYear());
		}

		Collections.sort(retorno);

		return retorno;
	}

	/**
	 * Retorna lista com os nomes de todos os meses.
	 * 
	 * OBS: nesta versao atual, no retorno de getMonths, o ultimo item sempre era vazio. Por conta disso pega-se todos os dados do array menos o ultimo. 
	 * 
	 * @return List<String>
	 */
	public static List<String> retornarListaMesesExtenso() {
		return Arrays.asList(new DateFormatSymbols().getMonths()).subList(0, 12);
	}
	
	public static boolean isPast(Date compare){
		return new Date().compareTo(compare) < 0;
	}
	
	public static Date appendTime(Date date){
		if(date!=null){
			DateTime dt = new DateTime(date);
			DateTime ref = new DateTime();
			
			dt = dt.withHourOfDay(ref.getHourOfDay());
			dt = dt.withMinuteOfHour(ref.getMinuteOfHour());
			dt = dt.withSecondOfMinute(ref.getSecondOfMinute());
			return dt.toDate();
		}
		return null;
	}
	
	public static List<DateTime> interval(DateTime startDate, DateTime endDate){
		List<DateTime> ret = new ArrayList<>();
		int days = Days.daysBetween(startDate, endDate).getDays();
		if(days == 0){
			ret.add( endDate );
		}else{
			for (int i=1; i <= days; i++) {
			    ret.add( startDate.plusDays(i) );
			}
		}
		return ret;
	}
        
	public static String extractFirstDate(String text) {
		Matcher matcher = datePattern.matcher(text);
		if(matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
}
