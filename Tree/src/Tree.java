import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class Tree<T extends Comparable<T>> {

    private final Node<T> head;
    int size;

    private final ArrayList<Consumer<Node<T>>> removeFunctions = new ArrayList<>(List.of(
            this::removeNoChildren,
            this::removeWithRightChild,
            this::removeWithLeftChild,
            this::removeWithBothChildren));

    boolean firmed;

    Tree(){
        head = new Node<>(null);
        size = 0;
    }

    public Node<T> getHead(){
        return head.getLeft();
    }

    public void add(T data) {
        if (firmed) unfirm();
        Node<T> curr = head.getLeft();
        Node<T> toAdd = new Node<>(data);
        boolean notAdded = true;
        if (size == 0){
            toAdd.setParent(head);
            head.setLeft(toAdd);
            size++;
        } else {
            while (notAdded) {
                Node<T> prev = curr;
                int compareResult = prev.getData().compareTo(toAdd.getData());
                if (compareResult < 0) {
                    curr = prev.getRight();
                } else if (compareResult > 0) curr = prev.getLeft();
                else notAdded = false;
                if (notAdded && curr == null) {
                    if (compareResult < 0){
                        prev.setRight(toAdd);
                    }else
                        prev.setLeft(toAdd);
                    toAdd.setParent(prev);
                    notAdded = false;
                    size++;
                }
            }
        }
    }

    public void remove(T data){
        if (this.firmed){
            unfirm();
            removeImp(data);
            firmWare();
        }else
            removeImp(data);
    }

    private void removeImp(T data) throws NoSuchElementException {
        Node<T> curr = head.getLeft();
        boolean notRemoved = true;
        while (notRemoved) {
            if (curr == null) {
                throw new NoSuchElementException();
            } else {
                int compareResult = curr.getData().compareTo(data);
                if (compareResult > 0) {
                    curr = curr.getLeft();
                } else if (compareResult < 0) {
                    curr = curr.getRight();
                } else {
                    Node<T> left = curr.getLeft();
                    Node<T> right = curr.getRight();
                    removeFunctions.get(removeFunctionHash(left, right)).accept(curr);
                    notRemoved = false;
                }
            }
        }
        size--;
        firmed = false;
    }

    private int removeFunctionHash(Node<T> left, Node<T> right) {
        byte hash = 0;
        if (left != null) hash |= 2;
        if (right != null) hash |= 1;
        return hash;
    }

    private void removeNoChildren(Node<T> curr) {
        if (curr.getParent().getLeft().getData().compareTo(curr.getData()) == 0) {
            curr.getParent().setLeft(null);
        } else curr.getParent().setRight(null);
    }

    private void removeWithLeftChild(Node<T> curr) {
        Node<T> parent = curr.getParent();
        Node<T> left = curr.getLeft();
        if (parent.getLeft().getData().compareTo(curr.getData()) == 0) {
            parent.setLeft(left);
        } else parent.setRight(left);
        left.setParent(parent);
    }

    private void removeWithRightChild(Node<T> curr) {
        Node<T> parent = curr.getParent();
        Node<T> right = curr.getRight();
        if (parent.getLeft().getData().compareTo(curr.getData()) == 0) {
            parent.setLeft(right);
        } else parent.setRight(right);
        right.setParent(parent);
    }

    private void removeWithBothChildren(Node<T> curr) {
        Node<T> parent = curr.getParent();
        Node<T> left = curr.getLeft();
        Node<T> right = curr.getRight();
        if (parent.getLeft().getData().compareTo(curr.getData()) == 0) {
            parent.setLeft(right);
            right.setParent(parent);
            while (right.getLeft() != null){
                right = right.getLeft();
            }
            right.setLeft(left);
            left.setParent(right);
        } else {
            parent.setRight(left);
            left.setParent(parent);
            while (left.getRight() != null){
                left = left.getRight();
            }
            left.setRight(right);
            right.setParent(left);
        }
    }

    private void unfirm(Node<T> curr) {
        if (curr != null) {
            if (curr.isFirmedLeft) {
                curr.setLeft(null);
                curr.isFirmedLeft = false;
            }
            if (curr.isFirmedRight){
                curr.setRight(null);
                curr.isFirmedRight = false;
            }
            unfirm(curr.getRight());
            unfirm(curr.getLeft());
        }
    }

    public void unfirm() {
        unfirm(head.getLeft());
        this.firmed = false;
    }

    private void firmRight(Node<T> curr){
        Node<T> parent = curr.getParent();
        while (parent.getData() != null && parent.getData().compareTo(curr.getData()) < 0){
            parent = parent.getParent();
        }
        curr.setRight(parent);
        curr.isFirmedRight = true;
    }

    private void firmLeft(Node<T> curr){
        Node<T> parent = curr.getParent();
        while (parent.getData() != null && parent.getData().compareTo(curr.getData()) > 0){
            parent = parent.getParent();
        }
        curr.setLeft(parent);
        curr.isFirmedLeft = true;
    }

    private void firmWare(Node<T> curr){
        if(curr != null){
            firmWare(curr.getLeft());
            firmWare(curr.getRight());
            if (curr.getRight() == null){
                firmRight(curr);
            }
            if (curr.getLeft() == null) {
                firmLeft(curr);
            }
        }
    }

    public void firmWare() {
        Node<T> curr = head.getLeft();
        if (firmed) unfirm(curr);
        firmWare(curr);
        if (curr.getLeft() != null || curr.getRight() != null){
            firmed = true;
        }
    }

    public StringBuilder print() {
        if (head.getLeft() != null)
            return head.getLeft().printOnLevel(1);
        else return new StringBuilder("<NULL>");
    }


}
class Node<T> {
    private Node<T> parent;
    private Node<T> left;
    private Node<T> right;
    private final T data;

    Node(T data) {
        this.data = data;
        this.right = null;
        this.left = null;
        this.parent = null;
        this.selected = false;
        this.isFirmedRight = false;
        this.isFirmedLeft = false;
    }

    public boolean isFirmedLeft;
    public boolean isFirmedRight;
    private boolean selected;

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public T getData() {
        return data;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    private StringBuilder printFirmed(int level){
        StringBuilder stringBuilder = new StringBuilder("\t".repeat(level) + this + " (\n");

        if (this.isFirmedRight) {
            if (this.getRight().getData() != null) {
                stringBuilder.append("\t".repeat(level + 1))
                        .append("[")
                        .append(this.getRight().toString())
                        .append("]\n");
            }else stringBuilder.append("\t".repeat(level + 1))
                    .append("<ROOT>\n");

        }else if (this.getRight().getData() != null) {
            stringBuilder.append(this.getRight().printOnLevel(level + 1));
        }
        if (this.isFirmedLeft) {
            if (this.getLeft().getData() != null) {
                stringBuilder.append("\t".repeat(level + 1))
                        .append("[")
                        .append(this.getLeft().toString())
                        .append("]\n");
            }else stringBuilder.append("\t".repeat(level + 1))
                    .append("<ROOT>\n");

        }else if (this.getLeft().getData() != null){
            stringBuilder.append(this.getLeft().printOnLevel(level + 1));
        }
        stringBuilder.append("\t".repeat(level)).append(")\n");
        return stringBuilder;
    }

    public StringBuilder printOnLevel(int level) {
        StringBuilder stringBuilder = new StringBuilder(("\t").repeat(level) + this );
        if (this.isFirmedRight || this.isFirmedLeft) {
            return printFirmed(level);
        }else if (left != null || right != null){
            stringBuilder.append(" (\n");

            if (right != null) {
                stringBuilder.append(this.right.printOnLevel(level + 1));

            }else stringBuilder.append("\t".repeat(level + 1))
                    .append("<NULL>\n");

            if (left != null) {
                stringBuilder.append(this.left.printOnLevel(level + 1));

            }else stringBuilder.append("\t".repeat(level + 1))
                    .append("<NULL>\n");

            stringBuilder.append("\t".repeat(level))
                    .append(")\n");
        }else
            stringBuilder.append("\n");

        return stringBuilder;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    @Override
    public String toString() {
        if (selected) {
            return "(" + data + ")";
        }
        return data.toString();
    }
}
