
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;


public class OrangeHRM_Test {

    WebDriver driver = null;
    WebDriverWait wait = null;


    @BeforeMethod
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("https://orangehrm-demo-6x.orangehrmlive.com/auth/login");
        wait = new WebDriverWait(driver, 5);
    }


    @Test
    public void loginSupervisor() throws InterruptedException{
        WebElement login = driver.findElement(By.xpath("//button[@class='btn btn-primary dropdown-toggle']"));
        login.click();
        WebElement admin = driver.findElement(By.xpath("(//a[@class='login-as'])[4]"));
        admin.click();

    }





    @AfterMethod
    public void tearDown(){ driver.quit();
    }


}
