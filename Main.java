import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {
  public static void main(String[] args) {
    System.out.println("Errate die Phobie by DasDirt.");
    System.out.println("I use the following list for the words: https://phobien.ndesign.de/");
    System.out.println("This game is written in German");
    new Main();
  }

  // This should be over the main method but i write it here that i can see it
  // without scrolling xD
  private HashMap<String, String> phobieHashmap = new HashMap<>();
  private int currentRound = 1;
  private int correctAnswers = 0;

  public Main() {
    try {
      // Store the html code in this var
      String input = getContent(new URL("https://phobien.ndesign.de/"));
      // This is the regex to get the phobia names & description
      Pattern pattern = Pattern.compile("<td>(.+?)</td><td>(.+?)<a.+?");
      Matcher matcher = pattern.matcher(input);

      // Ik that this is not very well solved but idc. Its working
      // split at the body tag to remove the header shit
      String[] bodySplit = input.split("<body>");
      // split at every table the words are in tables
      String[] tableSplit = bodySplit[1].split("<table>");
      for (String s1 : tableSplit) {
        // split at every table row to get just the row content
        String[] trSplit = s1.split("<tr>");
        for (int i = 0; i < trSplit.length; i++) {
          // use the regex to get the name & the description
          if (matcher.find()) {
            // put the words on a hashmap
            phobieHashmap.put(matcher.group(1), matcher.group(2));
          }
        }
      }
      // check if the hashmap isn't empty
      if (phobieHashmap.size() > 0) {
        nextRound();
      } else {
        System.err.println("Can't load the list of phobia's");
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void nextRound() throws IOException, InterruptedException {
    // get a random answer
    List<String> keys = new ArrayList<>(phobieHashmap.keySet());
    Random random = new Random();

    int correctAnswer = 0;
    // Pick a random phobia
    String phobia = keys.get(random.nextInt(keys.size()));
    // Always set a random answer.
    String one = phobieHashmap.get(keys.get(random.nextInt(keys.size())));
    String two = phobieHashmap.get(keys.get(random.nextInt(keys.size())));
    String three = phobieHashmap.get(keys.get(random.nextInt(keys.size())));

    // switches thought a random number between 1 and 3 and sets the right answer to
    // a random var.
    switch (correctAnswer = (random.nextInt(4 - 1) + 1)) {
    case 1:
      one = phobieHashmap.get(phobia);
      break;
    case 2:
      two = phobieHashmap.get(phobia);
      break;
    case 3:
      three = phobieHashmap.get(phobia);
      break;
    default:
      System.err.println("This can't be reached");
    }

    // "User interface"
    System.out.println("------------------------------------------------");
    System.out.printf("Runde %s von 10%n%n", currentRound);
    System.out.println("Phobie: " + phobia);
    System.out.println("(1): " + one);
    System.out.println("(2): " + two);
    System.out.println("(3): " + three);
    System.out.print("Wähle eine antwort (1-3): ");
    // Get the answer of the user
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    int input = -1;
    // This also checks if the user input is a Number
    try {
      String in = bufferedReader.readLine();
      if (in.equalsIgnoreCase("quit")) {
        System.out.println("Spiel wird beendet.");
        //todo: Should I make the number of rounds to a setting?
        // sleep for 1 sec to give the user time to read the sysout
        Thread.sleep(1000);
        System.exit(0);
      }
      input = Integer.parseInt(in);
    } catch (NumberFormatException e) {
      System.err.println("Bitte gebe nur Zahlen ein! Diese Runde wird ignoriert.");
      // sleep for 1 sec to give the user time to read the sysout
      Thread.sleep(1000);
      nextRound();
    }

    // check if the input is between 1 and 3
    if (input >= 1 && input <= 3) {
      if (input == correctAnswer) {
        correctAnswers++;
        System.out.println("Richtig.");
      } else {
        System.out.println("Die richtige Antwort ist: " + correctAnswer);
      }
      // sleep for 1 sec to give the user time to read the sysout
      Thread.sleep(1000);
    } else {
      System.err.println("Die eingegebene Zahl liegt nicht zwischen 1 und 3! Diese Runde wird ignoriert.");
      // sleep for 1 sec to give the user time to read the sysout
      Thread.sleep(1000);
      nextRound();
    }

    // The game has only 10 rounds if it hit 10 rounds it will show a simple
    // overview
    if (currentRound == 10) {
      checkStats();
    } else {
      currentRound++;
      nextRound();
    }
  }

  /** This method will be executed at the end of the game */
  public void checkStats() {
    System.out.println("---------------------------------------------------");
    System.out.printf("%s antworten von 10 sind richtig.%n", correctAnswers);
    System.out.println("---------------------------------------------------");
  }

  /** This mehod returns the content of a url */
  private String getContent(URL url) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
    StringBuilder result = new StringBuilder();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      result.append(line);
      result.append(System.lineSeparator());
    }
    return result.toString();
  }
}