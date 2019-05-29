
import javafx.util.Pair;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class MineSweeper {
    public static String libWithDriversLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\";
    public static int x;
    public static int y;
    public static int[][] field;

    public static WebDriver driver;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", libWithDriversLocation + "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("http://minesweeperonline.com/");

        String dimensions = driver.findElement(By.cssSelector("#game div:last-child")).getAttribute("id");

        String[] splitedLasteelement = dimensions.split("_");

        y = Integer.parseInt(splitedLasteelement[0]);
        x = Integer.parseInt(splitedLasteelement[1]) - 1;

        field = new int[x][y];



        click();
        findElements();
       // Thread.sleep(1000);
        driver.quit();

    }

    public static void findElements() {

        for (int i = 1; i <= y; i++) {
            for (int j = 1; j <= x; j++) {
                String value = driver.findElement(By.id(i + "_" + j)).getAttribute("class");

                if (value.equals("square blank")) field[j - 1][i - 1] = -1;
                if (value.equals("square open0")) field[j - 1][i - 1] = 0;
                if (value.equals("square open1")) field[j - 1][i - 1] = 1;
                if (value.equals("square open2")) field[j - 1][i - 1] = 2;
                if (value.equals("square open3")) field[j - 1][i - 1] = 3;
                if (value.equals("square open4")) field[j - 1][i - 1] = 4;
                if (value.equals("square open5")) field[j - 1][i - 1] = 5;
                if (value.equals("square open6")) field[j - 1][i - 1] = 6;
                if (value.equals("square open7")) field[j - 1][i - 1] = 7;
                if (value.equals("square open8")) field[j - 1][i - 1] = 8;

              //  System.out.print(field[j - 1][i - 1]+" ");
            }
           //System.out.println();
        }

    }

    public static void click() {
//        Pair<Integer, Integer>[] coord;
//        coord[0].getKey();
//        coord[0].getValue();

        driver.findElement(By.id("1_1")).click();


    }


}
