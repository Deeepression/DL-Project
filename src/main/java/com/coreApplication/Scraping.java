package com.coreApplication;

import com.coreApplication.Model.Post;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Scraping {

  private static final String userNameAccountX = "deepression_ai";
  private static final String userPasswordAccountX = "Kobikobi20";
  private static final String patient = "Deepression_AI";
  private static final String urlToX = "https://www.X.com";
  private static final int timeOutInSeconds = 60;
  private static int postCounter = 1;
  WebElement postElement, postDateElement;
  Post tempPost;
  List<Post> postList = new ArrayList<>();

  public List<Post> scrapePatient(String url) {

    // Initialize the ChromeDriver
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless=new");
    WebDriver driver = new ChromeDriver(options);
    driver.manage().window().maximize();

    try {
      // Navigate to URL
      driver.get(urlToX);
      WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);

      // Find the ad-cancel button element by xpath and click it
      WebElement adCancelButton = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//*[@data-testid='xMigrationBottomBar']")));
      adCancelButton.click();

      // Find the sign-in button element by xpath and click it
      WebElement signInButton = wait.until(
          ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-testid='loginButton']")));
      signInButton.click();

      // Find the username input element by xpath and enter the username
      WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("//input[@autocomplete='username']")));
      usernameInput.sendKeys(userNameAccountX);

      // Find the username button element by xpath and click it
      WebElement usernameButton = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//*[@id=\"react-root\"]//span[contains(text(),'Next')]/ancestor::button")));
      usernameButton.click();

      // Find the password input element by xpath and enter the password
      WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("//*[@id=\"react-root\"]//input[@name='password']")));
      passwordInput.sendKeys(userPasswordAccountX);

      // Find the password button element by xpath and click it
      WebElement passwordButton = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("(//button[@data-testid='LoginForm_Login_Button'])[1]")));
      passwordButton.click();

      // Click on ad-cancel button
      adCancelButton = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//*[@data-testid='xMigrationBottomBar']")));
      adCancelButton.click();

      //Navigate to patient profile
      driver.navigate().to(url);

      //find the username element of the patient by xpath
      WebElement usernamePatientElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath(
              "//main//div[@data-testid='primaryColumn']//div[@data-testid='UserName']//span[contains(text(),'@')]")));
      String usernamePatient = usernamePatientElement.getText();

      //find the amount of posts element by xpath
      WebElement postElementAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("//main//div[@data-testid='primaryColumn']//div[contains(text(),'posts')]")));
      int postAmount = Integer.parseInt(postElementAmount.getText().split(" ")[0]);

      // Find the post list element by xpath and print the posts
      //change 5 to postAmount
      for (int i = 1; i <= 5; i++) {
        postElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
            "(//*[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='"
                + usernamePatient.toLowerCase()
                + "']/ancestor::section//*[@data-testid='tweet']//*[@data-testid='tweetText']/span)["
                + i + "]")));

        postDateElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
            "(//*[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='"
                + usernamePatient.toLowerCase()
                + "']/ancestor::section//*[@data-testid='tweet']//*[@data-testid='tweetText'])["
                + i + "]/parent::div/parent::div//time")));
        postList.add(
            Post.builder()
                .source("Twitter")
                .text(postElement.getText())
                .date(postDateElement.getAttribute("datetime"))
                .build());
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // Close the browser
      driver.quit();
    }
    return postList;
  }


  public static void main(String[] args) {
    String urlToPatient = "https://x.com/Deepression_AI";
    Scraping scraping = new Scraping();
    List<Post> postList = scraping.scrapePatient(urlToPatient);
    System.out.println("\n\n Post list for: " + patient);
    System.out.println("---------------------------------------------------" + "\n");
    postList.forEach(p -> System.out.println("post " + postCounter++ + ": " + p + "\n"));
    System.out.println("Total posts: " + (postCounter - 1));
  }
}
