
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.util.concurrent.TimeUnit;


import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import static org.testng.Assert.assertEquals;



public class OrangeHRM_Test {
    int  currentsize;
    int differences=0;
    int updatesize;


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
    public void loginAsA() { // victoria
       loginAsAdministrator();

        driver.findElement(By.xpath("//a//span[text()='Admin']")).click();
        driver.findElement(By.xpath("//li[@id='menu_news_Announcements']//span[text()='Announcements']")).click();
        driver.findElement(By.xpath("//a[@id='menu_news_viewNewsList']//span[text()='News']")).click();

        driver.switchTo().frame("noncoreIframe");

        List<String> topic = new ArrayList<>();
        List<String> date = new ArrayList<>();
        List<String> userRoles = new ArrayList<>();
        List<String> attacment = new ArrayList<>();
        List<WebElement> el = driver.findElements(By.xpath("//i[starts-with(@class, 'material-icons attachment')]"));

        for(int i = 1 ; i < 24; i++) {
            topic.add(driver.findElement(By.xpath("((//table[@id='resultTable']//tbody/tr)["+i+"]/td)[2]")).getText());
            date.add(driver.findElement(By.xpath("((//table[@id='resultTable']//tbody/tr)["+i+"]/td)[3]")).getText());
            userRoles.add(driver.findElement(By.xpath("((//table[@id='resultTable']//tbody/tr)["+i+"]/td)[6]")).getText());
            attacment.add(driver.findElement(By.xpath("((//table[@id='resultTable']//tbody/tr)["+i+"]/td)[7]")).getText());
        }
        System.out.println("count of news : " + topic.size());
        currentsize = topic.size();
        System.out.println();


        for(int i = 0; i < topic.size(); i++) {
            if(el.get(i).getAttribute("parent_id").equals("10")) {
                System.out.println(topic.get(i) + " | " + date.get(i) + " | " + userRoles.get(i) +
                        " | " + attacment.get(i) + " - Yes");
            } else {
                System.out.println(topic.get(i) + " | " + date.get(i) + " | " + userRoles.get(i) +
                        " | " + attacment.get(i) + " - No");
            }
        }
    }


    @Test
    public void VerifNewNewsPosted () { // Asim
        addNewNewsItem();
        List <WebElement> topicSize = driver.findElements(By.xpath("//input[@class='formInputText']"));
        updatesize = topicSize.size();
        Assert.assertEquals(currentsize+differences,topicSize);
        String expected = "Congratulations dreamRunner";
        String actual = driver.findElement(By.xpath("//a[text()='Congratulations dreamRunner']")).getText();
        assertEquals(expected, actual);
    }
    
    @AfterMethod
   public void tearDown(){
        driver.close();
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
        differences++;
    }



}

