package com.coreApplication;

import com.coreApplication.Model.Post;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Scraping {

  private static final int MAX_TRIES = 3;
  private static final String USERNAME_ACCOUNT_X = "deepression_ai";
  private static final String PASSWORD_ACCOUNT_X = "Kobikobi20";
  private static final String EMAIL_ACCOUNT_X = "deepression.ai@gmail.com";
  private static final String PATIENT = "Deepression_AI";
  private static final String URL_TO_X = "https://www.x.com";
  private static final int TIMEOUT_IN_SECONDS = 30;
  private static final int TIMEOUT_IN_SECONDS_PER_POST = 3;
  private static int postCounter = 0;
  private static double postAmountDouble;
  private static int postAmount;

  private static final String AD_CANCEL_BUTTON_XPATH = "//*[@data-testid='xMigrationBottomBar']";
  private static final String SIGN_IN_BUTTON_XPATH = "//*[@data-testid='loginButton']";
  private static final String USERNAME_INPUT_XPATH = "//input[@autocomplete='username']";
  private static final String USERNAME_BUTTON_XPATH = "//*[@id='react-root']//span[contains(text(),'Next')]/ancestor::button";
  private static final String PASSWORD_INPUT_XPATH = "//*[@id='react-root']//input[@name='password']";
  private static final String PASSWORD_BUTTON_XPATH = "(//button[@data-testid='LoginForm_Login_Button'])[1]";
  private static final String UNUSUAL_LOGIN_INPUT_XPATH = "//input[@data-testid='ocfEnterTextTextInput']";
  private static final String UNUSUAL_LOGIN_BUTTON_XPATH = "//button[@data-testid='ocfEnterTextNextButton']";
  private static final String USERNAME_PATIENT_XPATH = "//main//div[@data-testid='primaryColumn']//div[@data-testid='UserName']//span[contains(text(),'@')]";
  private static final String POST_AMOUNT_XPATH = "//main//div[@data-testid='primaryColumn']//div[contains(text(),'posts')]";
  private static final String POST_TEXT_XPATH = "(//*[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='%s']/ancestor::section//*[@data-testid='tweet']//*[@data-testid='tweetText']/span)[%d]";
  private static final String POST_DATE_XPATH = "(//*[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='%s']/ancestor::section//*[@data-testid='tweet']//*[@data-testid='tweetText'])[ %d ]/parent::div/parent::div//time";
  private final List<Post> postList = new ArrayList<>();

  public List<Post> scrapePatient(String url) {
    System.out.println("Initializing ChromeDriver...");
    ChromeOptions options = new ChromeOptions();
    WebDriver driver = new ChromeDriver(options);
    driver.manage().window().maximize();

    try {
      System.out.println("Navigating to " + URL_TO_X + "...\n");
      driver.get(URL_TO_X);
      WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_IN_SECONDS);
      WebDriverWait waitPerPost = new WebDriverWait(driver, TIMEOUT_IN_SECONDS_PER_POST);

      // Cancel ad
      System.out.println("Waiting for ad cancel button...\n");
      WebElement adCancelButton = wait.until(
              ExpectedConditions.elementToBeClickable(By.xpath(AD_CANCEL_BUTTON_XPATH)));
      adCancelButton.click();
      System.out.println("Ad canceled.\n");

      // Log in
      System.out.println("Waiting for sign-in button...\n");
      WebElement signInButton = wait.until(
              ExpectedConditions.elementToBeClickable(By.xpath(SIGN_IN_BUTTON_XPATH)));
      signInButton.click();

      System.out.println("Entering username: " + USERNAME_ACCOUNT_X + "...\n");
      WebElement usernameInput = wait.until(
              ExpectedConditions.visibilityOfElementLocated(By.xpath(USERNAME_INPUT_XPATH)));
      usernameInput.sendKeys(USERNAME_ACCOUNT_X);

      System.out.println("Clicking 'Next' button...\n");
      WebElement usernameButton = wait.until(
              ExpectedConditions.elementToBeClickable(By.xpath(USERNAME_BUTTON_XPATH)));
      usernameButton.click();

      try{
        System.out.println("Unusual login - Enter email: " + EMAIL_ACCOUNT_X + "...\n");
        WebElement unusualLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(UNUSUAL_LOGIN_INPUT_XPATH)));
        unusualLogin.sendKeys(EMAIL_ACCOUNT_X);

        System.out.println("Clicking 'Next' button...\n");
        WebElement unusualLoginButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath(UNUSUAL_LOGIN_BUTTON_XPATH)));
        unusualLoginButton.click();
      }catch (Exception e){e.printStackTrace();}

      System.out.println("Entering password...\n");
      WebElement passwordInput = wait.until(
              ExpectedConditions.visibilityOfElementLocated(By.xpath(PASSWORD_INPUT_XPATH)));
      passwordInput.sendKeys(PASSWORD_ACCOUNT_X);

      System.out.println("Clicking 'Login' button...\n");
      WebElement passwordButton = wait.until(
              ExpectedConditions.elementToBeClickable(By.xpath(PASSWORD_BUTTON_XPATH)));
      passwordButton.click();

      // Cancel ad again if needed
      System.out.println("Waiting for ad cancel button again...\n");
      adCancelButton = wait.until(
              ExpectedConditions.elementToBeClickable(By.xpath(AD_CANCEL_BUTTON_XPATH)));
      adCancelButton.click();
      System.out.println("Ad canceled again.\n");

      // Navigate to patient's profile
      System.out.println("Navigating to patient's profile at " + url + "...\n");
      driver.navigate().to(url);

      System.out.println("Waiting for patient's username to be visible...\n");
      WebElement usernamePatientElement = wait.until(
              ExpectedConditions.visibilityOfElementLocated(By.xpath(USERNAME_PATIENT_XPATH)));
      String usernamePatient = usernamePatientElement.getText().toLowerCase();
      System.out.println("Patient's username: " + usernamePatient + "\n");

      System.out.println("Waiting for post amount to be visible...\n");
      String postAmountString = wait.until(
              ExpectedConditions.visibilityOfElementLocated(By.xpath(POST_AMOUNT_XPATH))).getText()
          .split(" ")[0];
      postAmount = convertToPostAmount(postAmountString);

      WebElement html = driver.findElement(By.tagName("html"));

      // Scrape posts
      for (int i = 1; i <= postAmount; i++) {
        int tries = 0;
        boolean success = false;
        while (!success) {
          try {
            String postTextXPath = String.format(POST_TEXT_XPATH, usernamePatient, i);
            String postDateXPath = String.format(POST_DATE_XPATH, usernamePatient, i);

            System.out.println("Retrieving post " + i + " (try " + (tries + 1) + ")...\n");
            retrievePost(waitPerPost, postTextXPath, postDateXPath);
            System.out.println("Post " + i + " retrieved.\n");
            success = true;
          } catch (Exception e) {
            tries++;
            if (tries >= MAX_TRIES) {
              System.out.println("Failed to retrieve post " + i + " after " + MAX_TRIES + " tries.\n");
              break;
            }
            // Scroll down a bit
            html.sendKeys(Keys.PAGE_DOWN);
          }
        }
        if (!success) {
          break; // Break from the outer loop if the maximum number of tries is exceeded
        }
      }
    } catch (Exception e) {
      System.err.println("An error occurred during scraping: " + e.getMessage() + "\n");
      e.printStackTrace();
    } finally {
      System.out.println("Closing the browser...\n");
      driver.quit();
    }

    return postList;
  }

  private void retrievePost(WebDriverWait waitPerPost, String postTextXPath, String postDateXPath) {
    WebElement postElement = waitPerPost.until(
        ExpectedConditions.visibilityOfElementLocated(By.xpath(postTextXPath)));
    WebElement postDateElement = waitPerPost.until(
        ExpectedConditions.visibilityOfElementLocated(By.xpath(postDateXPath)));

    postList.add(Post.builder()
        .source("Twitter")
        .text(postElement.getText())
        .date(postDateElement.getAttribute("datetime"))
        .build());
  }

  public int convertToPostAmount(String postAmountString) {
    if (postAmountString.contains("K")) {
      postAmountString = postAmountString.replace("K", "");
      postAmountDouble = Double.parseDouble(postAmountString) * 1000;
    } else if (postAmountString.contains(",")) {
      postAmountString = postAmountString.replace(",", "");
      postAmountDouble = Double.parseDouble(postAmountString);
    } else {
      postAmountDouble = Double.parseDouble(postAmountString);
    }
    postAmount = (int) postAmountDouble;
    System.out.println("Total posts: " + postAmount + "\n");
    return postAmount;
  }

  public static void main(String[] args) {
    String urlToPatient = "https://x.com/Deepression_AI";
    Scraping scraping = new Scraping();
    List<Post> postList = scraping.scrapePatient(urlToPatient);

    System.out.println("\n\nPost list for: " + PATIENT);
    System.out.println("---------------------------------------------------\n");
    postList.forEach(p -> System.out.println("Post " + ++postCounter + ": " + p + "\n"));
    System.out.println("Total posts: " + (postCounter));
  }
}
