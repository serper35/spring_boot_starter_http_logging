# Spring Boot Starter для логирования HTTP-запросов
Spring-boot-starter-http-logging — это стартер для Spring Boot, который предоставляет мощное и настраиваемое решение для логирования HTTP-запросов и ответов в приложении Spring Boot. Он упрощает процесс отслеживания и отладки HTTP-коммуникаций,
предлагая гибкие возможности логирования, которые легко интегрируются в любое Spring Boot приложение

## Структура проекта
- Aspect: Содержит аспект логирования, который перехватывает вызовы методов и исключения для их логирования.
- Controller: REST-контроллеры, обрабатывающие HTTP-запросы.
- DTO (Data Transfer Object): Объекты, используемые для передачи данных между слоями приложения.
- Exception: Пользовательские классы исключений, используемые в проекте.
- Mapper: Классы, отвечающие за преобразование между моделями и DTO.
- Model: Классы, представляющие бизнес-сущности.
- Repository: Интерфейсы для доступа к данным.
- Service: Классы, содержащие бизнес-логику.
- Tests: Тестовые классы, проверяющие логику сервиса и функциональность логирования.

## Функциональность
- Автоматическое логирование HTTP-запросов и ответов: Логирует основную информацию о входящих HTTP-запросах и исходящих ответах, включая заголовки, полезные данные и коды статуса.
- Настраиваемые уровни логирования: Позволяет настраивать уровень логирования (например, INFO, DEBUG, ERROR) для управления детализацией логов.
- Выборочное логирование: Включает или отключает логирование на основе различных условий, таких как HTTP-методы, конечные точки или коды статуса.
- Настраиваемый формат логов: Предоставляет возможности для настройки формата сообщений в логах.
- Обработка исключений: Захватывает и логирует исключения, которые происходят в процессе обработки HTTP-запросов.
- Легковесный и ненавязчивый: Разработан с минимальным воздействием на производительность приложения и может быть легко отключен при необходимости.

## Установка
Чтобы включить spring-boot-starter-http-logging в ваш проект, добавьте следующую зависимость в pom.xml:
`<dependency>
    <groupId>ru.t1.spring_boot_starter_http_logging</groupId>
    <artifactId>spring-boot-starter-http-logging</artifactId>
    <version>1.0.0</version>
</dependency>`

### Примеры логов
Пример логов, которые могут быть сгенерированы при вызове метода getOrder в сервисном слое:
- `<dependency>
    <groupId>ru.t1.spring_boot_starter_http_logging</groupId>
    <artifactId>spring-boot-starter-http-logging</artifactId>
    <version>1.0.0</version>
</dependency>`
- `2024-08-13T00:51:08.901+03:00  INFO 8888 --- [Order] [nio-8080-exec-2] ru.t1.Order.aspect.LoggingAspect         : Returning from method getOrder with result OrderDTO{id=3, description='Заказ 1', status='PENDING', userId=4}`

## Начало работы
### Предварительные требования
- IntelliJ IDEA
- Java 21
- SpringBoot
- Spring Boot Test
- Spring Boot AOP
- Spring Boot Data JPA
- Spring Web
- PostgreSQL
- Log4j2
- Lombok

## Сборка и запуск приложения

### Соберите проект:
mvn clean install

### Запустите приложение:
mvn spring-boot:run

### Доступ к API:

API будет доступен по адресу http://localhost:8080.

## Тестирование
### Тесты логирования
Следующий тест проверяет, что аспект логирования правильно логирует вызов метода deleteOrder при негативном сценарии:  

`@ExtendWith(OutputCaptureExtension.class)`  
`@SpringBootTest`  
`public class OrderServiceImplTest {`  

    @Autowired
    private OrderServiceImpl orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Mapper mapper;
    void testDeleteOrderNotFoundLogging(CapturedOutput output) {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1));

        // Проверка логов
        assertThat(output).contains("Calling method deleteOrder with args")
                .contains("Exception thrown in method deleteOrder");
    }
    }
  c результатом: 
- `2024-08-13T01:04:10.465+03:00  INFO 3820 --- [Order] [           main] ru.t1.Order.aspect.LoggingAspect         : Calling method deleteOrder with args [1]`
- `2024-08-13T01:04:10.466+03:00 ERROR 3820 --- [Order] [           main] ru.t1.Order.aspect.LoggingAspect         : Exception thrown in method deleteOrder: Order not found exception`

## Заключение
Этот проект демонстрирует, как можно эффективно использовать Spring AOP для отделения логики логирования от бизнес-логики, что делает код более модульным и легким в обслуживании.
