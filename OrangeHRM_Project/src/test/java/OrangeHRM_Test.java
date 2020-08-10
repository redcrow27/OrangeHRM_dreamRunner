
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;


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
        wait = new WebDriverWait(driver, 20);
    }

    
    @Test
    public void VerifNewNewsPosted () { // erdi
        addNewNewsItem();

        String expected = "Congratulations dreamRunner";
        String actual = driver.findElement(By.xpath("//a[text()='Congratulations dreamRunner']")).getText();
        assertEquals(expected, actual);
    }


    @AfterMethod
    public void tearDown() {
        driver.quit();
    }


    public void loginAsAdministrator () { // erdi
        driver.findElement(By.xpath("//button[@type='button']")).click();
        driver.findElement(By.xpath("//a[text()='Administrator']")).click();

    }


    public void addNewNewsItem () { //erdi
        loginAsAdministrator();

        // click admin > announcements > news bnts
        driver.findElement(By.xpath("//span[text()='Admin']")).click();
        driver.findElement(By.xpath("//span[text()='Announcements']")).click();
        driver.findElement(By.xpath("//span[text()='News']")).click();

        driver.switchTo().frame("noncoreIframe");

        // click add btn
        WebElement element = driver.findElement(By.xpath("//i[text()='add']"));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // fill topic
        String topicInput = "Congratulations dreamRunner";
        WebElement topic = driver.findElement(By.xpath("//input[@id='news_topic']"));
        topic.sendKeys(topicInput);

        driver.switchTo().frame("news_description_ifr");

        // fill description
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM YYYY");
        String formattedDate = formatter.format(today);
        String descriptionInput = "Promotion was awarded to dreamRunner -";
        WebElement description = driver.findElement(By.xpath("//body[@id='tinymce']"));
        description.sendKeys(descriptionInput + " " + formattedDate);

        driver.switchTo().parentFrame();

        // click next btn
        WebElement nextBtn = driver.findElement(By.xpath("//button[text()='Next']"));
        wait.until(ExpectedConditions.visibilityOf(nextBtn));
        nextBtn.click();

        // check Publish To - All User Roles
        driver.findElement(By.xpath("//label[@ for='news_publish_all']")).click();

        // click publish
        driver.findElement(By.xpath("//button[text()='Publish']")).click();

    }




}
