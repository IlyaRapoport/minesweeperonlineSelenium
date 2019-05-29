
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class MineSweeper {
   public static String libWithDriversLocation = System.getProperty("user.dir") + "\\src\\main\\resources\\";

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", libWithDriversLocation + "chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        //open test homepage
        driver.get("http://minesweeperonline.com/");
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("#game > .square[display=\"\"]:last-child")).click();
        driver.findElement(By.xpath("div[@id='game']/div[@class='square']")).click();

//        Actions action = new Actions(driver);
//        action.moveToElement("#" + lastElement.getAttribute("id")).click().perform();
    }

}
