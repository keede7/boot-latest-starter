## Spring Boot Latest Version Starter

- **Spring Boot 3.1.2**
- **Security 6.1**

### Security Config

`Security` 설정도 `Spring Boot 3.x` 부터 변화가 생겼습니다.

그에 따라서 변경된 코드를 살펴보고, REST 방식의 로그인을 알아봅니다. 

> 각 브랜치마다 구현에 대한 코드를 구분시켰습니다.
> 
> 구현된 파트마다 포스팅 예정입니다.

1) `Security` 기본설정
   - [Velog](https://velog.io/@kide77/Spring-Boot-3.x-Security-%EA%B8%B0%EB%B3%B8-%EC%84%A4%EC%A0%95-%EB%B0%8F-%EB%B3%80%ED%99%94)
   - [Branch](https://github.com/keede7/boot-latest-starter/tree/security)

2) `Form` => `REST` 방식의 로그인 구현
    - [Velog](https://velog.io/@kide77/Spring-Boot-3.x-Security-Rest-API-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%9A%94%EC%B2%AD%EB%B6%80-%EA%B5%AC%ED%98%84-1)
    - [Branch](https://github.com/keede7/boot-latest-starter/tree/filter/login%231)

3) `REST` 로그인의 문제와 해결방안
   - [Velog](https://velog.io/@kide77/Security-6.1.x-Rest-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%84%B1%EA%B3%B5%EC%97%90-%EB%8C%80%ED%95%9C-%EA%B3%A0%EC%B0%B0-%EC%8A%A4%EC%95%95-%EC%A3%BC%EC%9D%98)
   - [Branch](https://github.com/keede7/boot-latest-starter/tree/filter/login%232)

4) `REST` 로그인 성공 구현
   - [Velog](https://velog.io/@kide77/Security-6.1.2-Rest-API-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%84%B1%EA%B3%B5-%EC%B2%98%EB%A6%AC-%EA%B5%AC%ED%98%84)
   - [Branch](https://github.com/keede7/boot-latest-starter/tree/login/success%231)

5) `REST` 로그아웃 구현
   - [Velog](https://velog.io/@kide77/Security-6.1.2-%EB%A1%9C%EA%B7%B8%EC%95%84%EC%9B%83-%EA%B5%AC%ED%98%84)
   - [Branch](https://github.com/keede7/boot-latest-starter/tree/logout/success%231)

6) 인증실패 처리 구현
   - [Branch](https://github.com/keede7/boot-latest-starter/tree/filter/entryPoint)


---

### 로그인 + JWT

잘 활용되는 방식은 아니겠지만, 만약 해당 방식으로 구현할 경우에는 특정 조건이 존재 할 것이다.

1) 기획단계에서 이러한 방법을 요청했다.
2) JWT를 활용함과 동시에 세션을 사용해서 구현을 해야했다.
3) JWT 토큰으로 자동 로그인을 구현한다.

등등,,

---

### OAuth2 + JWT