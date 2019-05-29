import javafx.util.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MineSweeper {
    public static String libWithDriversLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\";
    public static int x;
    public static int y;
    public static int[][] field;

    public static WebDriver driver;

    public static void main(String[] args)  {

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




        MineSweeperSolver mss = new MineSweeperSolver();
        while (!driver.findElement(By.id("face")).getAttribute("class").equals("facewin")
                && !driver.findElement(By.id("face")).getAttribute("class").equals("facedead")) {
            Pair[] squaresToClick = mss.solve(field);
            if (squaresToClick.length > 0) {
                click(squaresToClick);
            } else {
                clickRandom();
            }
            findElements();
        }
        //   driver.quit();

    }




    public static void findElements() {
        String value = driver.getPageSource();

        for (int i = 1; i <= y; i++) {
            for (int j = 1; j <= x; j++) {



                if(value.contains("class=\"square blank\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = -1;
                if(value.contains("class=\"square open0\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = 0;
                if(value.contains("class=\"square open1\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = 1;
                if(value.contains("class=\"square open2\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = 2;
                if(value.contains("class=\"square open3\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = 3;
                if(value.contains("class=\"square open4\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = 4;
                if(value.contains("class=\"square open5\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = 5;
                if(value.contains("class=\"square open6\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = 6;
                if(value.contains("class=\"square open7\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = 7;
                if(value.contains("class=\"square open8\" id=\""+i+"_"+j+"\"")) field[j - 1][i - 1] = 8;




                System.out.print(field[j - 1][i - 1] + " ");
            }
            System.out.println();
        }

    }

    public static void click() {
//

        driver.findElement(By.id("8_15")).click();


    }

    public static void click(Pair[] squaresToClick) {
        for (Pair<Integer, Integer> stc : squaresToClick) {
            driver.findElement(By.id(stc.getValue() + 1 + "_" + (stc.getKey() + 1))).click();


        }
    }

    public static void clickRandom() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (field[i][j] == -1) {
                    driver.findElement(By.id(j + 1 + "_" + (i + 1))).click();
                    return;
                }
            }
        }
    }

}
