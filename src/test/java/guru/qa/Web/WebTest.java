package guru.qa.Web;
import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import guru.qa.Web.data.Locale;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class WebTest {
    //["JUnit" - фреймворк для автоматизированного тестирования", состоит из нескольких библиотек,также содержит Assertions]
    //["@Test- аннотация для обозначения теста"]
    //[Assertions.assertEquals("Str", "Str") ; //для объектов Equals сравнивает адреса памяти,
    // а для string - проверяет содержимое строчек]
    //["@Disabled(в скобках можно оставить комментарий) - аннотация для того чтобы тест не запускался", вместо закомментирования]
    //["@DisplayName(название) - аннотация для именования теста"]

      //[Параметизированный тест - это тест, сценарий которого одинаковый для разных входных параметров]
      @ValueSource(strings = {"Selenide","Junit"}) //ValueSource работает только со String и примитивными типами данных
      //сколько в массиве строк, столько раз будет запущен тест
      @ParameterizedTest(name = "Проверка числа результатов поиска в Яндексе для запроса {0}")//{0} -т.к 1 параметр
      //нужно обозначить тестовые данные с помощью аннотации "ValueSource"
      void yandexSearchCommonTest(String testData){
          open("https://ya.ru");
          $("#text").setValue(testData);
          $("button[type='submit']").click();
          $$("li.serp-item").shouldHave(CollectionCondition.size(10))
                  .first()//проверка первого результат в поиске
                  .shouldHave(text(testData));
      }

    @CsvSource({//аналогично аннотации ValueSource, но несколько параметров,
            // если запятая это часть текста, то тогда delimiter = '|' - можно использовать как разделитель
            "Selenide","Selenide - это фреймворк для автоматизированного тестирования",
            "JUnit, JUnit.org"
    }
    )
    @ParameterizedTest(name = "Проверка числа результатов поиска в Яндексе для запроса {0}")//{0} -т.к 1 параметр
        //нужно обозначить тестовые данные с помощью аннотации "ValueSource"
    void yandexSearchCommonTest(String searchQuery, String expectedText) {
        open("https://ya.ru");
        $("#text").setValue(searchQuery);
        $("button[type='submit']").click();
        $$("li.serp-item").shouldHave(CollectionCondition.size(10))
                .first()//проверка первого результат в поиске
                .shouldHave(text(expectedText));
    }
    //датапровайдер
    static Stream<Arguments> selenideSiteButtonsText(){//stream - это набор аргументов, Arguments - это возваращаемое значение
          return Stream.of(//пишем return - так как есть возвращаемое значение
                  Arguments.of(Locale.EN, List.of("Quick Start", "Docs", "FAQ",
                          "Blog", "Javadoc", "Users", "Quotes")),//набор аргументов для одного запуска теста
                  Arguments.of(Locale.RU, List.of("С чего начать?", "Док", "ЧАВО",
                          "Блог", "Javadoc", "Пользователи", "Отзывы"))
          );
    }
    @MethodSource //возвращает набор параметров произвольного типа
    //если имя аннотации и датапровайдера не совпадает, то нужно прописать имя в ()
    @ParameterizedTest(name = "Check buttons names for locale: {0}")
    void selenideSiteButtonsText(Locale locale, List<String> buttonsTexts){
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).click();//метод name возвращает EN,RU
        $$(".main-menu-pages a").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));

    }
    @EnumSource(Locale.class)
    @ParameterizedTest
    void checkLocaleTest(Locale locale){
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).shouldBe(visible);

    }
}
