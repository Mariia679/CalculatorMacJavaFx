package sample.controller;

import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.*;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;
import sample.Main;

import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

import static java.lang.Math.abs;
import static javafx.scene.input.KeyCode.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.GuiTest.find;

public class CalculatorAppTest {

    private final FxRobot robot = new FxRobot();

    private static final Map<String, DataFromButton> dataFromButtons = new HashMap<>();

    static {
        dataFromButtons.put("0", new DataFromButton(DIGIT0, "zero"));
        dataFromButtons.put("1", new DataFromButton(DIGIT1, "one"));
        dataFromButtons.put("2", new DataFromButton(DIGIT2, "two"));
        dataFromButtons.put("3", new DataFromButton(DIGIT3, "three"));
        dataFromButtons.put("4", new DataFromButton(DIGIT4, "four"));
        dataFromButtons.put("5", new DataFromButton(DIGIT5, "five"));
        dataFromButtons.put("6", new DataFromButton(DIGIT6, "six"));
        dataFromButtons.put("7", new DataFromButton(DIGIT7, "seven"));
        dataFromButtons.put("8", new DataFromButton(DIGIT8, "eight"));
        dataFromButtons.put("9", new DataFromButton(DIGIT9, "nine"));
        dataFromButtons.put(".", new DataFromButton(PERIOD, "point"));

        dataFromButtons.put("AC", new DataFromButton(ESCAPE, "ac"));
        dataFromButtons.put("=", new DataFromButton(ENTER, "equal"));
        dataFromButtons.put("+", new DataFromButton(EQUALS, "addition"));
        dataFromButtons.put("/", new DataFromButton(SLASH, "division"));
        dataFromButtons.put("*", new DataFromButton(DIGIT8, "multiplication"));
        dataFromButtons.put("-", new DataFromButton(MINUS, "subtraction"));
        dataFromButtons.put("±", new DataFromButton(MINUS, "inversion"));
        dataFromButtons.put("%", new DataFromButton(DIGIT5, "percentage"));

        dataFromButtons.put("mc", new DataFromButton(null, "mc"));
        dataFromButtons.put("m-", new DataFromButton(null, "subtractFromMemory"));
        dataFromButtons.put("m+", new DataFromButton(null, "addToMemory"));
        dataFromButtons.put("mr", new DataFromButton(null, "mr"));
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
        new Thread("JavaFX Init Thread") {
            public void run() {
                Main.main(new String[0]);
            }
        }.start();
    }

    @Test
    public void testExpression() throws Exception {
        //test many buttons, that font size is decreasing
        testStyle("11111111111111111111111111111111111111111111111111111");

        //testing button "," test that new button don't add to display
        testApp(". . .", "0,");
        testApp("2 . . .", "2,");
        testApp("2 . . .3", "2,3");
        testApp(". . . 3", "0,3");
        testApp(". . . 3 . . . .", "0,3");
        testApp("12 . . . 399 . . . 55 .", "12,39955");

        testApp("9876543210 + 123456789 / 123456789 * 123456789 - 123456789 ± =", "10123456788");
        testApp("10 / 3 * 3 =", "10");
        testApp("2 + 10 / 3 * 3 =", "12");
        testApp("22 + 22 * 22 + 10 / 3 * 3 =", "516");
        testApp("2 * 10 / 3 * 3 =", "20");
        testApp("10 / 3 *", "3,333333333333333");
        testApp("9 / 3 * 3 =", "9");
        testApp("10 / 3 + 1 =", "4,333333333333333");

        testApp("2 ± + 2 * 2 =", "2");
        testApp("2 + 2 + * 2 ± =", "-8");
        testApp("2 + 2 * 2 ± =", "-2");
        testApp("2 + 2 * 2 ± = =", "4");
        testApp("2 + 2 * 2 ± = = =", "-8");
        testApp("2 + 3 * 7 ± =", "-19");
        testApp("2 + 3 * 7 ± = =", "133");
        testApp("2 + 3 * 7 ± = = =", "-931");
        testApp("2 + 2 ± + * 2 = = = =", "0");
        testApp("2 + 2 * =", "6");
        testApp("2 + 2 * = = =", "24");
        testApp("23 + 12 * / 2 + = = =", "116");

        testApp("2 ± + 3 - 4 / 7 * 9 =", "-4,142857142857143");
        testApp("2 + 2 * 2 =", "6");
        testApp("2 + 2 + * 2 =", "8");
        testApp("2 + 2 + * 2 = = = =", "64");
        testApp("2 + 2 + * 2 + = = = =", "40");
        testApp("2 + 3 - 4 / 7 * 9 =", "-0,1428571428571429");
        testApp("4 ± + 5.5 * + 2.5 =", "4");
        testApp("2 / 5 * 23 ± + 5.5 * + 2.5 =", "-1,2");
    }

    @Test
    public void testMOperation() throws Exception {
        testApp("mc mr", "0");
        testApp("2 m+ mr", "2");
        testApp("10 m+ mr", "12");
        testApp("112 m- mr", "-100");
        testApp("999999 m+ m- mr", "-100");
        testApp("100 m+ 200", "200");
        testApp("300 m- 999", "999");
        testApp("mr", "-300");
        testApp("mr 234", "234");
        testApp("mc 190", "190");
        testApp("mr", "0");

        //test button m+
        testApp("mc mr", "0");
        testApp("22 + 2 = m+ mr", "24");
        testApp("999999 + 999999 - 999999 = m+ mr", "1000023");
        testApp("999999 m+ mr", "2000022");
        testApp("999999 + 1 m+ + 2 m+ mr", "2000025");
        testApp("mc mr", "0");
        testApp("22 + 2 = m+ 2", "2");
        testApp("22 + 2 = m+ 2 + / * + = =", "6");
        testApp("mr", "48");
        testApp("mc mr", "0");

        //test button m-
        testApp("22 + 2 = m- mr", "-24");
        testApp("22 + 2 = m- mr 23", "23");
        testApp("999999 + 999999 - 999999 m+ = m- mr", "-48");
        testApp("mc mr", "0");
        testApp("999999 m- mr", "-999999");
        testApp("999999 m- mr", "-1999998");
        testApp("mc mr", "0");
        testApp("22 + 2 = m- mr 23 + =", "46");
        testApp("mc mr", "0");
        testApp("1234567890 + 2 = m- mr * 23 + ", "-28395061516");
        testApp("mc mr", "0");
        testApp("1234567890 + 2 = m- mr * 23 + =", "-56790123032");
        testApp("mc mr", "0");

        //test with = m+,m-,mc,mr,+,-,/,*,%
        testApp("22 + 2 = m+ 2 + / * + = = + 22 =", "28");
        testApp("22 + 2 = m+ 2 + / * + = 2 + = = + 4 = =", "14");
        testApp("22 + 2 = m+ 2 / * + = = 34", "34");
        testApp("22 + 2 = m- 2 * / * + = =", "6");
        testApp("22 + 2 = m- 8 - / + * = * 8 = + 3 =", "515");
        testApp("22 + 2 = m- 123 / + * = * 8 = + 123 =", "121155");
        testApp("123 / + * = * 8 = + 123 = = = mr", "0");
        testApp("12 + 34 = m+ m- 2 + / * + = = + 22 =", "28");
        testApp("3425 * 3220923 = m- m+ 2 + / * + = 2 + = = + 4 = =", "14");
        testApp("784334 + 23784 = m+ m- mr 2 / * + = = 34", "34");
        testApp("784334 + 23784 = m+ + 2 / * + = = ", "2424360");
        testApp("mc", "0");
        testApp("mr", "0");

        testApp("1000000000 * = m+ mr", "1e18");
        testApp("mc mr", "0");
        testApp("1000000000 * = m+ mr m+ mr", "2e18");
        testApp("mc mr", "0");
    }

    @Test
    public void testOperation() throws Exception {
        //test only operation and equal
        testApp("/ * * / - + / *  = ±", "0");
        testApp("/ * * / - + / * % % = ±", "0");
        testApp("/ / + * - * / - + / *  = ± = = =", "0");
        testApp("/ * - + / + = = - + / *  = ±", "0");
        testApp(". = = = = / * * / - + / *  = ±", "0");
        testApp("......... = = = = / * * / - + / *  = ±", "0");
        testApp("......... = = ..... = = / * * / ..... - + / *  = ±", "0");
        testApp(".0... = = = = / * * / - + / *  = ±", "0");
        testApp("0. = = = = / * * / - + / *  = ±", "0");
        testApp("0.0000000 = = = = / * * / - + / *  = ±", "0");

        //operation with equal in different position
        testApp("/ 2 =", "0");
        testApp("2 = / =", "1");
        testApp("2 = / = =", "0,5");
        testApp("2 = / = = =", "0,25");
        testApp("2 = + - / / = = =", "0,25");
        testApp("2 + = + - / / = = =", "0,0625");

        testApp("9999999999 % =", "99999999,99");
        testApp("9999999999 % = % %", "9999,999999");
        testApp("9999999999 % = % =", "999999,9999");
        testApp("9999999999 % = % % % = =", "99,99999999");
        testApp("9999999999 = % = % % % = =", "99,99999999");
        testApp("9999999999 % % % = % % % = =", "0,009999999999");

        testApp("1 + = = - +", "3");
        testApp("1 / + = = 2 + = ", "4");
        testApp("1 / + = = - + 2 = ", "5");
        testApp("2 + 2 = + 2 =", "6");
        testApp("1 / + = = - + 2 = = ", "7");
        testApp("2 + - / * + 2 = + + - - + 2 =", "6");

        testApp("19 = * =", "361");
        testApp("191919 + = - 2 =", "383836");
        testApp("191919 + + + + + = - - - 2 =", "383836");
        testApp("0 + 0.3 = = = = * 3 = = =", "32,4");
        testApp("000000 + 00000.3 = = = = * 00003 = = =", "32,4");
        testApp("0 . . . . + 0 . . . . 3 = = = = * 000003 . . . . = = =", "32,4");

        testApp("/ 2 = = 2 + 3 = = =", "11");
        testApp("/ 2 = = 2 + 3 =", "5");
        testApp("/ 2 = = 2 / 3 =", "0,6666666666666667");
        testApp("/ 2 = = 2 / 3 = = =", "0,07407407407407407");
        testApp("/ 2 = = 2 / 3 = + 2 = =", "4,666666666666667");
    }

    @Test
    public void testPercentOperation() throws Exception {
        testApp("3 + 3 - 4 + 6 * 2 % =", "2,72");
        testApp("3 % + 3 - 4 + 6 * 2 % =", "-0,25");
        testApp("20 % + 3 % - 4 + 6 * 2 % =", "-3,074");
        testApp("20 % + 3 % - 4 + 6 * 2 %", "0,12");

        testApp("2 * 2 % ", "0,04");
        testApp("2 * % =", "0,08");
        testApp("2 * 2 % =", "0,08");
        testApp("2 * 2 % = =", "0,0032");
        testApp("2 * 2 % = = =", "0,000128");

        testApp("2 ± + % =", "-1,96");
        testApp("2 ± + %", "0,04");
        testApp("2 + % =", "2,04");
        testApp("2 + 2 % =", "2,04");
        testApp("2 + 2 % = =", "2,08");
        testApp("2 + 2 % = = =", "2,12");

        testApp("2 - 2 % = = =", "1,88");
        testApp("2 - % = = =", "1,88");
        testApp("2 - %", "0,04");
        testApp("2 ± - %", "0,04");
        testApp("2 ± - % =", "-2,04");
        testApp("2 ± - + / - %", "0,04");
        testApp("2 ± - 2 % = = =", "-1,88");

        testApp("2 / % =", "50");
        testApp("2 / % = = =", "31250");
        testApp("2 / 2 % = =", "1250");
        testApp("2 / 2 % =", "50");

        testApp("2 % =", "0,02");
        testApp("2 % % =", "0,0002");
        testApp("2 % % = = = =", "0,0002");
        testApp("2 % = = =  % = = = =", "0,0002");
        testApp("2 = = = % = = = = % = =", "0,0002");

        testApp("2 ± % % =", "-0,0002");
        testApp("2 ± % ± % =", "0,0002");
        testApp("2 ± % ± % ± =", "-0,0002");
        testApp("2 ± ± ± % ± % ± =", "-0,0002");
        testApp("1234567899876543 ± ± ± % ± % ± =", "-123456789987,6543");
        testApp("1234567899876543 ± ± ± % ± % ± = = =", "-123456789987,6543");

        testApp("0 % =", "0");
        testApp("0 % ", "0");
        testApp("100 - 10 % ", "10");
        testApp("100 - 10 % =", "90");
        testApp("100 * 10 % =", "1000");
        testApp("100 * + / - % =", "0");
        testApp("100 + - % =", "0");
        testApp("100 ± - % = =", "-300");
        testApp("100 % ± = = =", "-1");
        testApp("10 %", "0,1");

        testApp("23500 / 2 %", "470");
        testApp("23500 / 2 % =", "50");
        testApp("2 / 23500 %", "470");
        testApp("2 / 23500 % =", "0,00425531914893617");
        testApp("9 % % % % % % % ", "0,00000000000009");
        testApp("9 % % % % % % % % ", "9e-16");
        testApp("9 % % % % % % % % = = = = =", "9e-16");
        testApp("9 % % % % % % % % % % % % % % % %  " +
                "% % % % % % % % % % % % % % % % % % % " +
                "% % % % % % % % % % % % % ", "Не определено");
    }

    @Test
    public void testMultiplyOperation() throws Exception {
        testApp("98765432 * 9 =", "888888888");
        testApp("98765432 ± * 9 ± =", "888888888");
        testApp("98765432 ± * 9 =", "-888888888");
        testApp("724387928792 * 724387928792 =", "5,247378713795637e23");
        testApp("724387928792 * 724387928792 * 724387928792 =", "3,80113779807365e35");
        testApp("987654321 * 987654321 =", "9,75461057789971e17");
        testApp("0.987654321 * .987654321 =", "0,975461057789971");

        testApp("1 * 1 =", "1");
        testApp("1 ± * 9 =", "-9");
        testApp("1 * * * 9 =", "9");
        testApp("1 * * * 9 = = = =", "6561");
        testApp("1 + / * 9 =", "9");
        testApp(".1 ± + / - * 9 =", "-0,9");
        testApp(".1 ± ± ± + / - * 9 ± ± =", "-0,9");

        testApp(". + / - * 9 ± ± =", "0");
        testApp(".0 + / - * 9 =", "0");
        testApp("0.0 + / - * 9 =", "0");
        testApp("0 +  * 9 =", "0");
        testApp(". + / - * 9 ± ± = = =", "0");
        testApp("0.0000 + - / * 9 =", "0");
        testApp("0. +  * 9 =", "0");
        testApp("0345 + / * 9 =", "3105");
        testApp("0345 + / * 0009 =", "3105");

        testApp("1000000000 * = = = = =", "1e54");
        testApp("1000000000 * = = = = = = = = = = = =", "1e117");
        testApp("99 * = = = = = = = = = = = = = = = = = = =", "8,179069375972309e39");
        testApp("1000000000 * = = = = = = = = = = = = = = = = = = =", "Не определено");
        testApp("99 * = = = = = = = = = = = = = = = = = = = = = " +
                "= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =" +
                " = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", "Не определено");
        testApp("99999999 * = = = = = = = = = =", "9,999998900000055e87");
        testApp("99999999 * = = = = = = = = = = = = = = = = = = =", "9,99999800000019e159");
        testApp("99999999 * = = = = = = = = = = = = = = = = = = = =", "Не определено");

        testAlertShown();
        assertWindowIsMinimized();
    }

    @Test
    public void testExit() throws Exception {
        final boolean[] invoked = new boolean[1];

        ExitController.instance = new ExitController() {
            @Override
            public void exit() {
                invoked[0] = true;
            }
        };
        //click exit
        WaitForAsyncUtils.waitForFxEvents();
        Button close = find("#close");
        Platform.runLater(close::fire);
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(invoked[0]);
    }

    @Test
    public void testDivideOperation() throws Exception {
        testApp("2 a / 3 =", "0,6666666666666667");
        testApp("2 / 3 =", "0,6666666666666667");
        testApp("10 / 3 =", "3,333333333333333");
        testApp("10 / 3 = =", "1,111111111111111");
        testApp("10 / 3 = = =", "0,3703703703703704");
        testApp("3 / 9 =", "0,3333333333333333");
        testApp("9 / 5 =", "1,8");
        testApp("3 / 3 =", "1");
        testApp("10 / 10 =", "1");
        testApp("19191919191 / 354834693643 =", "0,05408692987137564");

        testApp("2 / / / / ", "2");
        testApp("2 / / / / = = =", "0,25");
        testApp("10 / =", "1");
        testApp("10 / = = =", "0,01");
        testApp("91919191919 / = ", "1");
        testApp("91919191919 / = = =", "1,183552711030181e-22");

        testApp("0 / 2 =", "0");
        testApp("0 / 438974723 ± =", "0");
        testApp("0 ± / 74287482 ± =", "0");
        testApp("00000 ± / 0000743278423 ± =", "0");
        testApp("0000743278423 ± / 0000743278423  =", "-1");
        testApp("438974723 / 438974723 ± =", "-1");
        testApp("438974723 ± / 438974723 =", "-1");
        testApp("7865947546 ± / 7865947546 ± =", "1");
        testApp("828742387 ± / 7865947546 ± =", "0,1053582396975725");

        testApp("0 / = 2", "2");
        testApp("2 / 0 * 2 =", "2");
        testApp("0 / = 1234456789", "1234456789");
        testApp("0 / = 564755", "564755");
        testApp("0 / 0 = 54321", "54321");
        testApp("0 / .0 = 564755", "564755");
        testApp("0 / 0. = 564755", "564755");
        testApp("0 / 0.0000 = 564755", "564755");
        testApp("0 / .00000 = 564755", "564755");

        testApp("2 / 0 =", "Не определено");
        testApp("0 / =", "Не определено");
        testApp("124124 / 0 =", "Не определено");
        testApp("21367 / 0 =", "Не определено");
        testApp("0 / = %", "Не определено");
        testApp("0 / = % % % % %", "Не определено");
        testApp("0 / / = = = = ", "Не определено");
        testApp("3410853457 ± / 0 =", "Не определено");
        testApp("84748375782740336014 ± / . =", "Не определено");
        testApp("2374387246262 ± / .0 =", "Не определено");
        testApp("2374387246262 ± / .000 =", "Не определено");
        testApp("3410853457 ± / 0. =", "Не определено");
        testApp("459574754937 * / 0 . =", "Не определено");
        testApp("9876567493 ± + - * / 0 =", "Не определено");

        testApp("1000000 / = = = = = = = = = = = = = = = = =", "Не определено");
        testApp("1000000 / = = = = = = = = = = = = = = = = = = =", "Не определено");
        testApp("1 / 99999999 = = = = = = = = = = =", "1,000000110000007e-88");
        testApp("1 / 99999999 = = = = = = = = = = = =", "Не определено");
        testApp("1 / 99999999 = = = = = = = = = = = = =", "Не определено");
        testApp("2 / 8888888888 = = = = = = = = = =", "Не определено");
        testApp("2 / 8888888888 = = = = = = = = = = = " +
                "= = = = = = = = = = ", "Не определено");
        testApp("2 / 8888888888 = = = = = = = = = = = " +
                "= = = = = = = = = = +", "Не определено");
        testApp("2 / 8888888888 = = = = = = = = = = = " +
                "= = = = = = = = = = /", "Не определено");
        testApp("2 / 8888888888 = = = = = = = = = = = " +
                "= = = = = = = = = = + / *", "Не определено");
        testApp("2 / 8888888888 = = = = = = = = = = = " +
                "= = = = = = = = = = + / * 2 / 2 =", "1");
        testApp("2 / 8888888888 = = = = = = = = =", "5,773015161583996e-90");

    }

    @Test
    public void testSubtractOperation() throws Exception {
        testApp("2 - 3 =", "-1");
        testApp("9 - 5 =", "4");
        testApp("3 - 9 =", "-6");
        testApp("5 - 5 =", "0");
        testApp("239 - 359 =", "-120");
        testApp("99 - 5 =", "94");
        testApp("35 - 8 =", "27");

        testApp("123456789 ±  * / - 01111 =", "-123457900");
        testApp("00003456 + - 00002 ± =", "3458");
        testApp("000 / - 2 =", "-2");
        testApp("2 - - 0000 =", "2");
        testApp("2 ± - + - 0.00000 =", "-2");
        testApp("2 - / - 0 =", "2");
        testApp("2 ± - * * - 0 =", "-2");
        testApp("0 - 2 ± =", "2");
        testApp("0 - 0002 ± =", "2");

        testApp("99 - 5 ± =", "104");
        testApp("99 - 5 ± ± ± =", "104");
        testApp("99 ± ± - 5 ± ± ± =", "104");
        testApp("35 ± - 8 ± =", "-27");
        testApp("99 + - 5 ± =", "104");
        testApp("35 ± * - 8 ± =", "-27");
        testApp("99 - - 5 ± =", "104");
        testApp("35 ± * / + - 8 ± =", "-27");

        testApp("2.5 - 0.5 =", "2");
        testApp("2.5  / - 0.5 =", "2");
        testApp("123456789.987654321  - 987654321.123456789 =", "-864197531,1358025");
        testApp("987654321.123456789 - 123456789.987654321 =", "864197531,1358025");
        testApp("987654321.123456789 + / * * - 987654321.123456789 ± =", "1975308642,246914");
        testApp("123456789.123456789 ± - / * + + - 123456789.987654321 =", "-246913579,1111111");

        testApp("999 ± - = = = =", "2997");
        testApp("191919 + - 2 =", "191917");
        testApp("191919 + + + + + - - - 2 =", "191917");
        testApp("191919 * * * * / / / - 2 =", "191917");
        testApp("11111 * * * * / / / - = = = = = = = = = = = = = =", "-144443");
    }

    @Test
    public void testAddOperation() throws Exception {
        testApp("1 + 2 =", "3");
        testApp("9 + 5 =", "14");
        testApp("123 + 3 =", "126");
        testApp("123456789 + 987654321 =", "1111111110");
        testApp("724387928792 + 724387928792 =", "1448775857584");
        testApp("724387928792 + 724387928792 + 724387928792 + 724387928792 =", "2897551715168");
        testApp("724387928792 + 724387928792 + 724387928792 + 724387928792 + 724387928792 + 724387928792 =", "4346327572752");

        testApp("123456789 ± + 01111 =", "-123455678");
        testApp("00003456 + 00002 ± =", "3454");
        testApp("000 + 2 =", "2");
        testApp("2 + 0000 =", "2");
        testApp("2 ± + 0.00000 =", "-2");
        testApp("2 + 0 =", "2");
        testApp("2 ± + 0 =", "-2");
        testApp("0 + 2 ± =", "-2");
        testApp("0 + 0002 ± =", "-2");

        testApp("5.6 + 2.7 =", "8,3");
        testApp("0.99 + 0.03 =", "1,02");
        testApp("0.999999999999 + 0.0000000003 =", "1,000000000299");
        testApp("0000.999999999999 + 0000.0000000003 =", "1,000000000299");
        testApp("0.999999999999 + 000.0000000003 =", "1,000000000299");
        testApp("123456789.987654321 + 987654321.123456789 =", "1111111111,111111");
        testApp("1.111111111 + 2.999999999 =", "4,11111111");

        testApp("1.111111111 ± + 2.999999999 =", "1,888888888");
        testApp("1.111111111 ± + 2.999999999 ± ± =", "1,888888888");
        testApp("1.111111111 ± ± + 2.999999999 ± ± =", "4,11111111");
        testApp("1.111111111 + 2.999999999 ± =", "-1,888888888");
        testApp("1.111111111 + 2.999999999 ± ± =", "4,11111111");

        testApp("1 - * / + 2 =", "3");
        testApp("1 - * / + + - + 2 =", "3");
        testApp("1 - * / + + - + 2 =", "3");
        testApp("123456789 * / + + - + 2 =", "123456791");
        testApp("123456789 * / + = =", "370370367");

        testApp("999 ± + = = = =", "-4995");
        testApp("191919 + 2 ± =", "191917");
        testApp("0.99 + 0.03 ± =", "0,96");
        testApp("35 ± + 23 ± =", "-58");

        testApp("2 + + + = = = =", "10");
        testApp("2 + = = = =", "10");
        testApp("2 ± ± ± ± + = = = =", "10");
        testApp("2 ± + = = = =", "-10");

        testApp("1 - * / + + - + 2 ", "2");
        testApp("1123324 + - + 2 ±", "-2");
        testApp("1 - * / + + - + 2 ±", "-2");
        testApp("1 - * / + + - + 2 ±", "-2");
        testApp("2 ± + + + +", "-2");
    }

    @Test
    public void testMouseClickedOnButton() throws Exception {
        testClickMouseButton("AC 12 + 34 - 56 * 78 / 90 = ", "-2,533333333333333");
        testClickMouseButton("AC 1 . . . 2 + 34 - 56 * 78 / 90 = ", "-13,33333333333333");
        testClickMouseButton("AC 1 . . . 2 + 34 - 56 * 78 / 90 = AC", "0");

        testClickMouseButton("AC 9876543210 % % + 36487136 - 374138274 * 93755 / 3890 = ", "-8979834690,108306");
        testClickMouseButton("AC 9876543210 % % + 36487136 - 374138274 * 93755 / 3890 =  % %", "-897983,4690108306");
        testClickMouseButton("AC 9876543210 % %  =", "987654,321");
        testClickMouseButton("AC 9876543210 ± ± ± ± ± ± ± ± % %  = ", "987654,321");

        testClickMouseButton("AC . 00000001", "0,00000001");
        testClickMouseButton("AC . 00000001 ±", "-0,00000001");
        testClickMouseButton("AC . 000 . . . 00001 = = = =", "0,00000001");
        testClickMouseButton("AC . 00000001 % % % % %", "1e-18");

        //test click on mc m+,m-,mr.mc
        testClickMouseButton("AC 2 m+ mr", "2");
        testClickMouseButton("AC 112 m- mr", "-110");
        testClickMouseButton("AC 999999 m+ m- mr", "-110");
        testClickMouseButton("AC 100 m+ 200", "200");
        testClickMouseButton("AC 300 m- 999", "999");
        testClickMouseButton("AC mr", "-310");
        testClickMouseButton("AC mr 234", "234");
        testClickMouseButton("AC mc 190", "190");
        testClickMouseButton("AC mr", "0");
    }

    @Test
    public void testKeyBoard() throws Exception {
        testAppKeyEvent("12 + 34 - 56 * 78 / 90 = ", "-2,533333333333333");
        testAppKeyEvent("1 . . . 2 + 34 - 56 * 78 / 90 = ", "-13,33333333333333");
        testAppKeyEvent("1 . . . 2 + 34 - 56 * 78 / 90 = AC", "0");
        testAppKeyEvent("1 + 2 + 3 + 4 =", "10");

        testAppKeyEvent("9876543210 % % + 36487136 - 374138274 * 93755 / 3890 = ", "-8979834690,108306");
        testAppKeyEvent("9876543210 % % + 36487136 - 374138274 * 93755 / 3890 = % %", "-897983,4690108306");
        testAppKeyEvent(" 9876543210 % %  =", "987654,321");
        testAppKeyEvent(" 9876543210 ± ± ± ± ± ± ± ± % %  = ", "987654,321");
        testAppKeyEvent(" 9876543210 ±  % %  = ", "-987654,321");
        testAppKeyEvent(" 9876543210 ± ± % % % % % % % %  = ", "0,000000987654321");

        testAppKeyEvent(" . 00000001", "0,00000001");
        testAppKeyEvent("", "0");
        testAppKeyEvent(" . 00000001 ±", "-0,00000001");
        testAppKeyEvent(" . 000 . . . 00001 = = = =", "0,00000001");
        testAppKeyEvent(" . 00000001 % % %", "0,00000000000001");
    }

    @Test
    public void testMouseDragged() throws Exception {
        assertWindowLocation(700, 400);
        assertWindowLocation(700, 300);
        assertWindowLocation(800, 300);
        assertWindowLocation(600, 500);
        assertWindowLocation(500, 400);
        assertWindowLocation(400, 300);
        assertWindowLocation(900, 400);
        assertWindowLocation(200, 700);
        assertWindowLocation(1400, 400);
        assertWindowLocation(1300, 200);
        assertWindowLocation(1200, 100);
        assertWindowLocation(1100, 300);

        Random random = new Random();
        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
        assertWindowLocation(700, 400);
    }

    private void testAlertShown() throws InterruptedException {
        WaitForAsyncUtils.waitForFxEvents();
        Label display = find("#display");
        Platform.runLater(() -> display.setText("qwert"));

        WaitForAsyncUtils.waitForFxEvents();
        Button button = find("#addition");
        Platform.runLater(() -> button.fire());

        Thread.sleep(500);
        Stage dialog = FXRobotHelper.getStages().get(1);
        Stage stage = FXRobotHelper.getStages().get(0);

        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(dialog.isFocused());
        assertFalse(stage.isFocused());

        Thread.sleep(2000);
        Platform.runLater(() -> dialog.close());
    }

    private void testClickMouseButton(String actual, String expected) {
        clickButton(actual, expected, this::mouseClick);
    }

    private void testAppKeyEvent(String actual, String expected) {
        testAc();
        testAc();
        clickButton(actual, expected, this::keyPressImpl);

    }

    private void testApp(String actual, String expected) {
        clickButton(actual, expected, this::pressButton);
        testAc();
        testAc();
    }

    private void clickButton(String actual, String expected, Consumer<String> logic) {
        String[] arrayOfButtons = actual.split(" ");
        for (String arrayOfButton : arrayOfButtons) {
            if (isNumber(arrayOfButton)) {
                for (int j = 0; j < arrayOfButton.length(); j++) {
                    logic.accept(String.valueOf(arrayOfButton.charAt(j)));
                }
            } else {
                logic.accept(arrayOfButton);
            }
        }
        testDisplay(expected);
    }


    private void mouseClick(String token) {
        for (Map.Entry<String, DataFromButton> entry : dataFromButtons.entrySet()) {
            if (entry.getKey().equals(token)) {
                mouseClickImpl(entry.getValue().getButtonId());
            }
        }
    }

    private void assertWindowLocation(double expectedX, double expectedY) {
        WaitForAsyncUtils.waitForFxEvents();
        Label display = find("#display");
        Scene scene = display.getScene();
        Window window = scene.getWindow();

        robot.drag(display);

        Point point = MouseInfo.getPointerInfo().getLocation();
        double mouseLocationOnWindowX = abs(window.getX() - point.getX());
        double mouseLocationOnWindowY = abs(window.getY() - point.getY());

        robot.dropTo(expectedX, expectedY);

        assertThat(window.getX(), is(expectedX - mouseLocationOnWindowX));
        assertThat(window.getY(), is(expectedY - mouseLocationOnWindowY));
    }


    private void keyPressImpl(String token) {
        for (Map.Entry<String, DataFromButton> entry : dataFromButtons.entrySet()) {
            if (entry.getKey().equals(token)) {
                keyPress(entry, token);
            }
        }
    }

    private void keyPress(Map.Entry<String, DataFromButton> entry, String token) {
        if (isNumber(token) || token.equals("=") || token.equals(".") || token.equals("AC")) {
            keyPress(entry.getValue().getKeycode(), entry.getValue().getButtonId());
        } else if (token.equals("±")) {
            keyCombinationPressForConversion();
        } else {
            keyCombinationPress(entry.getValue().getKeycode(), entry.getValue().getButtonId());
        }

    }

    private void keyPress(KeyCode keyCode, String buttonId) {
        if (keyCode.equals(KeyCode.ESCAPE)) {//need to press twice to clear display and memory before each test
            keyPressImpl(keyCode, buttonId);
        }
        keyPressImpl(keyCode, buttonId);
    }

    private void keyPressImpl(KeyCode keyCode, String buttonId) {
        WaitForAsyncUtils.waitForFxEvents();
        Button button = find("#" + buttonId);

        Platform.runLater(() -> {
            Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_PRESSED,
                    "", "", keyCode, false,
                    false, false, false));
            Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_RELEASED,
                    "", "", keyCode, false,
                    false, false, false));
        });
    }

    private void keyCombinationPress(KeyCode keyCode, String buttonId) {
        WaitForAsyncUtils.waitForFxEvents();
        Button button = find("#" + buttonId);

        Platform.runLater(() -> {
            Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_PRESSED,
                    "", "", keyCode, true,
                    false, false, false));
            Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_RELEASED,
                    "", "", keyCode, true,
                    false, false, false));
        });

    }

    private void keyCombinationPressForConversion() {
        WaitForAsyncUtils.waitForFxEvents();
        Button button = find("#inversion");

        Platform.runLater(() -> {
            Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_PRESSED,
                    "", "", KeyCode.MINUS, false,
                    false, true, false));
            Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_RELEASED,
                    "", "", KeyCode.MINUS, false,
                    false, true, false));

        });
    }

    private void mouseClickImpl(String buttonId) {
        WaitForAsyncUtils.waitForFxEvents();
        Button button = find("#" + buttonId);

        if (buttonId.equals("ac")) {
            robot.doubleClickOn(button);
        } else {
            robot.clickOn(button);
        }
    }

    private boolean isNumber(String token) {
        try {
            new BigDecimal(token);
        } catch (NumberFormatException exc) {
            return false;
        }
        return true;
    }

    private void assertWindowIsMinimized() {
        WaitForAsyncUtils.waitForFxEvents();
        Button button = find("#minus");

        Scene scene = button.getScene();
        Platform.runLater(button::fire);

        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(scene.getWindow().isShowing());
    }

    private void testStyle(String expression) {
        testAc();
        testAc();

        double maxFontSize = 50;
        int minLengthOnDisplay = 7;
        int maxLengthOnDisplay = 38;

        for (int quantityDigits = 1; quantityDigits < expression.length() - 1; quantityDigits++) {
            pressButton(String.valueOf(expression.charAt(quantityDigits)));

            if (quantityDigits <= minLengthOnDisplay) {
                testStyleImpl(maxFontSize);
            } else if (quantityDigits > minLengthOnDisplay && quantityDigits < maxLengthOnDisplay) {
                testStyleImpl(maxFontSize / quantityDigits * minLengthOnDisplay);
            }
        }

        testAc();
        testAc();
    }

    private void pressButtonImpl(String buttonId) {
        WaitForAsyncUtils.waitForFxEvents();
        Button button = find("#" + buttonId);
        Platform.runLater(button::fire);
    }

    private void testStyleImpl(Double fontSize) {
        WaitForAsyncUtils.waitForFxEvents();
        Label display = find("#display");

        WaitForAsyncUtils.waitForFxEvents();
        verifyThat(display.getStyle(), is("-fx-font-size:" + fontSize));
    }

    private void pressButton(String token) {
        for (Map.Entry<String, DataFromButton> entry : dataFromButtons.entrySet()) {
            if (entry.getKey().equals(token)) {
                pressButtonImpl(entry.getValue().getButtonId());
            }
        }
    }

    private void testDisplay(String expected) {
        WaitForAsyncUtils.waitForFxEvents();
        Label display = find("#display");

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(expected, display.getText());
    }

    private void testAc() {
        WaitForAsyncUtils.waitForFxEvents();
        Button ac = find("#ac");

        Platform.runLater(ac::fire);

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("AC", ac.getText());
        testDisplay("0");
    }
}
