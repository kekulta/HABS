import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        LocalDate date = LocalDate.ofYearDay(scan.nextInt(), scan.nextInt());
        System.out.println(date.getDayOfMonth() == date.lengthOfMonth());
    }


    public static boolean checkTheSameNumberOfTimes(int elem, List<Integer> list1, List<Integer> list2) {
        AtomicInteger counter = new AtomicInteger();
        list1.forEach(num -> {
            if (num == elem) counter.getAndIncrement();
        });
        list2.forEach(num -> {
            if (num == elem) counter.getAndDecrement();
        });
        return counter.get() == 0;
    }
}