import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = new User(scanner);
        String msg = "";
        while (msg != "end"){
            System.out.print(User.ARROW);
            msg = scanner.nextLine();
            try {
                user.execute(msg.trim().split(" "));
            }catch (InputFormatException e){
                System.out.println("Format error.");
            }catch (NoSuchElementException e){
                System.out.println("There is no such element.");
            }
        }
    }
}
