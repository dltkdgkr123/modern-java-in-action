import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.*;
import java.util.stream.Stream;

public class Main {
    static class Fruit {}
    static class Lemon extends Fruit {

        int wegiht;
        String Color;

        public Lemon(int wegiht, String color) {
            this.wegiht = wegiht;
            Color = color;
        }

        public int getWegiht() {
            return wegiht;
        }

        public String getColor() {
            return Color;
        }
    }
    static class Orange extends Fruit {

        int wegiht;

        public Orange(int wegiht) {
            this.wegiht = wegiht;
        }
    }
    static class Apple extends Fruit{

        int wegiht;

        public Apple(int wegiht) {
            this.wegiht = wegiht;
        }

        public Apple(){}

        public int getWegiht() {
            return wegiht;
        }

        public static int getWegiht2(Apple a) {
            return a.getWegiht();
        }

        public void setWegiht(int wegiht) {
            this.wegiht = wegiht;
        }

        @Override
        public String toString() {
            return "Apple{" +
                    "wegiht=" + wegiht +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {

        // Stream/Ramda

        // 람다 표현은 함수형 인터페이스의 추상메서드를 구현한 "인스턴스"로 취급 할 수 있다.
        process(() -> System.out.println("Hello world"));

        // Runnable은 void를 반환해야 한다.
        Runnable r1 = () -> {};
        process(r1);

        // compareTo는 객체 값 비교에 사용한다. -> 프리미티브 타입 비교에는 존재하지 않으므로 객체클래스의 메서드 사용
        Comparator<Apple> byWeight = (a1, a2) -> Integer.compare(a1.getWegiht(), a2.getWegiht());

        // 객체를 반환형으로 쓸 수 있다.
        List.of(new Apple(10)).stream().map(apple -> new Apple(20));

        // Stream은 기존의 데이터를 가공하여 사용하기 위해 설계 - 임시/새 객체로 스트림을 만들어 사용할 순 있으나 권장하지 않음
        Stream<Apple> appleStream = Stream.<Apple>builder().add(new Apple(10)).build();
        Stream<Apple> appleStream2 = Stream.of(new Apple(10));

        // 팩토리 메서드 - of()
        // -------------------------------------------------------------------------------------------------
        List list = Collections.emptyList();

        // 원본 뷰를 불변 뷰로 래핑 -> 원본 뷰 수정 o, 불변 뷰 수정 x, 원본 뷰 수정사항 불변 뷰 반영 x
        Collections.unmodifiableList(list);
        // 초기화를 불변 뷰로 -> 수정 x
        List.of(list);

        // 요소의 개수가 0개면 emptyList, 1~2개면 List12, 3개 이상이면 ListN 사용 - 요소 개수 별 최적화
        // 0~10개는 오버로딩 되어있고 11개 이상은 가변인수 + switch문으로 구현되어 있다.
        System.out.println(List.of(1).getClass().getSimpleName());
        System.out.println(List.of(1,2).getClass().getSimpleName());
        System.out.println(List.of(1,2,3).getClass().getSimpleName());

        // java.util.ImmutableCollections.List12/ListN << AbstractImmutableList<E> << List<E>를 상속받는다.
        System.out.println(List.class.isAssignableFrom(List.of(1, 2).getClass())); // true
        System.out.println(List.class.isAssignableFrom(List.of(1, 2, 3).getClass())); // true

        // 함수 디스크립터 -> 함수나 메서드의 타입 및 시그니처를 서술하는 메서드
        // -------------------------------------------------------------------------------------------------

        fetch();
        fetch2();
        fetch3();

        // 동작 파라미터화 - 파일 읽기
        // -------------------------------------------------------------------------------------------------

        // 기존
        String result = processFile();
        System.out.println(result);

        // 함수형 인터페이스 활용
        String result2 = processFile2(br -> br.readLine() + " " + br.readLine());
        System.out.println(result2);

        // java.util.function 패키지 함수형 인터페이스
        // -------------------------------------------------------------------------------------------------

        Apple apple1 = new Apple(10);
        Apple apple2 = new Apple(20);
        Apple apple3 = new Apple(30);
        List apples = List.of(apple1, apple2, apple3);

        // Consumer T -> void : 입력 값을 받아 동작 수행
        Consumer<Apple> consumer = apple -> System.out.println(apple);
        forEach(apples, consumer);


        // Function T -> R : 입력 값을 매핑하여 출력 값으로 반환
        Function<Apple, Integer> function = apple -> Integer.valueOf(apple.getWegiht() * 10);
        System.out.println(map(apples, function));

        // Supplier () -> T : 입력 없이 출력 반환 ex) random()


        // -------------------------------------------------------------------------------------------------
        /*
        * 박싱한 값은 힙에 저장되어 메모리를 더 소비한다. 또한 기본 값 색출 시 메모리 탐색 과정을 거치므로 시간도 더 소요된다. (대부분의 경우 지장없긴 하다.)
        * 함수 인터페이스들은 대부분 제너릭 문법을 사용했었다 -> 레퍼런스 타입을 사용했었다는 소리가 된다.
        * 따라서 primitive type 값들이 래퍼클래스로 오토박싱 되었던 것이였다. (자바는 기본적으로 오토박싱을 제공한다.)
        * 이를 막기위해 함수 인터페이스는 입력/출력에 형식을 제약하는 인터페이스를 제공한다. 또한, 다룰 값 타입을 명시하여 가독성을 높힌다.
        */

        // 입력 제약
        IntConsumer intConsumer = i -> {}; // 입력 값 int로 변환, 변환 불가 값 타입일 시 문법 오류
        intConsumer.accept(new Integer(3)); // 가능 (자동 언박싱)

        // 출력 제약
        IntToDoubleFunction intToDoubleFunction;

        // 입/출력 제약
        ToDoubleFunction toDoubleFunction;

        // (기존 한개에서) 두개의 param을 입력받는다.
        BiPredicate biPredicate;

        // (기존 한개에서) 두개의 param을 입력받는다. 3번째는 결과값의 형식 표시임에 주의
        BiFunction biFunction;

        // 두개의 param + 두개 입력 제약
        ObjIntConsumer objIntConsumer;

        // 두개의 param + 출력 제약
        ToIntBiFunction toIntBiFunction;

        // 함수 디스크립터 : (int, int) -> int
        IntBinaryOperator binaryOperator;

        // 없는게 훨씬 많다. 성능 차이도 미미하다고 하니 Bi 형식명만 알아두기로 했다.

        // -------------------------------------------------------------------------------------------------

        // 예외 처리 필요 시, 직접 함수 인터페이스를 만들기 번거롭다면 익명 함수(람다)의 바디에 try-catch 블럭을 작성한다.
        Consumer<Apple> consumer1 = a -> {
            try {
                // 동작 기입
            }
            catch (Exception e) {
                throw e;
            }
        };
        
        // 바디에 일반 표현식이 있으면 void 반환 디스크립터와 호환된다!!
        Consumer consumer2 = s -> list.add(new Object()); // add()가 boolean 반환해도 void와 호환
        
        // 자유 변수 - 파라미터로 넘겨진 변수가 아닌 외부 변수
        int externalVal = 3;
        staticVal = 3;
//        extVal = 3; // 이후 캡처링 불가
        // 람다 캡처링 - 외부 변수 활용 (final 혹은 final과 같이 쓰이는 (값의 변동이 없는) 변수만 사용 가능)
        // 람다를 실행하는 스레드는 지역 변수의 복사본을 제공받아 할당 해제 이후에도 사용한다 따라서, 동기화 문제 방지를 위해 값을 한번만 할당하게 제약한다.
        Runnable r = () -> System.out.println(externalVal + staticVal);
//        extVal = 3; // 이후 캡처링 불가
        staticVal = 3;

        List<Apple> appleList = new ArrayList(apples);
        System.out.println(123);
        appleList.stream().map(Apple::getWegiht).forEach(System.out::println);
        appleList.stream().map(Apple::getWegiht2).forEach(System.out::println);

        // 메서드 참조
        // -------------------------------------------------------------------------------------------------

        // 메서드 참조 문법 (단축 표현)


        // 정적 메서드 참조
//        ToIntFunction<String> stringToInt = str -> Integer.parseInt(str);
        ToIntFunction<String> stringToInt = Integer::parseInt;

        // 인스턴스 메서드 참조
//        BiPredicate<List<String>, String> contains = (l, str) -> l.contains(str);
        BiPredicate<List<String>, String> contains = List::contains;

        // 기존 객체(외부 값 가능)의 인스턴스 메서드 참조
        // 책에는 this 키워드로 람다 파라미터 혹은 main의 인스턴스를 참조하게 되어있다.
        // this로 람다 파라미터를 가리키려할 때 Main.this을 가리켜 오류가 발생한다.
        // Main.this의 메서드를 가리켜도 main()이 static이기 때문에 오류가 발생한다. main() non-static처리 시 오류가 발생한다. 그래서 예제를 변경했다.
        String exampleStr = "안녕";
//        Predicate<String> equals = str -> exapleStr.equals(str);
        Predicate<String> equals = exampleStr::equals;

        // 디폴트 메서드의 조합 - 함수형 인터페이스 내 디폴트 메서드 활용
        // -------------------------------------------------------------------------------------------------
        // and,or,not 등의 연산과 다양한 인터페이스의 조합으로 복잡한 식을 만들 수 있다.

        // 1. Predicate
        Predicate<Lemon> lemonPredicate = lemon -> "yellow".equals(lemon.getColor());
        Predicate<Lemon> badLemonPredicate = lemonPredicate.negate().or(lemon -> lemon.getWegiht() < 80);

        // 2. Comparator -> Comp 인터페이스에 기술

        // 3. Function
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;

        Function<Integer, Integer> h1 = f.andThen(g);
        h1.apply(1); // f(g(x)) = 4;

        Function<Integer, Integer> h2 = f.compose(g);
        h1.apply(1); // f(g(x)) = 3;

        /*
        * Function으로 함수를 파라미터화하여 적분 구하기
        * 주어진 함수: f(x) = x + 10
        * 적분 구간: [3, 7]
        * */
        System.out.println(intergrate(x -> x + 10, 7, 3));

        // 생성자 참조
        // -------------------------------------------------------------------------------------------------

        // 인스턴스화 하지 않고 생성자에 접근할 수 있다!

//        Supplier<Apple> appleSupplier = () -> new Apple();
        Supplier<Apple> appleSupplier = Apple::new;
        Apple a1 = appleSupplier.get();

//        Function<Integer, Apple> appleFunction = wegiht -> new Apple(wegiht);
        Function<Integer, Apple> appleFunction = Apple::new;
        Apple a2 = appleFunction.apply(110);

        // 생성자 참조 방식의 형성은 간결성/함수형 프로그래밍 적용/수정의 용이 등의 이점이 있다.
        // 생성자의 인자가 2개일 땐 BiFunction을 사용할 수 있을 것이다. 이 이상 늘어난다면?.. => 사용자 정의 함수 인터페이스 사용

        // 갑자기 생각났다. Comparator도 함수형 인터페이스며 추상메서드 compare(T o1, T o2)를 포함한다.
        // Integer.compare(int x, int y)와 시그니처가 일치하므로 Comparator로 쓸 수 있을 것 같았다.
        List<Integer> lsit2 = new ArrayList<>();
        lsit2.sort(Integer::compare);

        // 최종적으로 생성자 참조를 활용하면, makeFruit와 같은 메서드를 만들 수 있다. (아래 정적 코드 참조)

    }

    static Map<String, Function<Integer, Fruit>> fruitMap = new HashMap<>();

    static {
        fruitMap.put("apple", Apple::new);
        fruitMap.put("orange", Orange::new);

        makeFruit("apple", 150);
        makeFruit("orange", 100);
    }

    // DoubleToDoubleFunction은 없넹
    // f(a)로 표현하지 못하고 f.apply(a)로 끝까지 표현해야하는 이유는 자바가 "자바의 모든 것은 객체로 여기는 것"을 포기할 수 없어서 라고 한다.
    static double intergrate (DoubleFunction<Double> f, double a, double b) {
        return (f.apply(a) + f.apply(b))/2 * Math.abs(a-b);
    }
    static int staticVal = 3;
    static void process(Runnable r) {
        r.run();
    }

    // 함수 디스크립터 : void -> V;
    static Callable<String> fetch() {
        return () -> "fetch";
    }

    // 함수 디스크립터 : void -> void
    static Runnable fetch2() {
        return () -> {};
    }

    // 함수 디스크립터 : T -> boolean
    static Predicate<Apple> fetch3() {
        return apple -> {return true;};
//        return apple -> true;
    }

    static String processFile() throws IOException{

        // try-with-resources
        try (BufferedReader br = new BufferedReader(new FileReader("src/sh.txt"))) {
            return br.readLine();
        }
    }

    // 동작 파라미터화
    static public String processFile2(BufferedReaderProcessor p) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("src/sh.txt"));
        return p.brProcess(br);
    }

    static <T> void forEach(List<T> list, Consumer<T> c) {
        for (T t : list) {
            c.accept(t);
        }
    }

    static <T, R> List<R> map(List<T> list, Function<T, R> f) {

        List<R> result = new ArrayList<>();

        for (T t : list) {
            result.add(f.apply(t));
        }

        return result;
    }

    static Fruit makeFruit(String fruit, Integer wegiht) {
        return fruitMap.get(fruit.toLowerCase()).apply(wegiht);
    }
}