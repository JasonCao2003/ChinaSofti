# 一、Spring Boot DevTools

## **1. 添加 DevTools 依赖**

在 `pom.xml` 文件中添加 DevTools 依赖：

```xml
<dependencies>
    <!-- Spring Boot DevTools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

> **注意**：`<optional>true</optional>` 是为了避免该依赖在生产环境被打包。

------

## **2. 启用 DevTools 自动重启**

默认情况下，Spring Boot DevTools 会监视 `src/main/resources` 和 `src/main/java` 目录的变更，并在文件发生更改时自动重启应用。

**修改 `application.properties` 或 `application.yml`**

- `application.properties`:

  ```yaml
  spring.devtools.restart.enabled=true  # 启用自动重启（默认开启）
  spring.devtools.livereload.enabled=true  # 启用LiveReload（默认开启）
  ```

- `application.yml`:

  ```yaml
  spring:
    devtools:
      restart:
        enabled: true  # 启用自动重启
      livereload:
        enabled: true  # 启用LiveReload
  ```

------

## **3. 运行应用**

可以使用 **IDEA、Eclipse 或命令行** 启动 Spring Boot 应用：

```
mvn spring-boot:run
```

然后修改 `src/main/java` 或 `src/main/resources` 下的文件，Spring Boot 会自动检测并 **热重启** 应用。

# 二、Spring Boot 2 vs 3

| **对比项**           | **Spring Boot 2**              | **Spring Boot 3**                    |
| -------------------- | ------------------------------ | ------------------------------------ |
| **JDK 版本**         | JDK 8+（推荐 11）              | **JDK 17+**                          |
| **EE 依赖**          | Java EE（`javax.*`）           | **Jakarta EE 9+（`jakarta.\*`）**    |
| **GraalVM 原生镜像** | 不支持                         | **官方支持**                         |
| **Hibernate**        | Hibernate 5                    | **Hibernate 6**                      |
| **Spring Security**  | `WebSecurityConfigurerAdapter` | **移除，使用 `SecurityFilterChain`** |
| **默认 JSON 解析库** | 仅 Jackson                     | **Jackson + JSON-B + Kotlinx**       |
| **Actuator**         | 默认较宽松                     | **更严格的安全策略**                 |
| **Spring Cloud**     | 兼容 Spring Cloud 2021         | **兼容 Spring Cloud 2022+**          |

# 三、SpringBootApplication

**Spring Boot 启动时执行的主要流程**

1. **`@ComponentScan` 扫描组件**，加载 `@Component`, `@Service`, `@Repository`, `@Controller` 标注的 Bean。
2. **`@EnableAutoConfiguration` 根据 classpath 依赖进行自动配置**（如数据库连接池、Spring MVC）。
3. **`@Configuration` 解析并注册 Bean**，支持 `@Bean` 定义的自定义 Bean。
4. **Spring Boot 启动完成**，并运行 `main` 方法中的 `SpringApplication.run()`。

## **1. @Configuration**

`@Configuration` 主要用于 **定义 Spring 配置类**，可以代替 XML 配置文件。

```java
@Configuration
public class MyConfig {
    @Bean
    public String myBean() {
        return "Hello, Spring!";
    }
}
```

- 允许使用 `@Bean` 方法定义 Bean，并添加到 Spring 容器。

**可以单独使用吗？** ✅ **可以**，但需要手动加载该配置类：

```java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
String bean = context.getBean(String.class);
System.out.println(bean); // 输出: Hello, Spring!
```

------

## **2. @ComponentScan**

`@ComponentScan` 主要用于 **指定扫描 Spring 组件（`@Component`, `@Service`, `@Repository`, `@Controller`）的包路径**。

```java
@ComponentScan(basePackages = "com.example.service")
public class AppConfig {
}
```

**作用：**

- 自动扫描 `basePackages` 指定的包及其子包，并注册 **Spring 组件**（如 `@Service`、`@Controller`）。
- 如果不指定 `basePackages`，则默认扫描 **当前类所在的包及其子包**。

**可以单独使用吗？** ✅ **可以**，但通常需要配合 `@Configuration` 或 `@SpringBootApplication`。

```java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
```

------

## **3. @EnableAutoConfiguration**

`@EnableAutoConfiguration` 让 Spring Boot **根据 classpath 依赖自动配置应用**（如数据库、Web MVC）。

```java
@EnableAutoConfiguration
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

**作用：**

- 让 Spring Boot **自动配置** Web 框架、数据库连接池、JPA、缓存等组件。

**可以单独使用吗？** ✅ **可以**，但如果没有 `@ComponentScan`，Spring 可能无法扫描到自定义的组件。

# 四、Spring Boot 启动流程

Spring Boot 启动时，会经历一系列初始化过程，包括 **初始化环境、扫描组件、自动配置、启动 Web 服务器（如 Tomcat）、监听事件等**。、

------

## **1. 初始化 **

Spring Boot 应用通常通过 `SpringApplication.run()` 启动：

```java
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

**执行流程：**

1. **创建 `SpringApplication` 对象**
   - 读取 `META-INF/spring.factories`，加载 `ApplicationContextInitializer`、`ApplicationListener` 等组件。
   - 判断应用类型（Web 应用还是普通 Java 应用）。
   - 解析命令行参数。
2. **调用 `run()` 方法**
   - **触发 Spring Boot 启动流程**
   - 初始化 `ApplicationContext`
   - 发布 `ApplicationStartingEvent` 事件

------

## **2. 组件扫描**

@ComponentScan

**Spring Boot 会自动扫描 `@Component`, `@Service`, `@Repository`, `@Controller` 标注的类**，并将它们注册到 `ApplicationContext`。

**默认扫描：**

- 如果 `@SpringBootApplication` 在 `com.example` 包下，则默认扫描 `com.example` 及其子包。

**手动指定扫描路径：**

```java
@ComponentScan(basePackages = "com.example.service")
```

------

## **3. 自动配置**

`@EnableAutoConfiguration` 让 Spring Boot **根据 classpath 依赖进行自动配置**。

**工作原理：**

- 读取 `META-INF/spring.factories` 文件，加载 `EnableAutoConfiguration` 相关的配置类。
- 例如：
  - 如果 `spring-webmvc` 依赖存在，则自动配置 **Spring MVC**。
  - 如果 `spring-data-jpa` 依赖存在，则自动配置 **数据库 JPA**。
  - 如果 `thymeleaf` 依赖存在，则自动配置 **Thymeleaf 模板引擎**。

## **4. 启动内嵌Web服务器**

如果应用是 Web 应用（如 `spring-boot-starter-web` 存在），Spring Boot 会自动启动 **内嵌 Web 服务器**（Tomcat、Jetty 或 Undertow）。

1. **创建 `TomcatServletWebServerFactory`**
2. **初始化 `Tomcat` 实例**
3. **创建 `ServletContext` 并注册 Web 组件**
4. **启动 `Tomcat`**
5. **监听 8080 端口（默认）**

## **5. 监听事件**

ApplicationEventListener

Spring Boot 允许监听应用生命周期中的事件，例如：

- `ApplicationStartingEvent`（应用启动前）
- `ApplicationEnvironmentPreparedEvent`（环境准备完成）
- `ApplicationPreparedEvent`（ApplicationContext 创建完成）
- `ApplicationStartedEvent`（应用启动完成）
- `ApplicationReadyEvent`（应用完全就绪）
- `ApplicationFailedEvent`（应用启动失败）

### **自定义监听器**

```java
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("应用启动完成！");
    }
}
```

# **五、Spring Boot Bean 管理**

Spring Boot 通过 **Spring IoC（控制反转）容器** 进行 Bean 的 **创建、管理、初始化、使用和销毁**。理解 Bean 的生命周期和管理机制，有助于更好地控制应用的运行方式。

------

## **1. Bean 的定义与注册**

Spring Boot 通过以下方式 **定义和注册 Bean**：

**(1) 组件扫描（自动注册 Bean）**

Spring Boot 默认会扫描 **启动类所在包及其子包**，并自动注册 **带有以下注解的类** 作为 Bean：

- `@Component`：通用组件。
- `@Service`：业务逻辑层组件。
- `@Repository`：数据访问层组件。
- `@Controller` / `@RestController`：控制层组件。

```java
@Component
public class MyComponent {
    public void sayHello() {
        System.out.println("Hello, Spring Boot!");
    }
}
```

**自定义扫描路径：**

如果 Bean 不在默认扫描包下，可以使用 `@ComponentScan` 指定扫描路径：

```java
@ComponentScan(basePackages = "com.example.service")
public class AppConfig {
}
```

------

**(2) `@Bean` 手动注册 Bean**

如果要注册 **第三方 Bean**（如工具类、外部库），可以使用 `@Bean` 注解。

```java
@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

------

**(3) `@Import` 导入外部 Bean**

如果有多个 Bean 需要管理，可以使用 `@Import` 直接导入配置类：

```java
@Import({DataSourceConfig.class, ServiceConfig.class})
@Configuration
public class MainConfig {
}
```

------

## **2. Bean 的生命周期**

Spring 容器对 Bean 进行 **实例化、依赖注入、初始化、使用和销毁**，以下是完整的生命周期过程：

**(1) Bean 的创建（实例化）**

Spring 容器 **创建 Bean 实例**，实例化方式包括：

- **无参构造方法（默认）**
- **静态工厂方法**
- **实例工厂方法**

```java
@Component
public class MyBean {
    public MyBean() {
        System.out.println("Bean 实例化...");
    }
}
```

------

**(2) 依赖注入（属性赋值）**

实例化后，Spring 会对 Bean 进行 **依赖注入**，方式包括：

- **构造函数注入**（推荐）：

  ```java
  @Service
  public class UserService {
      private final UserRepository userRepository;
      
      @Autowired
      public UserService(UserRepository userRepository) {
          this.userRepository = userRepository;
      }
  }
  ```

- **Setter 方法注入**（较少使用）：

  ```java
  @Component
  public class MyService {
      private MyRepository myRepository;
  
      @Autowired
      public void setMyRepository(MyRepository myRepository) {
          this.myRepository = myRepository;
      }
  }
  ```

------

**(3) Bean 的初始化（Initialization）**

初始化阶段可以执行一些 **自定义初始化逻辑**，Spring 提供了以下方式：

| 方式               | 说明                             |
| ------------------ | -------------------------------- |
| `@PostConstruct`   | 方法执行于依赖注入完成后         |
| `InitializingBean` | 实现 `afterPropertiesSet()` 方法 |
| `init-method`      | 在 XML 或 `@Bean` 指定初始化方法 |

示例：

```java
@Component
public class MyBean implements InitializingBean {
    @PostConstruct
    public void postConstructInit() {
        System.out.println("1. @PostConstruct 初始化方法");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("2. InitializingBean 初始化方法");
    }
}
```

对于 `@Bean` 方式的初始化：

```java
@Bean(initMethod = "customInit")
public MyBean myBean() {
    return new MyBean();
}

public void customInit() {
    System.out.println("3. 自定义初始化方法");
}
```

------

**(4) Bean 的使用（In Use）**

初始化完成后，Bean 进入 **应用阶段**，供程序调用和使用。Spring 容器会在整个应用的生命周期内维护这些 Bean，直到应用关闭。

------

**(5) Bean 的销毁（Destruction）**

当 Spring 容器 **关闭** 时，Bean 需要执行 **资源释放** 或 **清理操作**，Spring 提供了以下方式：

| 方式             | 说明                           |
| ---------------- | ------------------------------ |
| `@PreDestroy`    | Bean 被销毁前执行              |
| `DisposableBean` | 实现 `destroy()` 方法          |
| `destroy-method` | 在 XML 或 `@Bean` 指定销毁方法 |

```java
@Component
public class MyBean implements DisposableBean {
    @PreDestroy
    public void preDestroyCleanup() {
        System.out.println("1. @PreDestroy 清理资源");
    }

    @Override
    public void destroy() {
        System.out.println("2. DisposableBean destroy() 方法");
    }
}
```

对于 `@Bean` 方式的销毁：

```java
@Bean(destroyMethod = "customDestroy")
public MyBean myBean() {
    return new MyBean();
}

public void customDestroy() {
    System.out.println("3. 自定义销毁方法");
}
```

------

## **3. Bean 生命周期流程总结**

完整的 Bean 生命周期流程如下：

1. **实例化**（创建 Bean 实例）

2. **依赖注入**（赋值 `@Autowired` 依赖）

3. **`BeanPostProcessor` 前置处理**（`postProcessBeforeInitialization()`）

4. 初始化

   - `@PostConstruct` 方法执行
   - `InitializingBean.afterPropertiesSet()` 方法执行
   - `init-method` 指定的方法执行

5. **`BeanPostProcessor` 后置处理**（`postProcessAfterInitialization()`）

6. **使用 Bean**（运行阶段）

7. 销毁 Bean

   （应用关闭时执行）

   - `@PreDestroy` 方法执行
   - `DisposableBean.destroy()` 方法执行
   - `destroy-method` 指定的方法执行

# 六、自动关闭程序

## **1. 使用System.exit(0)**

`System.exit(0)` 立即终止 Java 进程，`0` 表示正常退出，非 `0` 表示异常退出。

**示例：满足条件自动退出**

```java
public class ExitExample {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            System.out.println("当前值: " + i);
            if (i == 5) {  // 当 i == 5 时退出
                System.out.println("条件满足，程序退出！");
                System.exit(0);
            }
        }
        System.out.println("不会执行到这里");
    }
}
```

## **2. 在main方法中return**

如果 `return` 在 `main` 方法中被调用，程序会直接返回，但不会终止整个 JVM 进程（适用于单线程应用）。

```java
public class ReturnExample {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            System.out.println("当前值: " + i);
            if (i == 5) {
                System.out.println("条件满足，return 退出！");
                return;  // 退出 main 方法
            }
        }
        System.out.println("不会执行到这里");
    }
}
```

------

## **3. 在多线程环境中退出**

如果程序有多个线程运行，`System.exit(0)` 会 **终止整个 JVM**，但如果你只想终止当前线程，可以使用 `Thread.currentThread().interrupt()` 或 `return`。

**示例：退出当前线程**

```java
public class ThreadExitExample {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println("子线程运行: " + i);
                if (i == 5) {
                    System.out.println("子线程条件满足，退出！");
                    return;  // 只退出当前线程
                }
            }
        });

        thread.start();
    }
}
```

------

## **4. 触发异常并退出**

如果某个条件满足，可以抛出一个异常来终止程序执行。

**示例：抛出 `RuntimeException`**

```java
public class ExceptionExitExample {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            System.out.println("当前值: " + i);
            if (i == 5) {
                throw new RuntimeException("程序异常退出！");
            }
        }
    }
}
```

------

## **5. 通过halt(0) 强制终止**

`System.exit(0)` 允许 `Shutdown Hook`（钩子线程）执行，而 `Runtime.getRuntime().halt(0)` 会 **直接终止 JVM**，不会执行钩子。

```java
public class HaltExample {
    public static void main(String[] args) {
        System.out.println("程序启动");
        Runtime.getRuntime().halt(0); // 强制终止
        System.out.println("这句话不会被执行");
    }
}
```

------

## **最佳实践**

| 方法                                 | 适用场景                     | 备注                            |
| ------------------------------------ | ---------------------------- | ------------------------------- |
| `System.exit(0)`                     | 终止整个 JVM 进程            | 推荐用于命令行工具或单线程程序  |
| `return`                             | 退出 `main` 方法，不影响 JVM | 适用于单线程应用                |
| 抛出异常                             | 遇到错误时退出               | 可配合 `try-catch` 进行日志记录 |
| `Thread.currentThread().interrupt()` | 退出当前线程                 | 适用于多线程应用                |
| `Runtime.getRuntime().halt(0)`       | 强制终止 JVM，不执行钩子     | 不推荐，除非特殊情况            |

- **想终止整个程序？** → `System.exit(0)`
- **仅退出 `main` 方法？** → `return`
- **遇到异常时退出？** → 抛出 `RuntimeException`
- **多线程程序，只退出当前线程？** → `return` 或 `Thread.currentThread().interrupt()`
- **强制终止 JVM（不执行 `shutdown hooks`）？** → `Runtime.getRuntime().halt(0)`

# 七、Spring JPA 

## 1. JpaRepository

### 1.1 JpaRepository 与 Repository

`JpaRepository` 是 `Repository` 的子接口，专为 JPA 提供数据访问功能。

- `Repository`
  - 仅是一个标记接口，本身无任何方法。
  - 主要作用是告诉 Spring Data 该接口是一个仓库接口，Spring Data 会自动生成实现。
- `JpaRepository`
  - 提供 CRUD 方法，如 `save`、`findAll`、`findById`、`delete` 等。
  - 继承 `PagingAndSortingRepository`，支持分页和排序。
  - 支持批量操作，如 `saveAll`、`deleteAll`。

### 1.2 分页和排序

`JpaRepository` 继承 `PagingAndSortingRepository`，支持分页查询：

```java
Page<User> findAll(Pageable pageable);
```

使用 `PageRequest` 进行分页：

```java
Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
Page<User> users = userRepository.findAll(pageable);
```

## 2. Java 对象方法

### 2.1 equals 方法

用于比较两个对象是否相等，默认实现比较对象的内存地址。

### 2.2 hashCode 方法

返回对象的哈希码值，通常与 `equals` 方法配合使用，以保证 Hash 结构（如 `HashMap`）的正确性。

### 2.3 toString 方法

返回对象的字符串表示，默认格式为 `类名@哈希码`。

```java
@Override
public String toString() {
    return "User{name='" + name + "', age=" + age + "}";
}
```

## 3. 自定义模糊查询

### 3.1 方法命名规则

```java
List<User> findByNameLike(String name);
```

### 3.2 在 Service 层调用

```java
public List<User> findUsersByNamePrefix(String prefix) {
    return userRepository.findByNameLike("%" + prefix);
}
```

## 4. 持久化（Persistence）

### 4.1 什么是持久化？

持久化是指将数据从内存存储到持久化设备（如数据库、文件）。

### 4.2 序列化与反序列化

- **序列化**：对象 → 字节流
- **反序列化**：字节流 → 对象

```java
class User implements Serializable {
    private static final long serialVersionUID = 1L;
}
// 序列化
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.ser"));
oos.writeObject(user);
oos.close();

// 反序列化
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.ser"));
User user = (User) ois.readObject();
```

## 5. 多字段查询

### 5.1 方法命名规则

```java
List<User> findByNameAndAge(String name, Integer age);
```

### 5.2 @Query 注解

```java
@Query("SELECT u FROM User u WHERE u.name = :name AND u.age = :age")
List<User> findUsersByJPQL(@Param("name") String name, @Param("age") Integer age);
```

### 5.3 Specification 动态查询

```java
public static Specification<User> searchUsers(String name, Integer age) {
    return (root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(criteriaBuilder.equal(root.get("name"), name));
        }
        if (age != null) {
            predicates.add(criteriaBuilder.equal(root.get("age"), age));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
}
```

## 6. 乐观锁与悲观锁

### 6.1 乐观锁（@Version）

适用于读多写少、并发冲突概率低、对性能要求高的场景，能够提高系统的吞吐量，但可能会在冲突时导致重试或异常处理。

```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    
    @Version
    private Integer version;
}
```

### 6.2 悲观锁（@Lock）

适用于写操作频繁、并发冲突较高、需要强一致性的场景，能够确保数据的可靠性，但可能会降低性能。

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT u FROM User u WHERE u.id = :id")
User findByIdForUpdate(@Param("id") Long id);
```

### 6.3 手动锁定

#### **使用 `synchronized` 关键字**

`synchronized` 是 Java 内置的锁机制，它可以确保同一时刻只有一个线程能够访问被 `synchronized` 修饰的代码块或方法，从而避免并发冲突。

```java
public class SynchronizedExample {

    private int counter = 0;

    // 使用 synchronized 锁定方法
    public synchronized void increment() {
        counter++;
    }

    // 使用 synchronized 锁定代码块
    public void decrement() {
        synchronized (this) {  // 锁定当前对象
            counter--;
        }
    }

    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) {
        SynchronizedExample example = new SynchronizedExample();

        // 多个线程同时调用 increment 和 decrement 方法
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.decrement();
            }
        });

        t1.start();
        t2.start();
    }
}
```

- 使用 `synchronized` 可以确保同一时刻只有一个线程执行被同步的方法或代码块。
- 可以锁定整个方法，也可以只锁定方法中的一部分代码（使用同步代码块）。
- `synchronized` 锁定的是对象，所有对同一对象的访问都会被同步。

#### **使用 `Lock`**

`Lock` 是一个更灵活的锁机制，它提供了比 `synchronized` 更细粒度的控制，例如能够尝试获取锁、可中断的锁获取等。常见的实现是 `ReentrantLock`。

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockExample {

    private int counter = 0;
    private final Lock lock = new ReentrantLock();  // 创建一个可重入锁

    public void increment() {
        lock.lock();  // 获取锁
        try {
            counter++;
        } finally {
            lock.unlock();  // 确保锁被释放
        }
    }

    public void decrement() {
        lock.lock();
        try {
            counter--;
        } finally {
            lock.unlock();
        }
    }

    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) {
        LockExample example = new LockExample();

        // 创建并启动线程
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.decrement();
            }
        });

        t1.start();
        t2.start();
    }
}
```

- 使用 `ReentrantLock` 对 `counter` 进行加锁操作。
- `lock.lock()` 用于获取锁，`lock.unlock()` 用于释放锁。释放锁必须放在 `finally` 块中，以确保无论执行过程是否发生异常，锁都能够被释放。
- `Lock` 接口提供了更多控制，比如 `tryLock()`、`lockInterruptibly()` 等，可以更细致地控制锁的获取和释放。

#### **比较 `synchronized` 和 `Lock`**

| 特性             | `synchronized`                       | `Lock`（如 `ReentrantLock`）                |
| ---------------- | ------------------------------------ | ------------------------------------------- |
| 锁的粒度         | 锁定整个方法或代码块                 | 可以精确锁定代码块，灵活性高                |
| 是否支持中断     | 不支持中断                           | 可以通过 `lockInterruptibly()` 方法响应中断 |
| 锁的可重入性     | 支持（锁定方法内的代码块会自动重入） | 支持（`ReentrantLock` 为可重入锁）          |
| 是否能尝试获取锁 | 不支持（会一直阻塞直到获得锁）       | 支持（`tryLock()` 方法可以尝试获取锁）      |
| 死锁风险         | 较低，但需要谨慎使用多个锁的情况     | 死锁风险较高，需确保锁的顺序一致            |
| 性能             | 性能较差（在竞争激烈的环境下）       | 性能较好，提供更多优化和控制机制            |

## 7. Hibernate ORM

Hibernate 是 Java 的 ORM 框架，将 Java 对象映射到数据库表，减少 SQL 代码。

### 7.1 主要特性

1. **自动生成表结构**：Hibernate 可以根据 Java 类（实体类）及其关系自动生成数据库表结构。
2. **HQL（Hibernate 查询语言）**：与 SQL 类似，但用于操作 Java 对象而非数据库表。通过 HQL，可以编写数据库查询操作，但不需要关心底层数据库的具体实现。
3. **缓存机制**：Hibernate 内置支持缓存机制，可以提高性能，通过减少数据库查询次数。
4. **延迟加载**：支持延迟加载，只有在需要时才加载关联的对象，提升效率。
5. **事务管理**：与 JTA（Java 事务 API）集成，提供事务管理功能。
6. **跨数据库兼容性**：Hibernate 是数据库无关的，能够在不同的数据库之间切换，几乎不需要修改代码。

### 7.2 实体映射

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
}
```

### 7.3 HQL 查询

```java
@Query("FROM User WHERE name = :name")
List<User> findByName(@Param("name") String name);
```

# 八、分页器

`Page<XX>` 是 Spring Data 提供的一个接口，用于封装分页查询的结果。

## 1、常用方法

- **获取当前页数据列表**：`getContent()` 方法返回当前页的实体对象列表。
- **获取当前页码**：`getNumber()` 方法返回当前页的页码，页码从 0 开始计数。
- **获取每页大小**：`getSize()` 方法返回每页显示的记录数。
- **获取总页数**：`getTotalPages()` 方法返回查询结果的总页数。
- **获取总记录数**：`getTotalElements()` 方法返回查询结果的总记录数。

## 2、分页查询示例

在 Spring Data JPA 中，可以使用 `PageRequest` 生成 `Pageable` 对象，并传递给 `findAll` 方法来进行分页查询。

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
}
```

## 3、分页缓存

### 1. 缓存方案选择

可以使用 Spring Cache 结合 Redis 来实现分页缓存。Spring Cache 是 Spring 提供的一个缓存抽象层，它可以方便地与各种缓存实现集成，而 Redis 是一个高性能的内存数据库，适合用于缓存数据。

### 2. 配置 Spring Cache 和 Redis

在 `pom.xml` 中添加 Redis 和缓存相关的依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

然后，在配置类中启用缓存并配置 Redis：

```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // 设置缓存过期时间
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig)
                .build();
    }
}
```

### 3. 使用缓存注解进行分页缓存

```java
@Service
class UserService {
    @Autowired
    private UserRepository userRepository;
    @Cacheable(value = "userPages", key = "#root.methodName + '-' + #page + '-' + #size")
    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
    @CacheEvict(value = "userPages", allEntries = true)
    public void clearUserCache() {
        // 当用户数据变更时调用此方法清空分页缓存
    }
}
```

### 4. 说明

- `@Cacheable`：用于标记方法的返回值可以被缓存。`value` 指定缓存名称，`key` 生成缓存键，确保不同的分页请求有不同的缓存。
- `@CacheEvict`：用于清除缓存，`allEntries = true` 确保当数据变更时清除所有分页缓存，防止数据不一致。
- 结合 `PageRequest.of(page, size)` 进行分页查询，确保分页缓存逻辑一致。

通过以上方式，可以高效地实现分页查询，并使用 Redis 进行缓存优化，减少数据库查询压力，提高系统性能。

# 九、RestfulAPI

## 1、常见类型

| HTTP 方法 | 操作类型       | 描述                                                         | 示例                                                         |
| --------- | -------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| GET       | 查询           | 用于获取资源的信息，可以获取单个资源或者资源列表。           | `GET /users`：获取所有用户列表 `GET /users/1`：获取 ID 为 1 的用户信息 |
| POST      | 创建           | 用于在服务器上创建新的资源，通常会将资源的详细信息放在请求体中。 | `POST /users`：创建一个新用户，请求体中包含用户的详细信息，如姓名、邮箱等 |
| PUT       | 更新           | 用于更新已存在的资源的全部信息，需要提供资源的完整数据。     | `PUT /users/1`：更新 ID 为 1 的用户信息，请求体中包含该用户完整的最新信息 |
| PATCH     | 部分更新       | 用于更新已存在资源的部分信息，只需要提供需要更新的字段及其新值。 | `PATCH /users/1`：更新 ID 为 1 的用户的邮箱信息，请求体中只需包含邮箱字段的新值 |
| DELETE    | 删除           | 用于删除指定的资源。                                         | `DELETE /users/1`：删除 ID 为 1 的用户                       |
| OPTIONS   | 获取支持的操作 | 用于获取服务器针对特定资源所支持的 HTTP 请求方法，也可以用于检查服务器的性能、可用性等。 | `OPTIONS /users`：获取针对 `/users` 资源所支持的 HTTP 方法   |
| HEAD      | 获取资源元信息 | 与 GET 请求类似，但服务器只返回响应头，不返回响应体，用于获取资源的元信息，如资源的大小、修改时间等。 | `HEAD /users/1`：获取 ID 为 1 的用户信息的元数据，如最后     |

## 2、CURL

1. 发送 GET 请求

   ```bash
   curl https://example.com
   ```

2. 发送 POST 请求

   当需要向服务器提交数据时，可使用 POST 请求

   ```bash
   curl -X POST -d "key1=value1&key2=value2" https://example.com/api
   ```

   - `-X POST`：指定请求方法为 POST。
   - `-d "key1=value1&key2=value2"`：用于传递 POST 数据，数据格式为键值对。

   若要发送 JSON 数据，可按如下方式操作

   ```bash
   curl -X POST -H "Content-Type: application/json" -d '{"key1": "value1", "key2": "value2"}' https://example.com/api
   ```

   - `-H "Content-Type: application/json"`：设置请求头，表明发送的数据为 JSON 格式。
   - `-d '{"key1": "value1", "key2": "value2"}'`：传递 JSON 格式的数据。

3. 发送 PUT 请求

   ```bash
   curl -X PUT -d "updated_key=updated_value" https://example.com/api/resource/1
   ```

4. 发送 DELETE 请求

   ```bash
   curl -X DELETE https://example.com/api/resource/1
   ```

5. 设置请求头

   ```bash
   curl -H "Authorization: Bearer your_token" https://example.com/api
   ```

6. 跟随重定向

   ```bash
   curl -L https://example.com
   ```

7. 显示响应头信息

   ```bash
   curl -i https://example.com
   ```

# 十、Spring 事务管理

## 1. @Transactional 的核心作用

`@Transactional` 是 Spring 提供的声明式事务管理注解，其核心作用包括：

- **声明事务边界**：标记方法或类，指示该方法应在事务上下文中执行。
- **自动管理事务**：Spring 负责事务的开启、提交或回滚，无需手动调用 `commit()` 或 `rollback()`。
- **支持多种事务管理器**：兼容 JDBC、JPA、Hibernate、JTA 等数据访问技术。

------

## 2. 基本用法

### 2.1 启用事务管理

在 Spring Boot 中，`@EnableTransactionManagement` 用于开启事务支持（通常已默认启用）。

### 2.2 注解位置

- **类级别**：类中的所有方法默认继承事务配置。
- **方法级别**：可以覆盖类级别配置，为单个方法定制事务行为。

```java
@Service
@Transactional // 类级别，所有方法默认继承事务
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 方法级别事务覆盖
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
```

------

## 3. 事务关键属性

### 3.1 传播行为（propagation）

| 传播行为          | 描述                                                       |
| ----------------- | ---------------------------------------------------------- |
| `REQUIRED` (默认) | 当前方法必须运行在事务中，若已有事务则加入，否则新建一个。 |
| `REQUIRES_NEW`    | 始终启动新事务，挂起当前事务（若存在）。                   |
| `SUPPORTS`        | 若有事务则加入，无事务则以非事务方式执行。                 |
| `NOT_SUPPORTED`   | 以非事务方式执行，挂起当前事务（若存在）。                 |
| `MANDATORY`       | 必须运行在已有事务中，否则抛出异常。                       |
| `NEVER`           | 以非事务方式执行，若有事务则抛出异常。                     |
| `NESTED`          | 在当前事务中嵌套子事务（需数据库支持，如 MySQL InnoDB）。  |

### 3.2 隔离级别（isolation）

| 隔离级别           | 描述                                                         |
| ------------------ | ------------------------------------------------------------ |
| `DEFAULT`          | 使用数据库默认隔离级别（如 MySQL 默认为 `REPEATABLE_READ`）。 |
| `READ_UNCOMMITTED` | 允许读取未提交数据（可能产生脏读、不可重复读、幻读）。       |
| `READ_COMMITTED`   | 只能读取已提交数据（避免脏读，可能有不可重复读、幻读）。     |
| `REPEATABLE_READ`  | 确保事务期间多次读取数据一致（避免脏读、不可重复读，但可能有幻读）。 |
| `SERIALIZABLE`     | 最高隔离级别，完全串行化执行（避免所有并发问题，但性能最低）。 |

### 3.3 事务回滚（rollbackFor / noRollbackFor）

- **`rollbackFor`**：指定哪些异常应触发回滚（默认仅对 `RuntimeException` 和 `Error` 回滚）。
- **`noRollbackFor`**：指定哪些异常不应触发回滚。

```java
@Transactional(rollbackFor = CustomException.class)
public void methodC() throws CustomException {
    // 抛出 CustomException 时回滚事务
}
```

### 3.4 事务超时（timeout）

设置事务超时时间（秒），超时后自动回滚，默认依赖数据库配置。

```java
@Transactional(timeout = 10) // 10秒超时
public void methodD() {
    // 超时将触发回滚
}
```

### 3.5 只读事务（readOnly）

优化查询性能，提示数据库启用只读模式（如 MySQL 会禁用写操作缓存）。

- `true`：优化查询性能，写操作可能失败。
- `false`（默认）：允许读写操作。

```java
@Transactional(readOnly = true)
public List<User> findAllUsers() {
    return userRepository.findAll();
}
```

------

## 4. 事务的底层实现

- **AOP 代理**：Spring 通过 AOP（动态代理）实现事务管理，调用 `@Transactional` 方法时，实际调用的是代理对象的方法。

- 事务管理器

  ```
  PlatformTransactionManager
  ```

   是事务管理的核心接口，具体实现包括：

  - `DataSourceTransactionManager`（JDBC）
  - `JpaTransactionManager`（JPA）

- **事务同步**：`TransactionSynchronizationManager` 绑定资源（如数据库连接）到当前线程。

------

## 5. 最佳实践与常见问题

### 5.1 事务方法应声明为 `public`

**原因**：Spring AOP 代理只能拦截 `public` 方法，非 `public` 方法的 `@Transactional` 注解无效。

### 5.2 避免自调用导致事务失效

**问题**：同一类内方法 A 调用方法 B，即使 B 有 `@Transactional`，事务也不会生效。

**解决方案**：

- 将方法 B 移到另一个类。
- 使用 `AopContext.currentProxy()` 获取代理对象后调用：

```java
((UserService) AopContext.currentProxy()).methodB();
```

### 5.3 正确处理异常

**默认回滚规则**：

- 仅对未捕获的 `RuntimeException` 和 `Error` 进行回滚。
- **受检异常（如 `IOException`）默认不会回滚**，若需回滚，需要显式配置：

```java
@Transactional(rollbackFor = Exception.class)
```

### 5.4 确保事务边界清晰

- **推荐**：在 **Service 层** 管理事务，而非 DAO 层。
- **避免长事务**：事务应尽量短小，减少数据库锁竞争，提高并发能力。

------

通过合理使用 `@Transactional`，可以有效管理 Spring 应用中的事务，确保数据一致性和高效性。

# 十一、数据库查询优化

## 1. 避免 N+1 查询

N+1 查询问题通常出现在使用 ORM（对象关系映射）框架时。查询主实体列表时，每个主实体会额外查询一次关联实体，导致数据库发出 N+1 次查询，从而影响性能。

### **优化方法：**

- **Fetch Join**：使用 JPQL 中的 `FETCH JOIN`，一次性获取主实体和关联实体的数据，避免多次查询。

  ```java
  String jpql = "SELECT u FROM User u JOIN FETCH u.orders WHERE u.id IN :ids";
  TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
  query.setParameter("ids", userIds);
  List<User> users = query.getResultList();
  ```

- **@EntityGraph**：在 Spring Data JPA 中，使用 `@EntityGraph` 注解加载关联实体，避免 N+1 查询。

  ```java
  @Repository
  public interface UserRepository extends JpaRepository<User, Long> {
      @EntityGraph(attributePaths = "orders")
      List<User> findAllByIdIn(List<Long> ids);
  }
  ```

## 2. 大数据分页优化

使用 `LIMIT` 和 `OFFSET` 进行深度分页时，随着 `OFFSET` 增大，数据库需要扫描大量记录，导致性能下降。

### **优化方法：**

- **游标分页**：使用游标分页机制，避免大规模跳过记录，提升性能。

  ```sql
  -- 传统分页（低效）
  SELECT * FROM orders ORDER BY id LIMIT 10 OFFSET 100000;
  
  -- 游标分页（高效）
  SELECT * FROM orders WHERE id > last_id ORDER BY id LIMIT 10;
  ```

## 3. 索引优化

索引加速查询，但不当的索引配置可能导致性能问题。

### **优化方法：**

- **单列索引**：为常用查询条件的列创建单列索引。

  ```sql
  CREATE INDEX idx_user_name ON users (name);
  ```

- **组合索引**：当查询条件涉及多个列时，创建组合索引，并注意索引顺序。

  ```sql
  CREATE INDEX idx_user_name_age ON users (name, age);
  ```

## 4. 使用 DTO 投影

查询时，如果返回实体对象，可能会加载不必要的字段，增加数据库负担和数据传输开销。

### **优化方法：**

- **DTO 投影**：使用 DTO（数据传输对象）仅返回所需字段，减少数据加载。

  ```java
  public class UserDTO {
      private Long id;
      private String username;
  
      // Getter 和 Setter
  }
  
  String jpql = "SELECT new com.example.dto.UserDto(u.id, u.username) FROM User u";
  TypedQuery<UserDto> query = entityManager.createQuery(jpql, UserDto.class);
  List<UserDto> userDtos = query.getResultList();
  ```

## 5. 批量操作优化

批量插入或更新时，逐行操作会导致多次数据库交互，降低性能。

### **优化方法：**

- **JPA 批量处理**：启用 JDBC 批量处理和配置 `batch_size`，提高批量操作效率。

  ```yaml
  spring:
    jpa:
      properties:
        hibernate:
          jdbc.batch_size: 100
          order_inserts: true
  ```

  ```java
  @Transactional
  public void batchInsert(List<User> users) {
      users.forEach(userRepository::save);
  }
  ```

## 6. 使用二级缓存

数据库频繁查询同一数据时，二级缓存可以减少访问数据库的次数。

### **优化方法：**

- **配置二级缓存**：在 Spring Boot 中启用 Hibernate 二级缓存，例如使用 Ehcache。

  ```xml
  <prop key="hibernate.cache.use_second_level_cache">true</prop>
  <prop key="hibernate.cache.region.factory_class">org.hibernate.cache.ehcache.EhCacheRegionFactory</prop>
  ```

- **实体缓存**：使用 `@Cacheable` 注解启用二级缓存。

  ```java
  @Entity
  @Cacheable
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  public class User { 
      // 实体类定义 
  }
  ```

### **适用场景：**

- 高频读、低频写数据（如商品分类、配置表）。
- 合理设置过期时间，避免缓存击穿。

## 7. 避免长事务

长事务会持有数据库锁的时间较长，可能导致锁竞争和死锁，影响并发性能。

### **优化方法：**

- **控制事务范围**：尽量缩小事务范围，避免不必要的操作。

  ```java
  @Service
  public class UserService {
      @Transactional
      public void updateUser(User user) {
          userRepository.save(user);
      }
  }
  ```

- **只读事务**：对于只读操作，使用 `@Transactional(readOnly = true)` 注解来优化数据库性能。

  ```java
  @Service
  public class UserService {
      @Transactional(readOnly = true)
      public User getUserById(Long id) {
          return userRepository.findById(id).orElse(null);
      }
  }
  ```

## 8. 日志监控

通过监控 SQL 执行情况，能够找出性能瓶颈，优化慢查询。

### **优化方法：**

- **开启 Hibernate 日志**：配置 Hibernate 日志级别，查看 SQL 执行情况。

  ```properties
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.format_sql=true
  logging.level.org.hibernate.SQL=DEBUG
  logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
  ```

- **分析慢查询**：使用数据库的性能监控工具（如 MySQL 的慢查询日志），找出慢查询进行优化。

## 总结

| **优化方向**  | **具体措施**                              | **关键收益**         |
| ------------- | ----------------------------------------- | -------------------- |
| 避免 N+1 查询 | Fetch Join / @EntityGraph                 | 减少冗余查询         |
| 分页优化      | 游标分页（Keyset Pagination）             | 避免深度分页         |
| 索引优化      | 组合索引覆盖查询条件                      | 加速查询排序         |
| DTO 投影      | 只查询需要的字段                          | 减少数据加载         |
| 批量操作      | JDBC 批量处理 / 配置 `batch_size`/saveAll | 提升写入吞吐量       |
| 二级缓存      | 缓存高频读数据                            | 减少数据库访问       |
| 避免长事务    | 控制事务范围 + 只读事务                   | 减少锁竞争与连接占用 |
| 日志监控      | 开启Hibernate日志，分析慢查询             | 快速定位性能瓶颈     |

# 十二、数据库误删除

| 情况            | 解决方案                           |
| --------------- | ---------------------------------- |
| **有完整备份**  | 直接恢复 `mysqldump`               |
| **有 `binlog`** | 通过 `mysqlbinlog` 解析并恢复      |
| **事务未提交**  | `ROLLBACK;`                        |
| **MySQL 8.0+**  | `FLASHBACK TABLE`                  |
| **InnoDB**      | `undo log` + `redo log`            |
| **没有日志**    | 磁盘恢复工具 (`undrop-for-innodb`) |
