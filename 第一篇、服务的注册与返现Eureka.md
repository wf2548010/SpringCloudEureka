一、Spring Cloud说明
        Spring Cloud为开发人员提供了快速构建分布式系统的一些工具，包括配置管理、服务发现、断路器、
        路由、微代理、事件总线、全局锁、决策竞选、分布式会话等等。他运行环境简单，可以再开发人员的
        电脑上跑，另外说明Spring Cloud是基于Spring Boot的。
二、创建服务注册中心
        2.1 首先创建一个maven主工程。  
        首先创建一个主Maven工程，在其pom文件引入依赖，spring Boot版本为2.0.3.RELEASE，Spring Cloud版本为Finchley.RELEASE。这个pom文件作为父pom文件，起到依赖版本控制的作用，其他module工程继承该pom。这一系列文章全部采用这种模式，其他文章的pom跟这个pom一样。再次说明一下，以后不再重复引入。代码如下
        <?xml version="1.0" encoding="UTF-8"?>
        
        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
            <modelVersion>4.0.0</modelVersion>
            <groupId>com.forezp</groupId>
            <artifactId>sc-f-chapter1</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <packaging>pom</packaging>
            <name>sc-f-chapter1</name>
            <description>Demo project for Spring Boot</description>
            
            <parent>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-parent</artifactId>
                <version>2.0.3.RELEASE</version>
                <relativePath/>
            </parent>
            
            <modules>
                <module>eureka-server</module>
                <module>service-hi</module>
            </modules>
        
            <properties>
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
                <java.version>1.8</java.version>
                <spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
            </properties>
        
            <dependencies>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-test</artifactId>
                    <scope>test</scope>
                </dependency>
            </dependencies>
        
            <dependencyManagement>
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.cloud</groupId>
                        <artifactId>spring-cloud-dependencies</artifactId>
                        <version>${spring-cloud.version}</version>
                        <type>pom</type>
                        <scope>import</scope>
                    </dependency>
                </dependencies>
            </dependencyManagement>
        
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-maven-plugin</artifactId>
                    </plugin>
                </plugins>
            </build>
        
        </project>
        
        2.2 然后创建2个model工程:**一个model工程作为服务注册中心，即Eureka Server,另一个作为Eureka Client。
        下面以创建server为例子，详细说明创建过程：
        右键工程->创建model-> 选择spring initialir 
        下一步->选择cloud discovery->eureka server ,然后一直下一步就行了。
        创建完后的工程，其pom.xml继承了父pom文件，并引入spring-cloud-starter-netflix-eureka-server的依赖，代码如下：
        
        <?xml version="1.0" encoding="UTF-8"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
            <modelVersion>4.0.0</modelVersion>
        
            <groupId>com.forezp</groupId>
            <artifactId>eureka-server</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <packaging>jar</packaging>
        
            <name>eureka-server</name>
            <description>Demo project for Spring Boot</description>
        
            <parent>
                <groupId>com.forezp</groupId>
                <artifactId>sc-f-chapter1</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </parent>
        
            <dependencies>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
                </dependency>
            </dependencies>
        
        </project>
        
        2.3 启动一个服务注册中心，只需要一个注解@EnableEurekaServer，这个注解需要在springboot工程的启动application类上加：
        
        @SpringBootApplication
        @EnableEurekaServer
        public class EurekaServerApplication {
        
            public static void main(String[] args) {
                SpringApplication.run( EurekaServerApplication.class, args );
            }
        }
        
        2.4 **eureka是一个高可用的组件，它没有后端缓存，每一个实例注册之后需要向注册中心发送心跳（因此可以在内存中完成），在默认情况下erureka server也是一个eureka client ,必须要指定一个 server。eureka server的配置文件appication.yml：
        
        server:
          port: 8761
        
        eureka:
          instance:
            hostname: localhost
          client:
            registerWithEureka: false
            fetchRegistry: false
            serviceUrl:
              defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
        
        spring:
          application:
            name: eurka-server
        
        
        通过eureka.client.registerWithEureka：false和fetchRegistry：false来表明自己是一个eureka server.
        
        eureka server 是有界面的，启动工程,打开浏览器访问：
        http://localhost:8761
     
   
三、创建一个服务提供者（Eureka Client）
        当Client向Server注册时，他会提供一些元数据，例如主机和端口，URL,主页等。Eureka Server从每个Client实例接收
        心跳消息。如果心跳超时，则通常将改实例从注册Server中删除
        
        创建过程同server类似,创建完pom.xml如下：     
        
        
        <?xml version="1.0" encoding="UTF-8"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
            <modelVersion>4.0.0</modelVersion>
        
            <groupId>com.forezp</groupId>
            <artifactId>service-hi</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <packaging>jar</packaging>
        
            <name>service-hi</name>
            <description>Demo project for Spring Boot</description>
        
            <parent>
                <groupId>com.forezp</groupId>
                <artifactId>sc-f-chapter1</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </parent>
        
            <dependencies>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
                </dependency>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-web</artifactId>
                </dependency>
            </dependencies>
        
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-maven-plugin</artifactId>
                    </plugin>
                </plugins>
            </build>
        
        
        </project>
        
        通过注解@EnableEurekaClient 表明自己是一个eurekaclient. 
        
        @SpringBootApplication
        @EnableEurekaClient
        @RestController
        public class ServiceHiApplication {
        
            public static void main(String[] args) {
                SpringApplication.run( ServiceHiApplication.class, args );
            }
        
            @Value("${server.port}")
            String port;
        
            @RequestMapping("/hi")
            public String home(@RequestParam(value = "name", defaultValue = "forezp") String name) {
                return "hi " + name + " ,i am from port:" + port;
            }
        
        }
        
        
        仅仅@EnableEurekaClient是不够的，还需要在配置文件中注明自己的服务注册中心的地址，application.yml配置文件如下：
        
        需要指明spring.application.name,这个很重要，这在以后的服务与服务之间相互调用一般都是根据这个name 。
        启动工程，打开http://localhost:8761
        
        你会发现一个服务已经注册在服务中了，服务名为SERVICE-HI ,端口为7862
        
        这时打开 http://localhost:8762/hi?name=forezp ，你会在浏览器上看到 :
        hi forezp,i am from port:8762