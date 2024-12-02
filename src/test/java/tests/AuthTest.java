package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;

public class AuthTest extends BaseTest{

    static Stream<Arguments> dataForPositiveTests() {

        return Stream.of(
                Arguments.of("standard_user", "secret_sauce"),
                Arguments.of("problem_user", "secret_sauce"),
                Arguments.of("error_user", "secret_sauce"),
                Arguments.of("performance_glitch_user", "secret_sauce"),
                Arguments.of("visual_user", "secret_sauce")
        );
    }

    static Stream<Arguments> dataForNegativeTests() {

        return Stream.of(
                Arguments.of("", "secret_sauce", "Epic sadface: Username is required"),
                Arguments.of("standard_user", "", "Epic sadface: Password is required"),
                Arguments.of("", "", "Epic sadface: Username is required"),
                Arguments.of("qwerty", "abcde",
                        "Epic sadface: Username and password do not match any user in this service"),
                Arguments.of("locked_out_user", "secret_sauce",
                        "Epic sadface: Sorry, this user has been locked out.")
        );
    }

    @MethodSource("dataForPositiveTests")
    @ParameterizedTest(name = "User should be authorized entering valid data")
    void userShouldBeAuthorizedEnteringValidData(String login, String password){
        open("https://www.saucedemo.com/");
        $("#user-name").setValue(login);
        $("#password").setValue(password).pressEnter();
        Assertions.assertTrue($("#inventory_container").exists());
        closeWebDriver();
    }

    @MethodSource("dataForNegativeTests")
    @ParameterizedTest(name = "User shouldn't be authorized entering invalid data")
    void userShouldNotBeAuthorizedEnteringInvalidData(String login, String password, String errorMessage){
        open("https://www.saucedemo.com/");
        $("#user-name").setValue(login);
        $("#password").setValue(password).pressEnter();
        String textOfMessage = $x("//h3[@data-test = 'error']").getText();
        Assertions.assertEquals(errorMessage, textOfMessage, "Error!");
    }
}
