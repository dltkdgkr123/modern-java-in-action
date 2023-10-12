import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static class WeightPredicate implements ApplePredicate<Apple> {
        @Override
        public boolean test(Apple a) {
            return a.getWeght() > 100;
        }
    }

    public static class ColorPredicate implements ApplePredicate<Apple> {
        @Override
        public boolean test(Apple a) {
            // 문자 리터럴을 앞으로 -> 널포인터 방지
            return "Green".equals(a.getColor());
        }
    }

    public static class CustomPredicate implements Predicate<Apple> {

        @Override
        public boolean test(Apple a) {
            return a.getWeght() > 100;
        }
    }

    public interface ApplePredicate<Apple>{
        boolean test(Apple a);
    }

    static class Apple {

        int weght;

        String color;

        public Apple(int weght, String color) {
            this.weght = weght;
            this.color = color;
        }

        @Override
        public String toString() {
            return "Apple{" +
                    "weght=" + weght +
                    ", color='" + color + '\'' +
                    '}';
        }

        public int getWeght() {
            return weght;
        }

        public String getColor() {
            return color;
        }

        public static boolean isHeavy(Apple a) {
            return a.getWeght() > 100;
        }

        public static boolean isGreen(Apple a) {
            return "Green".equals(a.getColor());
        }

        //ApplePredicate는 Predicate의 상속/확장이 아닌 이상 test()메서드를 구현했더라도 Predicate로 인식되지 않는다.
        //반면에 메서드 참조 및 익명 함수/람다는 인수와 반환형이 같다면 Predicate로 사용할 수 있다.
        static List<Apple> appleFilter(List<Apple> inventory, ApplePredicate<Apple> p) {

            List<Apple> result = new ArrayList<>();

            for (Apple a : inventory) {
                if(p.test(a)) {
                    result.add(a);
                }
            }

            return result;
        }
    }
    public static void main(String[] args) {

        Apple apple1 = new Apple(130, "Green");
        Apple apple2 = new Apple(80, "Red");
        Apple apple3 = new Apple(100, "Green");

        List<Apple> inventory = List.of(apple1, apple2, apple3);

        // 메서드 참조 & static & predicate 사용
        // 기존 -> result list를 반환하는 메소드를 여러개 만들었음
        // 장점 -> result list를 반환하는 단일 메소드 구현후 필터 기준 boolean값을 반환하는 메소드(predicate)를 인자처럼 사용
        Apple.appleFilter(inventory, Apple::isGreen).forEach(System.out::println);

        // 메소드 참조 & filter 활용
        // 기존 -> 자바 라이브러리의 필터 클래스에 정의된 정렬 기준을 넣고 감싸서 참조해야 했음
        // 장점 -> 메서드 참조로 바로 넘길 수 있음, 필터링 메소드 또한 구현하지 않아도 됨
        inventory.stream().filter(Apple::isGreen).forEach(System.out::println);

        // predicate는 수학적인 정의로 인수를받아 boolean을 반환하는 함수이다. 따라서 boolean 값이 바로 들어갈 순[filter(true) 불가능] 없다.
        // dead code에는 도달할 수 없는 코드 뿐만 아니라 아무런 영향을 미치지 않는 코드도 포함한다.
        // 따라서 아래 필터는 predicate는 맞지만 dead code가 된다.
        inventory.stream().filter(a -> true).forEach(System.out::println);

        // 동작 파라미터화 - predicate 상속 없이 직접 정의하여 상속
        Apple.appleFilter(inventory, new WeightPredicate()).forEach(System.out::println);

        // 동작 파라미터화 - 기존 predicate를 상속받아 test() 메서드 재정의
        inventory.stream().filter(new CustomPredicate()).forEach(System.out::println);

        // 람다/익명함수 사용 -> 메서드 생성이 불필요하다고 느껴질 사이즈일 때 사용
        Apple.appleFilter(inventory, a -> "Green".equals(a.getColor())).forEach(System.out::println);
        Apple.appleFilter(inventory, (Apple a) -> a.getWeght() > 100).forEach(System.out::println);
        inventory.stream().filter(a -> "Green".equals(a.getColor()) || a.getWeght() > 100).forEach(System.out::println);


        // 동작 파라미터화 - 익명 클래스 사용
        Apple.appleFilter(inventory, new WeightPredicate() {
            @Override
            public boolean test(Apple a) {
                return a.getWeght() > 100;
            }
        }).forEach(System.out::println);


    }
}