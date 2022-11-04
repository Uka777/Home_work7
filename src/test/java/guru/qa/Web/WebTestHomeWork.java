package guru.qa.Web;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import guru.qa.Web.data.LocaleHM;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.files.DownloadActions.click;

public class WebTestHomeWork {
    @BeforeAll
    static void setUp() {
        Configuration.holdBrowserOpen = true;

    }
    @ValueSource(strings = {"iphone", "dyson"})
    @ParameterizedTest(name = "Checking the search result {0}")
    void testSearchTest(String testData) {
        open("https://technopark.ru/");
        $("#header-search-input-main").setValue(testData);
        $(".header-search-suggests__section").shouldHave(text(testData));
    }
    static Stream<Arguments> testSiteButtonsText() {
        return Stream.of(
                Arguments.of(LocaleHM.EN, List.of("Comparison", "Favourites", "Basket",
                        "Enter")),
                Arguments.of(LocaleHM.RU, List.of("Сравнение", "Избранное", "Корзина",
                        "Войти"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Check buttons names for locale: {0}")
    void testSiteButtonsText(LocaleHM locale, List<String> buttonsTexts) {
        open("https://www.technopark.ru/");
        $$(".language-switcher__item").find(text(locale.name())).click();
        $$(".header-main__indicators").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }
    @EnumSource(LocaleHM.class)
    @ParameterizedTest
    void checkLocaleTest(LocaleHM locale){
        open("https://www.technopark.ru/");
        $$(".language-switcher__item").find(text(locale.name())).shouldBe(visible);

    }

}
