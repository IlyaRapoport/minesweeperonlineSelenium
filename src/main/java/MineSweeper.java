import msSolver.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MineSweeper {
    private static String libWithDriversLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\";
    private static int[][] field;
    private static String sourceHTML;
    private static WebDriver driver;
    private static MineSweeperSolver msSolver;

    public static void main(String[] args) {

        // TODO slow firefox fix
//        System.setProperty("webdriver.gecko.driver", libWithDriversLocation + "geckodriver.exe");
//        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//        capabilities.setCapability("marionette",true);
//        driver= new FirefoxDriver();

        System.setProperty("webdriver.chrome.driver", libWithDriversLocation + "chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get("http://minesweeperonline.com/");

        //setCustomGame(50, 50, 700);

        String dimensions = driver.findElement(By.cssSelector("#game div:last-child")).getAttribute("id");
        String[] splittedLastElement = dimensions.split("_");
        int sizeY = Integer.parseInt(splittedLastElement[0]);
        int sizeX = Integer.parseInt(splittedLastElement[1]) - 1;

        field = new int[sizeX][sizeY];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                field[x][y] = -1;
            }
        }

        char mines100s = driver.findElement(By.id("mines_hundreds")).getAttribute("class").charAt(4);
        char mines10s = driver.findElement(By.id("mines_tens")).getAttribute("class").charAt(4);
        char mines1s = driver.findElement(By.id("mines_ones")).getAttribute("class").charAt(4);
        int totalMinesAmount = Character.getNumericValue(mines100s) * 100
                + Character.getNumericValue(mines10s) * 10
                + Character.getNumericValue(mines1s);

        msSolver = new MineSweeperSolver(sizeX, sizeY, totalMinesAmount);
        Cell[] firstClick = {msSolver.getCell(sizeX / 2, sizeY / 2)};
        click(firstClick);
        findElements(firstClick);

        while (!driver.findElement(By.id("face")).getAttribute("class").equals("facewin")
                && !driver.findElement(By.id("face")).getAttribute("class").equals("facedead")) {
            Cell[] cellsToClick = msSolver.solve(field);
            click(cellsToClick);
            findElements(cellsToClick);
        }

        // driver.quit();
    }

    private static void setCustomGame(int sizeX, int sizeY, int mineCount) {
        driver.findElement(By.cssSelector("[title='game options']")).click();
        driver.findElement(By.id("custom_height")).clear();
        driver.findElement(By.id("custom_height")).sendKeys(String.valueOf(sizeY));
        driver.findElement(By.id("custom_width")).clear();
        driver.findElement(By.id("custom_width")).sendKeys(String.valueOf(sizeX));
        driver.findElement(By.id("custom_mines")).clear();
        driver.findElement(By.id("custom_mines")).sendKeys(String.valueOf(mineCount));
        driver.findElement(By.cssSelector("[value='New Game']")).click();
    }

    private static void click(Cell[] cellsToClick) {
        for (Cell cell : cellsToClick) {
            driver.findElement(By.id((cell.getY() + 1) + "_" + (cell.getX() + 1))).click();

            try {
                Alert alert = driver.switchTo().alert();
                alert.sendKeys("Selenium");
                alert.accept();
                break;
            } catch (NoAlertPresentException Ex) {
            }
        }
    }

    private static void findElements(Cell[] cellsToClick) {
        sourceHTML = driver.getPageSource();
        sourceHTML = sourceHTML.substring(sourceHTML.indexOf("id=\"game\""), sourceHTML.indexOf("display: none;"));

        for (Cell cell : cellsToClick) {
            checkCell(cell);
            findElements(cell);
        }
    }

    private static void findElements(Cell cell) {
        msSolver.getGrid().forEachCellsAround(cell, (cellNearby) -> {
            if (field[cellNearby.getX()][cellNearby.getY()] == -1 && checkCell(cellNearby)) {
                findElements(cellNearby);
            }
        });
    }

    private static boolean checkCell(Cell cell) {
        int index = sourceHTML.indexOf("\" id=\"" + (cell.getY() + 1) + "_" + (cell.getX() + 1) + "\"") - 1;
        char ch = sourceHTML.charAt(index);
        if (ch == 'k') {
            return false;
        } else {
            field[cell.getX()][cell.getY()] = Character.getNumericValue(ch);
            return true;
        }
    }
}
