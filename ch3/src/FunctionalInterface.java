
// 함수형 인터페이스는 메서드 참조/람다 표현식 등을 지원하기 위해 하나의 추상메서드만 포함한다.
// @FunctionalInterface 애노테이션으로 검증 가능하다 - 2개 이상의 메서드 포함시 컴파일러가 에러를 발생시킴
// Runnable, Predicate, Comparable, ActionListener, Callable, PrivilegedAction
// 많은 default/static/private 메서드가 존재하더라도 추상 메서드가 하나라면 함수형 인터페이스다!
@java.lang.FunctionalInterface
public interface FunctionalInterface<T> {

    // 인터페이스에선 abstact 키워드 생략
    boolean test(T t);

    // 하나의 추상 메서드만 포함 가능
//    boolean test2(T t);

    // java 8부터 포함 - 인터페이스 자체 동작/유틸리티/팩토리 메서드를 제공하기 위해 사용 ex) 유틸리티 - emptySet(), 팩토리 메서드 - List.of()
    static boolean staticMethod() {return true;};

    // java 9부터 포함 - 인터페이스 내 다른 메서드에서 재사용 가능한 코드 블록을 분리하기 위해 사용
    private boolean privateMethod(T t) {return true;};

    // java 8부터 포함 - 인터페이스 확장성을 위해 구현하지 않아도 되는 메서드(인터페이스 변경 시 이미 기반으로 구현되어있는 클래스들을 모두 수정할 수 없어서 생김)
    // 상속 받은 클래스에서 호출 가능하며, 호출 불가능하게 하고 싶을 시 재정의한다. 재정의 없이 호출을 막을 순 없다.
    default boolean defaultMethod() {return true;};
}
