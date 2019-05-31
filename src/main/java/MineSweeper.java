import javafx.util.Pair;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MineSweeper {
    public static String libWithDriversLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\";
    public static int x;
    public static int y;
    public static int[][] field;
    public static String sourceHTML;

    public static WebDriver driver;
    public static int totalMine;

    private static boolean checkGridBorder(int x, int y, Pair<Integer, Integer> size) {
        return x >= 0 && y >= 0 && x < size.getKey() && y < size.getValue();
    }

    public static void main(String[] args) {

/*
        System.setProperty("webdriver.gecko.driver", libWithDriversLocation + "geckodriver.exe");
       // DesiredCapabilities capabilities = DesiredCapabilities.firefox();
       // capabilities.setCapability("marionette", true);
        driver = new FirefoxDriver();
*/

        System.setProperty("webdriver.chrome.driver", libWithDriversLocation + "chromedriver.exe");
        driver = new ChromeDriver();


        driver.manage().window().maximize();

        driver.get("http://minesweeperonline.com/");

       //  custom();


        String dimensions = driver.findElement(By.cssSelector("#game div:last-child")).getAttribute("id");

        String[] splitedLasteelement = dimensions.split("_");

        y = Integer.parseInt(splitedLasteelement[0]);
        x = Integer.parseInt(splitedLasteelement[1]) - 1;
        field = new int[x][y];
        findElementsFirst();
        click();

        Pair[] firstClick = {new Pair<>(x / 2, y / 2)};
        findElements(firstClick);

        MineSweeperSolver mss = new MineSweeperSolver(totalMine());
        while (!driver.findElement(By.id("face")).getAttribute("class").equals("facewin")
                && !driver.findElement(By.id("face")).getAttribute("class").equals("facedead")) {
            Pair<Integer, Integer>[] squaresToClick = mss.solve(field);
            click(squaresToClick);
            findElements(squaresToClick);
        }


        // driver.quit();

    }

    private static int totalMine() {
        String mine100 = driver.findElement(By.id("mines_hundreds")).getAttribute("class");
        String mine10 = driver.findElement(By.id("mines_tens")).getAttribute("class");
        String mine1 = driver.findElement(By.id("mines_ones")).getAttribute("class");
        for (int i = 1; i <= 9; i++) {
            if (mine100.equals("time" + i)) totalMine = totalMine + (i * 100);
            if (mine10.equals("time" + i)) totalMine = totalMine + (i * 10);
            if (mine1.equals("time" + i)) totalMine = totalMine + (i);
        }

        return totalMine;
    }

    public static boolean isAlertPresent() {

        try {
            driver.switchTo().alert();

            return true;
        } catch (NoAlertPresentException Ex) {
            return false;

        }

    }


    private static void findElementsFirst() {
        for (int i = 1; i <= y; i++) {
            for (int j = 1; j <= x; j++) {
                field[j - 1][i - 1] = -1;
            }
        }
    }

    private static void custom() {
        driver.findElement(By.xpath("//*[@title='game options']")).click();
        driver.findElement(By.id("custom")).click();
        driver.findElement(By.id("custom_height")).clear();
        driver.findElement(By.id("custom_height")).sendKeys("50");
        driver.findElement(By.id("custom_width")).clear();
        driver.findElement(By.id("custom_width")).sendKeys("50");
        driver.findElement(By.id("custom_mines")).clear();
        driver.findElement(By.id("custom_mines")).sendKeys("650");

        driver.findElement(By.xpath("//*[@value='New Game']")).click();

    }

    public static void findElements(Pair<Integer, Integer>[] squaresToClick) {
        if (!isAlertPresent()) {
            sourceHTML = driver.getPageSource();
            sourceHTML = sourceHTML.substring(sourceHTML.indexOf("id=\"game\""), sourceHTML.indexOf("display: none;"));

            for (Pair<Integer, Integer> clickedSquare : squaresToClick) {
                checkSquare(clickedSquare.getKey(), clickedSquare.getValue());
                findElements(clickedSquare.getKey(), clickedSquare.getValue());
            }
        } else fillAlert();
    }

    private static void fillAlert() {

        Alert alert = driver.switchTo().alert();

        alert.sendKeys("Selenium");


        alert.accept();
    }

    public static void findElements(int cx, int cy) {
        for (int x1 = -1; x1 <= 1; x1++) {
            for (int y1 = -1; y1 <= 1; y1++) {
                if (x1 == 0 && y1 == 0 || !checkGridBorder(cx + x1, cy + y1, new Pair<>(x, y))) {
                    continue;
                }
                if (checkSquare(cx + x1, cy + y1)) {
                    findElements(cx + x1, cy + y1);
                }
            }
        }
    }

    public static boolean checkSquare(int cx, int cy) {
        if (field[cx][cy] != -1) {
            return false;
        }
        int index = sourceHTML.indexOf("\" id=\"" + (cy + 1) + "_" + (cx + 1) + "\"") - 1;
        switch (sourceHTML.charAt(index)) {
            case 'k': {
                return false;
            }
            case '0': {
                field[cx][cy] = 0;
                return true;
            }
            case '1': {
                field[cx][cy] = 1;
                return true;
            }
            case '2': {
                field[cx][cy] = 2;
                return true;
            }
            case '3': {
                field[cx][cy] = 3;
                return true;
            }
            case '4': {
                field[cx][cy] = 4;
                return true;
            }
            case '5': {
                field[cx][cy] = 5;
                return true;
            }
            case '6': {
                field[cx][cy] = 6;
                return true;
            }
            case '7': {
                field[cx][cy] = 7;
                return true;
            }
            case '8': {
                field[cx][cy] = 8;
                return false;
            }
        }
        return false;
    }

    public static void click() {
        driver.findElement(By.id(((y + 1) / 2) + "_" + ((x + 1) / 2))).click();
    }

    public static void click(Pair<Integer, Integer>[] squaresToClick) {
        if (!isAlertPresent()) {
            for (Pair<Integer, Integer> stc : squaresToClick) {
                driver.findElement(By.id((stc.getValue() + 1) + "_" + (stc.getKey() + 1))).click();
            }
        } else fillAlert();
    }
}
