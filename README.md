# jpa-market

그동안 공부했던 jpa 정리하고 간단한 api 개발용으로 만든 레포


### Entity 설계 시 주의점
1. 실무에선 @Setter 주의해서 사용하기
2. 모든 연관관계에선 지연로딩으로 설정
   1. 연관된 엔티티를 함께 조회해야한다면, fetch Join 혹은 엔티티 그래프 사용
   2. OneToOne, ManyToOne 은 기본 값이 EAGER 이기 때문에 직접 LAZY로 설정
3. 컬렉션은 필드에서 초기화
   1. hibernate에선 엔티티를 영속화할 때 컬렉션을 감싸서 따로 제공하는 내장 컬렉션으로 변경함.
4. Table name 임의로 변경 가능
   1. application.yml에서 설정가능 (spring.jpa.hibernate.naming.physical-strategy : com.jpabook.jpamarket... CustomNaming.class)
   2. SpringPhysicalNamingStrategy 클래스를 상속받아 getIdentifier() 함수 수정
