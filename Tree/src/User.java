import java.util.NoSuchElementException;
import java.util.Scanner;

public class User {
    private final Tree<Integer> tree;
    public static final String ARROW = "-> ";
    private static final String END_OF_TRAVEL = "End of travel.";

    private final Scanner scanner;


    User(Scanner scanner){
        tree = new Tree<>();
        this.scanner = scanner;
    }

    public void execute(String[] args) throws InputFormatException, NoSuchElementException {
        switch (args[0]){
            case("add") -> add(args);
            case("remove") -> remove(args);
            case("firm") -> firm();
            case("unfirm") -> tree.unfirm();
            case("show") -> show();
            case("NLR") -> NLR();
            case("LNR") -> LNR();
            case("LRN") -> LRN();
            default -> System.out.println("Unknown command.");
        }
    }

    public void add(String[] args) throws InputFormatException {
        if (args.length < 2) throw new InputFormatException();
        int data;
        try {
            data = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            throw new InputFormatException();
        }
        tree.add(data);
    }

    public void remove(String[] args) throws InputFormatException, NoSuchElementException{
        if (args.length < 2) throw new InputFormatException();
        int data;
        try{
            data = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            throw new InputFormatException();
        }
        tree.remove(data);
    }

    public void firm(){
        if (!tree.firmed) {
            tree.firmWare();
        }
    }

    public void show(){
        System.out.println(tree.print().toString());
    }

    private void LNR(Node<Integer> curr){
        if (curr != null && curr.getData() != null){
            if (!curr.isFirmedLeft)
                LNR(curr.getLeft());
            curr.setSelected(true);
            show();
            curr.setSelected(false);
            System.out.print(ARROW);
            scanner.nextLine();
            if (!curr.isFirmedRight)
                LNR(curr.getRight());
        }
    }

    public void LNR(){
        LNR(tree.getHead());
        System.out.println(END_OF_TRAVEL);
    }

    private void LRN(Node<Integer> curr){
        if (curr != null && curr.getData() != null){
            if (!curr.isFirmedLeft)
                LRN(curr.getLeft());
            if (!curr.isFirmedRight)
                LRN(curr.getRight());
            curr.setSelected(true);
            show();
            curr.setSelected(false);
            System.out.print(ARROW);
            scanner.nextLine();
        }
    }

    public void LRN() {
        LRN(tree.getHead());
        System.out.println(END_OF_TRAVEL);
    }

    private void NLR(Node<Integer> curr) {
        if (curr != null && curr.getData() != null){
            curr.setSelected(true);
            show();
            curr.setSelected(false);
            System.out.print(ARROW);
            scanner.nextLine();
            if (!curr.isFirmedLeft)
                NLR(curr.getLeft());
            if(!curr.isFirmedRight)
                NLR(curr.getRight());
        }
    }

    public void NLR() {
        NLR(tree.getHead());
        System.out.println(END_OF_TRAVEL);
    }
}

class InputFormatException extends Exception{}
