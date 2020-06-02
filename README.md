# 基于Spring Boot的maven分布式项目框架

## 项目介绍

在微服务架构中，传统的 maven 项目已经无法满足，开始走向分布式架构，本项目主要搭建一个空的 maven 分布式架构，可以运用到实际项目中进行扩展。

这里搭建的是基于 maven 的分布式工程，因为在一个项目中，多个微服务是属于同一个工程，只不过是提供不同的服务而已，再加上 IDEA 是默认一个窗口打开一个项目工程（这点和 eclipse 不同），如果项目大，不用 maven 聚合工程的话，那估计会打开十几个窗口……会崩溃……而且在架构上，也应该使用 maven 分布式工程来搭建微服务架构。这里手把手教大家在 IDEA 中搭建基于 maven 分布式的 Spring Cloud 微服务工程架构。

## 1. maven分布式工程架构

首先来看一下 maven 分布式工程的基本架构，如下：
> microservice  
> ---- microservice-common  
> ---- microservice-order-provider  
> ---- microservice-order-consumer  

在 IDEA 中，并没有这个结构化的展示，这几个模块都是平级的方式展现在我们眼前，但是彼此有依赖关系，体现在 `pom.xml` 文件中，在下文会详细说明。`microservice`  为父工程模块，主要用来定义整个分布式工程的依赖版本；`microservice-common` 为公共模块，主要用来定义一些公共的组件，比如实体类等；`microservice-order-provider` 为订单服务提供者模块，提供订单信息，实际项目中可能还有其他服务提供模块；`microservice-order-consumer` 为服务消费模块，当然了，消费模块也可能有很多，这里只创建一个，实际项目中可以在此基础上进行扩展。

## 2. maven父工程microservice的搭建

打开 IDEA， File -> New -> New Project，然后选择 Empty Project，如下。

![maven分布式项目的创建](http://p99jlm9k5.bkt.clouddn.com/csdn/drk/springcloud/1-1.png)

Next，然后给 maven 分布式项目起个名儿，如 maven_distributed。

![maven分布式项目的创建](http://p99jlm9k5.bkt.clouddn.com/csdn/drk/springcloud/1-2.png)

接下来会弹出窗口让我们新建 modules，点击 + 号，新建一个父工程，也就是一个父 module。然后我们选择 maven 工程，选择 jdk 版本和模板，模板也可以不选择，我这里就没有选择，自己搭建即可。

![maven父工程的搭建](http://p99jlm9k5.bkt.clouddn.com/csdn/drk/springcloud/1-4.png)

Next，需要输入 GroupId 和 ArtifactId，这和普通的 maven 工程没什么两样，如：
> GroupId：com.itcodai
> ArtifactId：microservice

创建好之后，该父工程 microservice 是个空的 maven 工程，只有 src 目录和 pom.xml 文件，在父工程中我们主要做什么呢？父工程中主要用来定义一些依赖的版本，子工程在创建的时候继承该父工程，就可以使用对用的依赖，不需要指定版本号。同时，如果有版本相关的修改，只要在父工程中修改即可，这是 maven 分布式工程的好处之一，它就相当于 Java 中的抽象父类。

新创建的空 maven 工程没有指定其 `<packaging>` 类型，maven 父工程需要指定 `packaging` 类型为 pom，如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itcodai</groupId>
    <artifactId>microservice</artifactId>
    <version>1.0-SNAPSHOT</version>
    
    <packaging>pom</packaging>
    
</project>
```
然后我们来定义一些依赖和相应的版本，依赖的版本我们定义在 `properties` 标签内，如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project ...>
	<!-- 省略其他内容 -->
	<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring-cloud.version>Edgware.SR1</spring-cloud.version>
        <spring-boot.version>2.0.3.RELEASE</spring-boot.version>
        <mysql.version>5.1.46</mysql.version>
        <mybatis.version>1.3.2</mybatis.version>
        <lombok.version>1.16.18</lombok.version>
    </properties>
</prject>
```
如上，我们定义了项目的编码为 UTF-8，编译版本为 jdk8，其他依赖版本为：
* Spring Cloud 的版本为 Edgware.SR1，Spring Cloud 的版本号定义的非常“奇怪”，不是我们平常看到的几点几版本，它是由地名儿来命名的；
* Spring Boot 版本为当前最新版本 2.0.3.RELEASE；
* mysql 版本为 5.1.14；
* mybatis 版本为 1.3.2；
* lombok版本为 1.16.8.

其他依赖在项目使用到时，再添加即可，这里先定义这么多。在定义版本时，要注意的是不同的依赖版本之间会有影响，有些最新的版本不支持其他依赖的低版本一起使用，比如 mysql 的版本太低就不行，例如 5.0.4 版本就无法和最新的 mybatis 一起使用，这些在实际项目中都踩过坑，所以大家在学习的时候要多尝试，多总结，最新版本不是不好用，有时候是用不好。但是只要认真探索，多踩坑才能进步。

定义了依赖版本之后，接下来我们就在父工程中定义依赖管理，放在 `<dependencyManagement>` 标签中，如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project ...>
	<!-- 省略其他内容 -->
	<dependencyManagement>
        <dependencies>
            <!-- 定义 spring cloud 版本 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- 定义 spring boot 版本 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- 定义 mysql 版本 -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <!-- 定义 mybatis 版本 -->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybatis.version}</version>
            </dependency>
            <!-- 定义 lombok 版本 -->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```
使用 `${}` 来定义上面 `<properties>` 标签中定义的版本即可。最后我们定义一下 maven 插件，如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project ...>
	<!-- 省略其他内容 -->
	<build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## 3. maven子工程microservice-common模块搭建

接下来我们搭建 maven 子工程中的公共模块 `microservice-common`，新建子模块，我们要选择 module，步骤为： File -> New -> Module，然后选择 maven，这和上面建立父工程一模一样，下一步的时候需要注意：

![maven分布式工程公共模块](http://p99jlm9k5.bkt.clouddn.com/csdn/drk/springcloud/1-5.png)

这里要注意的是，使用 IDEA 创建子模块的时候，不需要选择 “Add as module to” 这一项，默认是选择了我们刚刚创建的父工程 `microservice`，我们在 Parent 项选择刚刚创建的 `microservice` 模块即可，然后给该子模块起名为 `microservice-common`。创建好之后，我们来看一下该模块的 pom.xml 文件：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>microservice</artifactId>
        <groupId>com.itcodai</groupId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../microservice/pom.xml</relativePath>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>microservice-common</artifactId>
    
</project>
```
可以看到，在 `microservice-common` 模块中，有个 `<parent>` 标签，里面内容是父工程 `microservice` 的相关信息，包括依赖的路径也标出来了，这样 `microservice-common` 模块和 `microservice` 模块就建立了关联。子模块的 `<packaging>` 类型我们定义成 jar 即可。
```xml
<packaging>jar</packaging>
```
在 `microservice-common` 模块我们主要定义一些公共的组件，本节课中，我们先定义一个订单实体类，因为这个实体在其他模块也要用到，所以我们定义在这个公共模块中，那么在该模块中，目前我们只需要引入 lombok 即可。如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project ...>
    <!-- 省略其他内容 -->

    <!-- 当前Module需要用到的依赖，按自己需求添加，版本号在父类已经定义了，这里不需要再次定义 -->
    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
</project>
```
lombok 依赖主要是用在实体类上，我们不用自己去定义构造方法，不用自己去生成 get 和 set 方法了，很方便。引入了依赖之后，我们去创建一个订单实体类。
```java
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TOrder {

    private Long id; // 主键id
    private String name; // 商品名称
    private Double price; // 商品价格
    private String dbSource; // 所存的数据库
}
```
解释一下该实体类上的几个注解：
> `@AllArgsConstructor` 注解：表示生成带有所有属性的构造方法
> `@NoArgsConstructor` 注解：表示生成不带参数的构方法
> `@Data` 注解：表示生成get和set方法

可以看出，使用 lombok 很方便，省去了很多繁琐的代码。到此为止，`microservice-common` 模块基本上就写完了，在实际项目中，可能还有别的实体类或者工具类等需要定义，视具体情况而定。

接下来我们需要把这个模块打包，让其他模块引用，这样其他模块就可以使用该模块中的公共组件了，就跟普通的 maven 依赖一样。如何打包呢？点开右边的 Maven Projects，我们可以看到目前项目中有两个模块，一个父工程和一个公共子工程模块，然后打开公共模块，执行 maven 中的 clean 和 install 命令即可。如下：

![将公共模块打包](http://p99jlm9k5.bkt.clouddn.com/csdn/drk/springcloud/1-6.png)

在下一节，我们创建订单服务提供模块，在订单服务提供模块中，我们引入该公共模块。

## 3. maven子工程microservice-order-provider模块搭建

接下来我们搭建 maven 子工程中的订单服务提供模块 `microservice-order-provider`。
新建子模块的方法和上面 `microservice-common` 模块一模一样，在命名的时候命名为 `microservice-order-provider` 即可。完成之后，来看一下该模块中的 pom 文件：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project ...>
    <parent>
        <artifactId>microservice</artifactId>
        <groupId>com.itcodai</groupId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../microservice/pom.xml</relativePath>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>microservice-order-provider</artifactId>
    <packaging>jar</packaging>

    <!-- 当前Module需要用到的依赖，按自己需求添加，版本号在父类已经定义了，这里不需要再次定义 -->
    <dependencies>
        <!-- 引入自己定义的 microservice-common 通用包，可以使用common模块中的TOrder类 -->
        <dependency>
            <groupId>com.itcodai</groupId>
            <artifactId>microservice-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <!-- spring boot web 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <!-- mybatis 依赖 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <!-- mysql 依赖 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
    </dependencies>
</project>
```
可以看出，引入上一节我们自定义的 `microservice-common` 模块和引入其他依赖没什么两样，版本我们使用 `${project.version}` 来跟着项目版本走即可。其他依赖我们只需要引入即可，不需要定义版本号。

由于这是服务提供模块，我们需要在表中查询出订单信息，然后将信息通过接口提供给调用方，所以在该模块中，我们需要整合一下 mybatis，mybatis 的整合我在 Spring Boot 课程中有详细的讲解，不是这门课的重点，mybatis 的相关配置和代码可以本课程下载源码查看，这里主要使用注解的方式。

我们看一下 application.yml 配置文件中的部分信息：
```yml
# 服务端口号
server:
  port: 8001
spring:
  application:
    name: microservice-order # 对外暴露的服务名称
```
`spring.application.name` 是用来定义服务的名称，在后面的课程会详细的说明。TOrder实体对应的表以及数据见 order.sql 脚本文件。我们看一下该表中的数据：

![order表数据](http://p99jlm9k5.bkt.clouddn.com/csdn/drk/springcloud/1-7.png)

在 OrderMapper 中写两个方法来查询表中信息：
```java
public interface OrderMapper {

    @Select("select * from t_order where id = #{id}")
    TOrder findById(Long id);

    @Select("select * from t_order")
    List<TOrder> findAll();
}
```
我们在 Controller 层写两个接口来测试一下：
```java
@RestController
@RequestMapping("/provider/order")
public class OrderProviderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/get/{id}")
    public TOrder getOrder(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/get/list")
    public List<TOrder> getAll() {
        return orderService.findAll();
    }
}
```
在浏览器中输入`localhost:8001/provider/order/get/list`，如果能查出来两条记录，并以 json 格式输出到浏览器，如下，说明服务提供模块功能正常。
```json
[{"id":1,"name":"跟武哥一起学 Spring Boot","price":39.99,"dbSource":"microservice01"},{"id":2,"name":"跟武哥一起学 Spring cloud","price":39.99,"dbSource":"microservice01"}]
```

## 4. maven子工程microservice-order-consumer模块搭建

接下来我们搭建 maven 子工程中的订单服务消费模块 `microservice-order-consumer`，
新建子模块的方法和上面两个子 模块一模一样，在命名的时候命名为 `microservice-order-consumer` 即可。完成之后，来看一下该模块中的 pom 文件：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project ...>
    <parent>
        <artifactId>microservice</artifactId>
        <groupId>com.itcodai</groupId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../microservice/pom.xml</relativePath>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>microservice-order-consumer</artifactId>
    <packaging>jar</packaging>

    <!-- 当前Module需要用到的依赖，按自己需求添加，版本号在父类已经定义了，这里不需要再次定义 -->
    <dependencies>
        <!-- 引入自己定义的 microservice-common 通用包，可以使用common模块中的Order类 -->
        <dependency>
            <groupId>com.itcodai</groupId>
            <artifactId>microservice-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <!-- spring boot web 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>
</project>
```
因为 `microservice-order-consumer` 模块主要是用来调用 `microservice-order-provider` 模块提供的订单信息，所以在 `microservice-order-consumer` 模块中我们不需要引入 mybatis 和 mysql 相关的依赖，因为不用操作数据库。当然了，在实际项目中，根据具体需求，如果需要操作其他表，那么还是要引入持久层依赖的。

在微服务都是以 HTTP 接口的形式暴露自身服务的，因此在调用远程服务时就必须使用 HTTP 客户端。Spring Boot 中使用的是 RestTemplate，首先，我们写一个配置类，将 RestTemplate 作为一个 Bean 交给 Spring 来管理。
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTmplateConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
```

有了 RestTemplate，接下来我们可以在 Controller 中注入该 RestTemplate 来调用 `microservice-order-provider01` 提供的服务了，如下：
```java
@RestController
@RequestMapping("/consumer/order")
public class OrderConsumerController {

    // 订单服务提供者模块的 url 前缀
    private static final String ORDER_PROVIDER_URL_PREFIX = "http://localhost:8001";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/get/{id}")
    public TOrder getOrder(@PathVariable Long id) {

        return restTemplate.getForObject(ORDER_PROVIDER_URL_PREFIX + "/provider/order/get/" + id, TOrder.class);
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/get/list")
    public List<TOrder> getAll() {
        return restTemplate.getForObject(ORDER_PROVIDER_URL_PREFIX + "/provider/order/get/list", List.class);
    }
}
```
我们来讲解一下 RestTemplate 的使用，在 Controller 中，我们定义了一个订单服务提供者的 url 前缀，这是 `microservice-order-provider` 的服务地址，因为我们等会要远程调用这个服务。`restTemplate.getForObject` 方法是 GET 请求方法，它有两个参数：
> url：请求地址  
> ResponseBean.class：HTTP 相应被转换成的对象类型  

对于实体类或者 List 均可以接收，同样地，还有处理 POST 请求的方法 `restTemplate.postForObject`，该方法有三个参数，如下：
> url：请求地址  
> requestMap：请求参数，封装到map中   
> ResponseBean.class：HTTP响应被转换成的对象类型  

那么整个流程即：订单消费模块不直接请求数据库，而是通过 http 远程调用订单提供模块的服务来获取订单信息。也就是说，在微服务里，每个服务只关注自身的逻辑和实现，不用管其他服务的实现，需要获取某个服务的数据时，只要调用该服务提供的接口即可获取相应的数据。实现了每个服务专注于自身的逻辑，服务之间解耦合。

我们来测试一下，启动 `microservice-order-provider` 和 `microservice-order-consumer` 两个服务，在浏览器中输入 `localhost:8080/consumer/order/get/list`，如果浏览器中能查到数据库中的两条记录，说明服务调用成功。
```json
[{"id":1,"name":"跟武哥一起学 Spring Boot","price":39.99,"dbSource":"microservice01"},{"id":2,"name":"跟武哥一起学 Spring cloud","price":39.99,"dbSource":"microservice01"}]
```
到此为止，基于 maven 分布式的微服务架构就搭建好了，实际项目中可以直接拿这个来扩展。   
如果觉得对你有用，可以请作者喝杯咖啡~