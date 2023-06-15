# JWT와 JUnit을 이용한 Bank App

### JPA LocalDateTime 자동으로 생성하는 법
- @EnableJpaAuditing <- 메인 클래스에 지정
- @EntityListeners(AuditingEntityListener.class) <- 엔티티 클래스에 지정

```
@Embeddable
public class BaseEntity {

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

