import java.util.Scanner;

class ArrayList<T> {
    T[] ts;
    int size;
    public ArrayList() {
        ts = (T[]) new Object[10];
        size = 0;
    }
    public void add(T data) {
        if (size == ts.length) {
            T[] temp = (T[]) new Object[ts.length * 2];
            for (int i = 0; i < ts.length; i++)
                temp[i] = ts[i];
            ts = temp;
        }
        ts[size] = data;
        size++;
    }
    public T get(int index) {
        return ts[index];
    }
}

class Person {
    StringBuilder name;
    int howManyEnEx;
    char value;
    boolean isTerminal;
    boolean isInLib;
    Book books;
    Person parent;
    ArrayList<Long> enter;
    ArrayList<Long> exit;
    ArrayList<Long> deltaTime;
    Person[] childs = new Person[26];
    public Person(char value , Person parent, StringBuilder name){
        enter = new ArrayList<>();
        exit = new ArrayList<>();
        deltaTime = new ArrayList<>();
        this.value = value;
        this.parent = parent;
        this.name = name;
        howManyEnEx = 0;
    }
}
class Book{
    int count;
    char value;
    boolean isTerminal;
    Person persons;
    StringBuilder name;
    Book parent;
    Book[] childs = new Book[26];
    public Book(char value , Book parent, StringBuilder name){
        this.value = value;
        this.parent = parent;
        this.name = name;
    }
}

public class Main {
    static Person person = new Person('.', null, new StringBuilder(""));
    static Book book = new Book('.', null, new StringBuilder(""));
    public static int binarySearch(ArrayList arrayList, long data, int min , int max){
        if(min <= max && min <= arrayList.size - 1){
            if((long)arrayList.get((min + max) / 2) == data)
                return (min + max) / 2;
            if ((long)arrayList.get((min + max) / 2) > data)
                return binarySearch(arrayList, data, min, (min + max) / 2 - 1);
            return binarySearch(arrayList, data, (min + max) / 2 + 1, max);
        }
        return (min + max) / 2;
    }
    public static boolean allChildEmptyPerson(Person person){
        boolean flag = true;
        for (int i = 0; i < person.childs.length; i++)
            if(person.childs[i] != null)
                return false;
        return true;
    }
    public static boolean allChildEmptyBook(Book book){
        boolean flag = true;
        for (int i = 0; i < book.childs.length; i++)
            if(book.childs[i] != null)
                return false;
        return true;
    }
    public static boolean existBook(String bookName){
        Book tmp = book;
        boolean flag = true;
        for (int i = 0; i < bookName.length(); i++) {
            if(tmp.childs[bookName.charAt(i) - 'a'] == null) {
                flag = false;
                break;
            }
            else
                tmp = tmp.childs[bookName.charAt(i) - 'a'];
        }
        return flag && tmp.isTerminal && tmp.count > 0;
    }
    public static void printPerson(Person person){
        if(person.isTerminal)
            System.out.print(person.name + " ");
        for (int i = 0; i < person.childs.length; i++)
            if(person.childs[i] != null)
                printPerson(person.childs[i]);
    }
    public static void printBook(Book book){
        if(book.isTerminal)
            System.out.print(book.name + " ");
        for (int i = 0; i < book.childs.length; i++)
            if(book.childs[i] != null)
                printBook(book.childs[i]);
    }

    public static void arrive(String personName, long timeEntered){
        Person tmp = person;
        for (int i = 0; i < personName.length(); i++) {
            if(tmp.childs[personName.charAt(i) - 'a'] == null)
                tmp.childs[personName.charAt(i) - 'a'] = new Person(personName.charAt(i), tmp, new StringBuilder(tmp.name.toString() + personName.charAt(i)));
            tmp = tmp.childs[personName.charAt(i) - 'a'];
        }
        tmp.enter.add(timeEntered);
        tmp.howManyEnEx++;
        tmp.isTerminal = true;
        tmp.isInLib = true;
    }
    public static void exit(String personName, long timeLeft){
        boolean flag = true;
        Person tmp = person;
        for (int i = 0; i < personName.length(); i++) {
            if(tmp.childs[personName.charAt(i) - 'a'] == null){
                flag = false;
                break;
            }
            else
                tmp = tmp.childs[personName.charAt(i) - 'a'];
        }
        if(flag && tmp.isTerminal && tmp.isInLib){
            tmp.isInLib = false;
            tmp.exit.add(timeLeft);
            if(tmp.enter.size == 1)
                tmp.deltaTime.add(tmp.exit.get(tmp.howManyEnEx - 1) - tmp.enter.get(tmp.howManyEnEx - 1));
            else
                tmp.deltaTime.add(tmp.exit.get(tmp.howManyEnEx - 1) - tmp.enter.get(tmp.howManyEnEx - 1) + tmp.deltaTime.get(tmp.howManyEnEx - 2));
        }
    }
    public static boolean isInLib(String personName){
        boolean flag = true;
        Person tmp = person;
        for (int i = 0; i < personName.length(); i++) {
            if(tmp.childs[personName.charAt(i) - 'a'] == null){
                flag = false;
                break;
            }
            else
                tmp = tmp.childs[personName.charAt(i) - 'a'];
        }
        return flag && tmp.isTerminal && tmp.isInLib;
    }
    public static void returnBook(String personName, String bookName){
        boolean flag = true;
        Book tmp = book;
        for (int i = 0; i < bookName.length(); i++) {
            if(tmp.childs[bookName.charAt(i) - 'a'] == null){
                flag = false;
                break;
            }
            else
                tmp = tmp.childs[bookName.charAt(i) - 'a'];
        }
        if(flag && tmp.isTerminal){
            if(book.persons == null)
                book.persons = new Person('.', null, new StringBuilder(""));
            Person tmp2 = tmp.persons;
            boolean flag2 = true;
            for (int i = 0; i < personName.length(); i++) {
                if(tmp2.childs[personName.charAt(i) - 'a'] == null){
                    flag2 = false;
                    break;
                }
                else
                    tmp2 = tmp2.childs[personName.charAt(i) - 'a'];
            }
            if(flag2 && tmp2.isTerminal){
                tmp2.isTerminal = false;
                while(true){
                    if(allChildEmptyPerson(tmp2) && tmp2.value != '.'){
                        int index = tmp2.value - 'a';
                        tmp2 = tmp2.parent;
                        tmp2.childs[index] = null;
                    }
                    else
                        break;
                }
                tmp.count++;
            }
        }
        Person ptmp = person;
        flag = true;
        for (int i = 0; i < personName.length(); i++) {
            if(ptmp.childs[personName.charAt(i) - 'a'] == null){
                flag = false;
                break;
            }
            else
                ptmp = ptmp.childs[personName.charAt(i) - 'a'];
        }
        if(flag && ptmp.isTerminal){
            if(ptmp.books == null)
                ptmp.books = new Book('.', null, new StringBuilder(""));
            Book tmp2 = ptmp.books;
            boolean flag2 = true;
            for (int i = 0; i < bookName.length(); i++) {
                if(tmp2.childs[bookName.charAt(i) - 'a'] == null){
                    flag2 = false;
                    break;
                }
                else
                    tmp2 = tmp2.childs[bookName.charAt(i) - 'a'];
            }
            if(flag2 && tmp2.isTerminal){
                tmp2.isTerminal = false;
                while(true){
                    if(allChildEmptyBook(tmp2) && tmp2.value != '.'){
                        int index = tmp2.value - 'a';
                        tmp2 = tmp2.parent;
                        tmp2.childs[index] = null;
                    }
                    else
                        break;
                }
            }
        }
    }
    public static long totalTimeInLib(String personName, long startTime, long endTime){
        boolean flag = true;
        Person tmp = person;
        for (int i = 0; i < personName.length(); i++) {
            if(tmp.childs[personName.charAt(i) - 'a'] == null) {
                flag = false;
                break;
            }
            else
                tmp = tmp.childs[personName.charAt(i) - 'a'];
        }
        if(flag && tmp.isTerminal){
            long result = 0;
            if (tmp.deltaTime.size == 0)
                return result;
            int indexOfEnter = binarySearch(tmp.enter, startTime, 0, tmp.enter.size);
            int indexOfExit = binarySearch(tmp.exit, endTime, 0, tmp.exit.size);
            if (indexOfExit > 0 && tmp.exit.get(indexOfExit) > endTime && tmp.enter.get(indexOfExit) > endTime)
                indexOfExit--;
            else if (indexOfExit < (tmp.exit.size - 1)  && tmp.exit.get(indexOfExit) < endTime && tmp.enter.get(indexOfExit + 1) < endTime)
                indexOfExit++;
            if (indexOfEnter < (tmp.enter.size - 1) && tmp.enter.get(indexOfEnter) < startTime && tmp.exit.get(indexOfEnter) < startTime)
                indexOfEnter++;
            else if (indexOfEnter > 0 && tmp.enter.get(indexOfEnter) > startTime && tmp.exit.get(indexOfEnter - 1) > startTime)
                indexOfEnter--;
            if (tmp.exit.get(indexOfExit) < startTime || tmp.enter.get(indexOfEnter) > endTime)
                return 0;
            result = tmp.deltaTime.get(indexOfExit);
            if(tmp.exit.get(indexOfExit) > endTime)
                result -= tmp.exit.get(indexOfExit) - endTime;
            if(tmp.enter.get(indexOfEnter) < startTime)
                result -= startTime - tmp.enter.get(indexOfEnter);
            if(indexOfEnter != 0)
                result -= tmp.deltaTime.get(indexOfEnter - 1);
            return result;
        }
        return 0;
    }
    public static void addNewBook(String bookName, int count){
        Book tmp = book;
        for (int i = 0; i < bookName.length(); i++) {
            if(tmp.childs[bookName.charAt(i) - 'a'] == null)
                tmp.childs[bookName.charAt(i) - 'a'] = new Book(bookName.charAt(i), tmp,new StringBuilder(tmp.name.toString() + bookName.charAt(i)));
            tmp = tmp.childs[bookName.charAt(i) - 'a'];
        }
        tmp.isTerminal = true;
        tmp.count += count;
    }
    public static void shouldBring(String bookName, String personName){
        if(isInLib(personName) && existBook(bookName)){
            Book tmp = book;
            for (int i = 0; i < bookName.length(); i++)
                tmp = tmp.childs[bookName.charAt(i) - 'a'];
            tmp.count--;
            if(tmp.persons == null)
                tmp.persons = new Person('.', null, new StringBuilder(""));
            Person p = tmp.persons;
            for (int i = 0; i < personName.length(); i++) {
                if(p.childs[personName.charAt(i) - 'a'] == null)
                    p.childs[personName.charAt(i) - 'a'] = new Person(personName.charAt(i), p, new StringBuilder(p.name.toString() + personName.charAt(i)));
                p = p.childs[personName.charAt(i) - 'a'];
            }
            p.isTerminal = true;
            Person pt = person;
            for (int i = 0; i < personName.length(); i++)
                pt = pt.childs[personName.charAt(i) - 'a'];
            if(pt.books == null)
                pt.books = new Book('.', null, new StringBuilder(""));
            Book bp = pt.books;
            for (int i = 0; i < bookName.length(); i++) {
                if(bp.childs[bookName.charAt(i) - 'a'] == null)
                    bp.childs[bookName.charAt(i) - 'a'] = new Book(bookName.charAt(i), bp, new StringBuilder(bp.name.toString() + bookName.charAt(i)));
                bp = bp.childs[bookName.charAt(i) - 'a'];
            }
            bp.isTerminal = true;
        }
    }
    public static void allPersonCurrentBook(String personName){
        boolean flag = true;
        Person tmp = person;
        for (int i = 0; i < personName.length(); i++) {
            if(tmp.childs[personName.charAt(i) - 'a'] == null){
                flag = false;
                break;
            }
            else
                tmp = tmp.childs[personName.charAt(i) - 'a'];
        }
        if(flag && tmp.isTerminal)
        {
            if(tmp.books == null || allChildEmptyBook(tmp.books))
                System.out.println("empty");
            else {
                printBook(tmp.books);
                System.out.println();
            }
        }
        else
            System.out.println("empty");
    }
    public static void allPersonHave(String bookName) {
        boolean flag = true;
        Book tmp = book;
        for (int i = 0; i < bookName.length(); i++) {
            if(tmp.childs[bookName.charAt(i) - 'a'] == null){
                flag = false;
                break;
            }
            else
                tmp = tmp.childs[bookName.charAt(i) - 'a'];
        }
        if(flag && tmp.isTerminal){
            if(tmp.persons == null || allChildEmptyPerson(tmp.persons))
                System.out.println("empty");
            else {
                printPerson(tmp.persons);
                System.out.println();
            }
        }
        else
            System.out.println("empty");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] cmd;
        while(scanner.hasNext()){
            cmd = scanner.nextLine().split(" ");
            switch (cmd[0]){
                case "arrive":
                    arrive(cmd[1], Integer.parseInt(cmd[2]));
                    break;
                case "exit":
                    exit(cmd[1], Integer.parseInt(cmd[2]));
                    break;
                case "isInLib":
                    if (isInLib(cmd[1])) System.out.println("YES");
                    else System.out.println("NO");
                    break;
                case "returnBook":
                    returnBook(cmd[1], cmd[2]);
                    break;
                case "totalTimeInLib":
                    System.out.println(totalTimeInLib(cmd[1], Long.parseLong(cmd[2]), Long.parseLong(cmd[3])));
                    break;
                case "addNewBook":
                    addNewBook(cmd[1], Integer.parseInt(cmd[2]));
                    break;
                case "shouldBring":
                    shouldBring(cmd[1], cmd[2]);
                    break;
                case "allPersonCurrentBook":
                    allPersonCurrentBook(cmd[1]);
                    break;
                case "allPersonHave":
                    allPersonHave(cmd[1]);
                    break;
            }
        }
    }
}