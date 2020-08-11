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
    int currentsize;
    int differences = 0;
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
        for (int i = 1; i < 24; i++) {
            topic.add(driver.findElement(By.xpath("((//table[@id='resultTable']//tbody/tr)[" + i + "]/td)[2]")).getText());
            date.add(driver.findElement(By.xpath("((//table[@id='resultTable']//tbody/tr)[" + i + "]/td)[3]")).getText());
            userRoles.add(driver.findElement(By.xpath("((//table[@id='resultTable']//tbody/tr)[" + i + "]/td)[6]")).getText());
            attacment.add(driver.findElement(By.xpath("((//table[@id='resultTable']//tbody/tr)[" + i + "]/td)[7]")).getText());
        }
        System.out.println("count of news : " + topic.size());
        currentsize = topic.size();
        System.out.println();
        for (int i = 0; i < topic.size(); i++) {
            if (el.get(i).getAttribute("parent_id").equals("10")) {
                System.out.println(topic.get(i) + " | " + date.get(i) + " | " + userRoles.get(i) +
                        " | " + attacment.get(i) + " - Yes");
            } else {
                System.out.println(topic.get(i) + " | " + date.get(i) + " | " + userRoles.get(i) +
                        " | " + attacment.get(i) + " - No");
            }
        }
    }

    @Test
    public void VerifNewNewsPostedE() { // erdi
        addNewNewsItem();
        String expected = "Congratulations dreamRunner";
        String actual = driver.findElement(By.xpath("//a[text()='Congratulations dreamRunner']")).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void VerifNewNewsPosted() { // asim
        addNewNewsItem();
        List<WebElement> topicSize = driver.findElements(By.xpath("//input[@class='formInputText']"));
        updatesize = topicSize.size();
        Assert.assertEquals(currentsize + differences, topicSize);
        String expected = "Congratulations dreamRunner";
        String actual = driver.findElement(By.xpath("//a[text()='Congratulations dreamRunner']")).getText();
        assertEquals(expected, actual);
    }

    public void loginAsAdministrator() { // erdi
        driver.findElement(By.xpath("//button[@type='button']")).click();
        driver.findElement(By.xpath("//a[text()='Administrator']")).click();
    }

    public void addNewNewsItem() { //erdi
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
        //fill description
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

    public void logIn1stLevelSupervisor() { // Lena
        WebElement login = driver.findElement(By.xpath("//button[@class='btn btn-primary dropdown-toggle']"));
        login.click();
        WebElement levelSupervisor = driver.findElement(By.xpath("//a[text()='1st Level Supervisor']"));
        levelSupervisor.click();
    }

    @Test
    public void verifyNewlyAdd() { // Lena
        logIn1stLevelSupervisor();
        // verify topic
        String actual = driver.findElement(By.xpath("//*[contains(text(),'Congratulations dreamRunner')]")).getText().trim();
        String expected = "Congratulations dreamRunner";
        assertEquals(actual, expected);
        // verify description
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/YYYY");
        String formattedDate = formatter.format(today);
        String actual2 = driver.findElement(By.xpath("//*[contains(text(),'Promotion was awarded to dreamRunner on')]")).getText().trim();
        String expected2 = "Promotion was awarded to dreamRunner on" + formattedDate;
        assertEquals(actual2, expected2);
    }

    @Test
    public void administration() { // Korn
        driver.findElement(By.tagName("button")).click();
        driver.findElement(By.xpath("//*[(text()='Administrator')]")).click();
        driver.findElement(By.xpath("//span[contains(text(),'Admin')]")).click();
        driver.findElement(By.xpath("//span[contains(text(),'Announcements')]")).click();
        driver.findElement(By.xpath("//span[contains(text(),'News')]")).click();
        driver.switchTo().frame("noncoreIframe");
        driver.findElement(By.xpath("//*[@id='resultTable']/tbody/tr[1]/td[1]/label")).click();
        driver.findElement(By.xpath("//*[@id='frmList_ohrmListComponent_Menu']/i")).click();
        driver.findElement(By.xpath("//*[@id='newsDelete']")).click();
        driver.findElement(By.xpath("//*[@id='news-delete-button']")).click();
        List<WebElement> newstopic = driver.findElements(By.xpath("//a[@class='newsTopic']"));
        for (int i = 0; i < newstopic.size(); i++) {
            if ((newstopic.get(i).getText().contains("Congratulations dreamRunner"))|| i == newstopic.size()-1) {
                System.out.println("DELETED");
            }
        }

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}


