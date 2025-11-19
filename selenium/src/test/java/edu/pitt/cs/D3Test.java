package edu.pitt.cs;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class D3Test {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  
  @Before
  public void setUp() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    driver = new ChromeDriver(options);
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  
  @After
  public void tearDown() { 
    driver.quit();
  }
  
  @Test
  public void tEST1LINKS() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/"); 
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";"); 
    
    // Store and verify Reset link href
    vars.put("menuItem", driver.findElement(By.linkText("Reset")).getAttribute("href"));
    assertThat(vars.get("menuItem").toString(), is("http://localhost:8080/reset"));
  }
  
  @Test
  public void tEST2RESET() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Set all cats to rented
    js.executeScript("document.cookie = \"1=true\";document.cookie = \"2=true\";document.cookie = \"3=true\";");
    
    // Click Reset link
    driver.findElement(By.linkText("Reset")).click();
    
    // Verify all cats are available after reset
    assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li")).getText(), is("ID 1. Jennyanydots"));
    assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
  }
  
  @Test
  public void tEST3CATALOG() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Navigate to Catalog page
    driver.findElement(By.linkText("Catalog")).click();
    
    // Verify cat2 image source
    vars.put("imageSource", driver.findElement(By.xpath("//li[2]/img")).getAttribute("src"));
    assertThat(vars.get("imageSource").toString(), is("http://localhost:8080/images/cat2.jpg"));
  }
  
  @Test
  public void tEST4LISTING() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Navigate to Catalog page
    driver.findElement(By.linkText("Catalog")).click();
    
    // Verify listing shows exactly 3 cats
    assertTrue(driver.findElement(By.xpath("//div[@id='listing']/ul/li[3]")).isDisplayed());
    assertFalse(driver.findElements(By.xpath("//div[@id='listing']/ul/li[4]")).size() > 0);
    assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
  }
  
  @Test
  public void tEST5RENTACAT() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Navigate to Rent-A-Cat page
    driver.findElement(By.linkText("Rent-A-Cat")).click();
    
    // Verify rent and return input boxes exist
    assertTrue(driver.findElement(By.id("rentID")).isDisplayed());
    assertTrue(driver.findElement(By.xpath("//button[text()='Rent']")).isDisplayed());
    assertTrue(driver.findElement(By.id("returnID")).isDisplayed());
    assertTrue(driver.findElement(By.xpath("//button[text()='Return']")).isDisplayed());
  }
  
  @Test
  public void tEST6RENT() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Navigate to Rent-A-Cat page
    driver.findElement(By.linkText("Rent-A-Cat")).click();
    
    // Rent cat ID 1
    driver.findElement(By.id("rentID")).clear();
    driver.findElement(By.id("rentID")).sendKeys("1");
    driver.findElement(By.xpath("//button[text()='Rent']")).click();
    
    // Verify success message and cat listing update
    assertThat(driver.findElement(By.id("rentResult")).getText(), is("Success!"));
    assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li")).getText(), is("Rented out"));
    assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
  }
  
  @Test
  public void tEST7RETURN() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Set cat 2 to rented
    js.executeScript("document.cookie = \"2=true\";");
    
    // Navigate to Rent-A-Cat page
    driver.findElement(By.linkText("Rent-A-Cat")).click();
    
    // Return cat ID 2
    driver.findElement(By.id("returnID")).clear();
    driver.findElement(By.id("returnID")).sendKeys("2");
    driver.findElement(By.xpath("//button[text()='Return']")).click();
    
    // Verify success message and cat listing update
    assertThat(driver.findElement(By.id("returnResult")).getText(), is("Success!"));
    assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
  }
  
  @Test
  public void tEST8FEEDACAT() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Navigate to Feed-A-Cat page
    driver.findElement(By.linkText("Feed-A-Cat")).click();
    
    // Verify feed input box and button exist
    assertTrue(driver.findElement(By.id("catnips")).isDisplayed());
    assertTrue(driver.findElement(By.xpath("//button[text()='Feed']")).isDisplayed());
  }
  
  @Test
  public void tEST9FEED() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Navigate to Feed-A-Cat page
    driver.findElement(By.linkText("Feed-A-Cat")).click();
    
    // Feed 6 catnips (evenly divisible among 3 cats)
    driver.findElement(By.id("catnips")).clear();
    driver.findElement(By.id("catnips")).sendKeys("6");
    driver.findElement(By.xpath("//button[text()='Feed']")).click();
    
    // Wait for result and verify success
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("feedResult"), "Nom, nom, nom."));
    assertThat(driver.findElement(By.id("feedResult")).getText(), is("Nom, nom, nom."));
  }
  
  @Test
  public void tEST10GREETACAT() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Navigate to Greet-A-Cat page
    driver.findElement(By.linkText("Greet-A-Cat")).click();
    
    // Verify 3 meows for 3 available cats
    assertTrue(driver.findElement(By.xpath("//*[text()='Meow!Meow!Meow!']")).isDisplayed());
  }
  
  @Test
  public void tEST11GREETACATWITHNAME() {
    // Test fixture: open URL and reset cookies
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    
    // Navigate to greet specific cat
    driver.get("http://localhost:8080/greet-a-cat/Jennyanydots");
    
    // Verify specific cat greeting
    assertTrue(driver.findElement(By.xpath("//*[text()='Meow! from Jennyanydots.']")).isDisplayed());
  }

}