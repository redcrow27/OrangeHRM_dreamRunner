
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;


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
       loginAsAdministrator();

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
            List<String> list1 = new ArrayList<String>();
            list.add(driver.findElement(By.xpath("(//table[@id='resultTable']//tbody//a[text()='" + each + "']/../../td)[3]")).getText());
            list1.add(driver.findElement(By.xpath("(//table[@id='resultTable']//tbody//a[text()='" + each + "']/../../td)[6]")).getText());
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
   public void tearDown(){
        //driver.quit();
    }

    public void VerifNewNewsPosted () { // erdi
        addNewNewsItem();

        String expected = "Congratulations dreamRunner";
        String actual = driver.findElement(By.xpath("//a[text()='Congratulations dreamRunner']")).getText();
        assertEquals(expected, actual);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/YYYY");
        String formattedDate = formatter.format(today);
        String descriptionInput = "Promotion was awarded to dreamRunner on";
        WebElement description = driver.findElement(By.xpath("//body[@id='tinymce']"));
        description.sendKeys(descriptionInput + " " + formattedDate);

        driver.switchTo().parentFrame();

        // click next btn
        driver.findElement(By.xpath("//button[text()='Next']")).click();

        // check Publish To - All User Roles
        driver.findElement(By.xpath("//label[@ for='news_publish_all']")).click();

        // click publish
        driver.findElement(By.xpath("//button[text()='Publish']")).click();

    }
}

