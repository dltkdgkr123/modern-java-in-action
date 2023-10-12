import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.*;
import java.util.stream.Stream;

public class Main {

    static class Apple {

        int wegiht;

        public Apple(int wegiht) {
            this.wegiht = wegiht;
        }

        public int getWegiht() {
            return wegiht;
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
        * 함수 인터페이스들은 대부분 제너릭 문법을 사용했었다 -> 레버런스 타입을 사용했었다는 소리가 된다.
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

        // 두개의 param + 두개 입력 제약
        ObjIntConsumer objIntConsumer;

        // 두개의 param + 출력 제약
        ToIntBiFunction toIntBiFunction;

        // 함수 디스크립터 : (int, int) -> int
        IntBinaryOperator binaryOperator;

        // 없는게 훨씬 많다. 성능 차이도 미미하다고 하니 Bi 형식명만 알아두기로 했다.


    }

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
}