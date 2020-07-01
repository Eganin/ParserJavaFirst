package Java;

import org.jsoup.nodes.Document;// строка с html-текстом

import java.net.URL; // работа с вебстраницой

import org.jsoup.Jsoup; // библиотека для парсинга
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.util.regex.Matcher;  // re - match
import java.util.regex.Pattern; // pattern для re-выражений


public class Parser {

    private static Document getPage() throws IOException {
        // Метод отвечает за скачивание страницы
        String url = "https://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);// объект для парсинга страницы
        return page; // получаем html-страницу
    }

    // \d - символ
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}"); // pattern from re-extends // с экранированием - \\


    private static String getDateFromString(String stringDate) throws Exception {// выброс исключения при ошибке
        // с помощью регулярного выражения возьмем из строки число
        Matcher matcher = pattern.matcher(stringDate);// выбираем из строки pattern

        if (matcher.find()) {
            // если совпадение с re-выражением есть
            return matcher.group(); // return value
        } else {
            throw new Exception("В строке не присутсвует дата"); // создание собственного исключения
        }
    }

    private static int printFourValue(Elements values, int Index) {
        // Данный метод распарсивает Elements - values
        int iterationCount = 4;
        if (Index == 0) {
            Element valueLn = values.get(3); // забираем по индексу
            boolean isMorning = valueLn.text().contains("Утро");// если значение содержит Утро
            if (isMorning) {
                iterationCount = 3;
            }
            boolean isDay = valueLn.text().contains("День");
            if (isDay) {
                iterationCount = 2;
            }
            boolean isEvening = valueLn.text().contains("Вечер");
            if (isEvening) {
                iterationCount = 1;
            }

            boolean isNight = valueLn.text().contains("Ночь");
            if (isNight) {
                iterationCount = 0;
            }

            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(Index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "      ");
                }
                System.out.println();
            }
            return iterationCount;
        } else {

            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(Index + i); // забираем по индексу
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "      ");
                }
                System.out.println();
            }
        }
        return iterationCount;
    }


    public static void main(String[] args) {
        try {
            Document page = getPage();
            // css query languarge -  используется при запросе к элементам
            // получение таблицы с погодой
            Element tableWth = page.select("table[class=wt]").first();// выборка из html нужного первого элемента
            Elements names = tableWth.select("tr[class=wth]"); // выборка списка из html
            Elements values = tableWth.select("tr[valign=top]");
            int Index = 0;
            for (Element name : names) {
                String date = name.select("th[id=dt]").text(); // преобразовать в text
                //String datetime = date.split(" ")[0]; // получаем только дату
                String datetime = getDateFromString(date);
                System.out.println(datetime + "    Явления    Температура    Давление    Влажность     Ветер");
                int iterationCount = printFourValue(values, Index);
                Index += iterationCount;
            }

        } catch (IOException e) {
            System.out.println("Error: Ошибка подключения к сайту");
        } catch (Exception e) {
            System.out.println("Произошла какая-то ошибка");
        }
    }
}
