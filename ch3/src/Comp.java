//import java.util.Comparator;
//import java.util.List;
//
//public class Comp {
//    static class Apple {
//
//        int weight;
//        String color;
//
//        public Apple(int weight, String color) {
//            this.weight = weight;
//            this.color = color;
//        }
//
//        public int getWeight() {
//            return this.weight;
//        }
//
//        public String getColor() {
//            return color;
//        }
//    }
//
//    static class AppleComparator implements Comparator<Apple> {
//
//        @Override
//        public int compare(Apple o1, Apple o2) {
//            return o1.getWeight() - o2.getWeight();
//        }
//    }
//
//    public static void main(String[] args) {
//
//        List<Apple> apples = List.of(new Apple(100, "GREEN"), new Apple(150, "RED"), new Apple(30, "GREEN"));
//
//        // 1. Comparator 클래스 사용
//        // Collections.sort(List, Comparator) 는 list.sort를 호출하게 되어있다.
////        Collections.sort(apples, new AppleComparator());
//        apples.sort(new AppleComparator());
//
//        // 2. 익명 클래스 사용
//        apples.sort(new Comparator<Apple>() {
//            @Override
//            public int compare(Apple o1, Apple o2) {
//                return o1.getWeight() - o2.getWeight();
//            }
//        });
//
//        // 3. 람다
//        apples.sort((Apple o1, Apple o2) -> o1.getWeight() - o2.getWeight());
//
//        // int to Integer : new Integer(int), Integer.valueOf(int), (Integer) int ...
//        // 밑의 예제에선 Integer과 int를 비교하므로 compareTo를 위해 오토박싱 후, compareTo의 반환형대로 int 반환될 것이다.
//        apples.sort((Apple o1, Apple o2) -> Integer.valueOf(o1.getWeight()).compareTo(o2.getWeight()));
//
//        // 4. 람다 - 콘텍스트 형식 추론 (파라미터 형식 생략)
//        apples.sort((o1, o2) -> o1.getWeight() - o2.getWeight());
//
//        // 5. Comparator 내장 메서드 comparing
//        Comparator<Apple> comparator = Comparator.comparing(apple -> apple.getWeight());
//        apples.sort(comparator);
//
//        apples.sort(Comparator.comparing(apple -> apple.getWeight()));
//
//        // 6. 메서드 참조
//        apples.sort(Comparator.comparing(Apple::getWeight));
//
//        // 7. 활용 - 디폴트 메서드
//        // 역순 정렬 후 무게가 같으면 색깔 스펠링 비교
//        apples.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));
//    }
//}