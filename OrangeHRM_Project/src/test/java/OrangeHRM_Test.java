
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OrangeHRM_Test {

    int initialsize;
    WebDriver driver = null;
    WebDriverWait wait = null;


    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("https://orangehrm-demo-6x.orangehrmlive.com/auth/login");
        wait = new WebDriverWait(driver, 20);
    }

    @Test
    public void loginAsA() throws InterruptedException {
        WebElement login = driver.findElement(By.xpath("//button[@class='btn btn-primary dropdown-toggle']"));
        login.click();
        WebElement admin = driver.findElement(By.xpath("(//a[@class='login-as'])[2]"));
        admin.click();
        driver.findElement(By.xpath("//a//span[text()='Admin']")).click();
        driver.findElement(By.xpath("//li[@id='menu_news_Announcements']//span[text()='Announcements']")).click();
        driver.findElement(By.xpath("//a[@id='menu_news_viewNewsList']//span[text()='News']")).click();

        driver.switchTo().parentFrame();
        Thread.sleep(3000);
        driver.switchTo().frame("noncoreIframe");
        List<WebElement> key = driver.findElements(By.xpath("//td/a[@class='newsTopic']"));
        Map<String, List<String>> data = new HashMap<String, List<String>>();
        //putting keys to the map
        for (int i = 0; i < key.size(); i++) {
            data.put(key.get(i).getText(), null);
        }
        for (String each : data.keySet()) {
            List<String> list = new ArrayList<String>();
            list.add(driver.findElement(By.xpath("(//table[@id='resultTable']//tbody//a[text()='" + each + "']/../../td)[3]")).getText());
            list.add(driver.findElement(By.xpath("(//table[@id='resultTable']//tbody//a[text()='" + each + "']/../../td)[6]")).getText());
            if (driver.findElement(By.xpath("(//table[@id='resultTable']//tbody//a[text()='" + each + "']/../../td)[7]/i")).getAttribute("class")
                    .equals("material-icons attachment disabled handCurser")) {
                list.add("Attachment - No");
            } else {
                list.add("Attachment - Yes");
            }
            data.put(each, list);
        }
        initialsize = data.size();
        System.out.println("count of news : " + initialsize);
    }
    
    @AfterMethod
   public void tearDown(){ driver.quit();
    }

}

