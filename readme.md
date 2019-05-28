## What all has been touched in this project, so far
. installation and setup of postgresql db  
. configuring postgresql with springboot application  
. creating and exposing sample REST APIs - 2 APIs  
. exposing the API with a JSON response  
. installation and usage of POSTMAN REST API testing tool  
. writing unit and integration tests for the persistence, service and controller/mvc layers for the REST API  
. code review performed with PMD, Checkstyle and Spotbugs.  
. exception handling in spring boot
. caching with redis

## Create table
. create table test (    
 id integer PRIMARY KEY,
 name text not null
);

## View all tables in postgresql db
. select * from pg_catalog.pg_tables;

## Insert
. insert into test (id) values (1);

## Sequence
. CREATE SEQUENCE judgements_id_seq OWNED BY test.id;

## PostgreSQL - Hibernate Sequence
. GenerationType.AUTO is not working w/ postgresql and hibernate.    
. Generation.IDENTITY also not working.  
. So, use the following:  
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="judgements_id_seq")  
   @SequenceGenerator(name="judgements_id_seq", sequenceName="judgements_id_seq", allocationSize=1)  
. Ofcourse, you still have to create this sequence in the db for it to work  

## Spring-boot-devtools automatic restart
. automatic restart will work either if you set build automatically for your project  
. or do a clean and build project and the restart will happen  
. this is much better than doing a full stop-start everytime you make changes to your application  
. this wont work if you dont include the spring-dev-tools in the pom file  

## Returning JSON Response
. As you are using spring boot web, jackson dependency is implicit and we do not have to define explicitly.  
. And as you have annotated with @RestController there is no need to do explicit json conversion. Just return a POJO and jackson serializer will take care of converting to json.  
. It is equivalent to using @ResponseBody when used with @Controller. Rather than placing @ResponseBody on every controller method we place @RestController instead of vanilla @Controller and @ResponseBody by default is applied on all resources in that controller.  
. MappingJackson2HttpMessageConverter will handle the conversion of the POJO to JSON.  

## PostgreSQL Installation
. I have installed postgresql directly from its website.    
. Downloaded a .run file and executed it w/ sudo privelages from the command shell.    
. Check google to find out how to run a .run file.    
. As part of the installation, pgadmin (the UI based tool for postgresql) and psql (command shell) are both installed automatically.    
. By default, postgreSQL will run on port 5432.    
. pgadmin will run on a random port (other than 5432), no need to change the port of pgadmin, let it run on random port.  
. there is some problem with pgadmin, when creating tables/columns, some space/blank characters is getting embedded and hence it is better not to use pgadmin for these purposes.  
. use psql, for ddl and dml ops.  

## Infinite Recursion with Jackson JSON and Hibernate JPA
. When you try to serialize JPA entities having @OneToMany, @ManyToOne, @ManyToMany relationships with Jackson (which is the default json processor with springboot), it ends up with an Infinite Recursion Stackoverflow error.  
. To avoid this, mention @JsonIgnoreProperties on both sides of the relationship in the bean/model and that's it.  
. Basically, this annotation is used to either suppress serialization of properties (during serialization) or to ignore processing of JSON properties read (during deserialization).  
. Here is the link to the solution and the issue on stackoverflow: https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue  

## Selecting specific columns with @Query
. When you are selecting specific columns in a query, whether it is spring data jpa query or spring data jpa native query, you must use an object array as a return type.  
. Instead, if you return the actual object then you will end up with ConvertorNotFoundException because the actual object all the columns and since you are fetching only a few, mapping won't happen, even if you have a matching constructor in the object, such a constructor will never get called at all.  
. So, for e.g., If your query is: @Query(value="select e.id, e.exchangeCode from ExchangeList e") then your return type should be List<Object[]> findAllParents();  

## JSON Views: @JsonView
. If you want only specific fields of your entity to be part of your json response, meaning, you dont even to send a null value against a specific field of your entity in your json response, for such scenarios use @JsonView(viewname).  
. For e.g., Let's say, your entity has the following fields:  
id, code, name and of this you want only code and name to be part of your json response, of course, when you query this from the db you can only fetch code and name but then when form the response the id value will also get serialized as null and will ultimately be part of the response as null and if you want to avoid this then do the following:  
. create a class called Views and create empty static classes(s) inside it. Each static class denotes one view, so create as many as you want.  
. Now, annotate, the fields of the entity which you want to be part of the json response with @JsonView(viewname), here, the view name is the name of the static class.   
. Then, in the @Controller or @RestController, annotate the method where you will be returning the json response with @JsonView(viewname).  
. You can also use @JsonIgnoreProperties but this will make the field to be always ignored/suppressed for serializing and deserializing in all occassions i.e. this field then can't be part of any json response whereas with @JsonView we can make this work on the basis of conditions i.e. show certains fields when a certain view is used and show others in case of a different view, gives a lot of flexibility.
. Ofcourse, you can always mark a certain fields as @JsonIrnoreProperties when you know for sure that these will never be part of any json response.  

## Testing
. @RunWith annot. takes in SpringRunner.class, this is an alias for SpringJunit4ClassRunner.  
. Use @DataJpaTest, when you test the db connection i.e. repository layer.  
. You will use @DataJpaTest for db integration testing, @WebMvcTest for Controller/MVC layer testing and @SpringBootTest in other scenarios such as testing the service layer.  
. assertThat is from AssertJ Assertions class.  
. For testing the service layer, you mock the Repository interface with @MockBean. In fact, you do this for mocking any layer.    
. And to mock the call, use Mockito.when and thenReturn. Mockito is from org.mockito.Mockito package.  
. To mock mvc calls, use MockMvc.perform and andExpect to verify the results.  
. MockMvc is from spring test.  
. While performing the mvc layer test i.e. when you use MockMvc then it is mandatory to annotate the test class with @WebMvcTest(controller class name) otherwise the MockMvc configuration will not happen and will throw an Unsatisfied dependency exception.  
. Matchers is a class from hamcrest which is from org.hamcrest.Matchers package. Used for validation and used in conjuction with andExpect of MockMvc.  
. AssertJ has fluent assertions for java whereas Hamcreast matchers are little bit not really reader-friednly but still will do the job. Especially, MockMvc jsonPath has support Hamcrest, so this is one case where Hamcrest is more suitable than AssertJ's assertThat.  
. And, Mockito is an open source testing framework for Java. It is used for creating mock objects. Keywords: when and thenReturn.  

## Service, Persistence and Controller/MVC Layers
. The Service Layer sits in-between Controller and the DAO i.e. the persistence layer.  
. Servuce layers job is to work on the data that is to be passed to the persistence layer or to be recvied from the persistence layer.  
. Any business rules that needs to be applied on the data before/after passing/fetching to/from the persistence layer can be done by the SL.  
. You can avoid the SL and let the Controller directly connect to the PL but this will lead to more chaos and tight coupling.  
. Ideally, the Controller's job is only to navigate requests coming in from the browser and sending back the response to the correct view and hence the SL acts as a bridge b/w the Controller and the PL.  

## Code Review
. Sonaqube is good but has some hardware and software requirements and might take time to setup, otherwise it is a good choice.  
. PMD, Spotbugs complement each other. Although, most issues are raised by pmd only.  
. Checkstyle will mainly complain about readability, styling and stuff like that.  
. All 3 - PMD, CheckStyle and Spotbugs have to be used together, in case, you are not using sonarqube.  
. As soon as you write code, run all these 3.  
. UR-anamoly of PMD is mostly a false positive. It complains about a variable used in the for-each being used inside the loop.  
. DU-anamoly is generally about value being assigned to variables. This can be solved.
. DD-anamolies should also be fixed.  

## No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor Exception
. This exception is thrown when you are lazy loading an entity but the before the entity is fully loaded the Jackson tries to serialize the field and hence this exception is thrown.  
. Either use @JsonIgnoreProperties on the field for that field to be ignore for serilization or use @JsonViews on other fields so that this field is ignored/suppressed for serialization. 

## Exception Handling
. First of all, this is a very good tutorial about exception handling in spring boot : https://www.toptal.com/java/spring-boot-rest-api-error-handling  
. And, this example of having one class handling all exceptions is a perfect enterprise application approach: https://medium.com/@jovannypcg/understanding-springs-controlleradvice-cd96a364033f  
. The above article also has good unit tests written to tests these exception handlers, which is the right way to do.  
. @ExceptionHandler annotation is part of org.springframework.web.bind.annotation, it's purpose is to handle any uncaught exceptions.  
. ExceptionHandler is a Spring annotation that provides a mechanism to treat exceptions that are thrown during execution of handlers (Controller operations). This annotation, if used on methods of controller classes, will serve as the entry point for handling exceptions thrown within this controller only.  
. Altogether, the most common way is to use @ExceptionHandler on methods of @ControllerAdvice classes so that the exception handling will be applied globally or to a subset of controllers.  
. Handler methods which are annotated with this annotation are allowed to have very flexible signatures.  
. The Return type for methods annotated with this annotation should either have a view, model, modelandview and similar types or a ResponseEntity<?> Object to set response headers and content.
. The ResponseEntity body will be converted and written to the response stream using message converters.  
. ResponseEntity class extends HttpEntity and has overloaded constructors for creating ResponsEntity/HttpEntity along with body, status, headers etc.  
. This annotation has one option param named value, which contains Exceptions handled by the annotated method. If empty, will default to any exceptions listed in the method argument list.  
. @ControllerAdvice is a Specialization of @Component for classes that declare @ExceptionHandler, @InitBinder, or @ModelAttribute methods to be shared across multiple @Controller classes.  
. Classes with @ControllerAdvice can be declared explicitly as Spring beans or auto-detected via classpath scanning.  
. All such beans are sorted via AnnotationAwareOrderComparator, i.e. based on @Order and Ordered, and applied in that order at runtime. For handling exceptions, an @ExceptionHandler will be picked on the first advice with a matching exception handler method.  
. By default the methods in an @ControllerAdvice apply globally to all Controllers. Use selectors annotations(), basePackageClasses(), and basePackages() (or its alias value()) to define a more narrow subset of targeted Controllers. If multiple selectors are declared, OR logic is applied, meaning selected Controllers should match at least one selector.  
. Note that selector checks are performed at runtime and so adding many selectors may negatively impact performance and add complexity.  
. Classes annotated with this annotation will intercept exceptions from controllers/classes across the application.  
. If there are multiple classess annotated with this annotation then they are chosen on the basis of the selectors or if not selectros then they are chosen on the basis of the order and if no order then they are chosen on the basis of the exceptions that are being handled by the @exceptionhandler in the class.  
. The ideal method of handling exceptions in a spring apprlication is as follows:  
a. Create your own exception classes. Despite Spring provides many classes that represent common exceptions in an application, it is better practice to write your own or extending those existing.  
b. One controller advice class per application. It is a good idea to have all exception handlers in a single class instead of annotating multiple ones with @ControllerAdvice.  
c. Write a handleException method. This one is meant to be annotated with @ExceptionHandler and will handle all exceptions declared in it and then will delegate to a specific handler method.  
d. Add one method handler per exception. Imagine you want to handle a UserNotFoundException, then create a handleUserNotFoundException.  
e. Create a method that sends the response to the user. Handler methods are meant to do the logic to treat a given exception, then they will call the method that sends the respond. This method will receive a list of errors as body and the specific HTTP status.  
. Difference b/w @ExceptionHandler, HandlerExceptionResolver and @COntrollerAdvice:  
. @ExceptionHandler works at the Controller level and it is only active for that particular Controller, not globally for the entire application.  
. HandlerExceptionResolver - This will resolve any exception thrown by the application. It is used to resolve standard Spring exceptions to their corresponding HTTP Status Codes. It does not have control over the body of the response, means it does not set anything to the body of the Response.It does map the status code on the response but the body is null.  
. @ControllerAdvice used for global error handling in the Spring MVC application.It also has full control over the body of the response and the status code.  
. If you have multiple classes with @ControllerAdvice then you can control the order in which these are executed by specifying the order using @Order or @Priority.  
. if you have a @ControllerAdvice with an @ExceptionHandler for Exception that gets registered before another @ControllerAdvice class with an @ExceptionHandler for a more specific exception, like IOException, that first one will get called. As mentioned earlier, you can control that registration order by having your @ControllerAdvice annotated class implement Ordered or annotating it with @Order or @Priority and giving it an appropriate value.  
. At context initialization, Spring will generate a ControllerAdviceBean for each @ControllerAdvice annotated class it detects. The ExceptionHandlerExceptionResolver will retrieve these from the context, and sort them using using AnnotationAwareOrderComparator which is an extension of OrderComparator that supports Spring's Ordered interface as well as the @Order and @Priority annotations, with an order value provided by an Ordered instance overriding a statically defined annotation value (if any). It'll then register an ExceptionHandlerMethodResolver for each of these ControllerAdviceBean instances (mapping available @ExceptionHandler methods to the exception types they're meant to handle). These are finally added in the same order to a LinkedHashMap (which preserves iteration order).  
. When an exception occurs, the ExceptionHandlerExceptionResolver will iterate through these ExceptionHandlerMethodResolver and use the first one that can handle the exception.  

## Caching - @Cacheable
. If you do not provide a cache name in the @Cacheable then I get a java error saying: At least one cache should be provided per cache operation. 

## Caching - Unable to Connect to Redis error 
. "Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost:6379" : Got this error because I hadn't installed Redis yet on my local machine, silly mistake. You need to install redis server first.  
. Followed the instructions here to install redis server: https://www.digitalocean.com/community/tutorials/how-to-install-and-secure-redis-on-ubuntu-18-04  

## Caching - Handy Redis Commands
. Some useful commands: 
 . sudo systemctl restart redis.service  - to restart the service
 . sudo systemctl status redis  - to know the status of redis 
 . redis-cli  - to connect to redis server using cli
 . sudo systemctl restart redis  - to restart redis on the whole  
 . sudo netstat -lnp | grep redis - to know which ip the server is bound to and on which port is it listening  

## Caching - Redis Metrics
a. redis-cli info stats  
. the most imp. metric of this output is keyspace_hits and keyspace_misses  
. One tells you about successful lookups and the other about failed lookups  
b. Determining hit_ratio and miss_ratio  
. hit_ratio  = (keyspace_hits)/(keyspace_hits + keyspace_misses)  
. miss_ratio = (keyspace_misses)/(keyspace_hits + keyspace_misses)  
c. Latency - delay b/w request and response  
. redis-cli --latency -h 127.0.0.1 -p 6379  
d. Fragmentation Ration  
. redis-cli info memory  
. in this output the following are imp. : used_memory_human and used_memory_rss_human  
. mem_fragmentation_ratio: used_memory_rss/used_memory  
e. Determining Evicted Keys  
. redis-cli info stats  
. The evicted_keys will give the no.of keys evicted by the redis because of maxmemory limit (if any) being reached.  
f. Check out the keys being used  
. redis-cli scan 0  

## Caching - Could not write JSON - LazyInitializationException Hibernate - Redis 
. "Could not write JSON: failed to lazily initialize a collection of role: com.springboot.postgresql.model.ExchangeList.stockLists, could not initialize proxy - no Session; nested exception is com.fasterxml.jackson.databind.JsonMappingException: failed to lazily initialize a collection of role : 
. You will get this exception when any of the collections in your entity that you are trying to cache is being loaded lazily using FetchType.LAZY.  
. As of know, I haven't found any solution for this and it seems only way to avoid this is making them EAGER loaded.  
. Probably, not a bad idea, coz anyways you are going to cache them so might not be a big issue but still this is an issue.
. So, Hibernate lazyLoading and Spring redis cache cannot go hand-in-hand.  
. And, this may not be anything do with hbernate or redis, this might still happen with any jpa provider and any cache provider.  
. Will explain why this happens in the redis cache flow.  

## Caching - Redis Cache Flow - With LazyLoading Collection
. When the control first goes to a method annotated with @Cacheable, redis then will first search in the cache to find the info., if it finds, this is then returned and the application throws it back to the client.  
. 16-05-2019 15:42:47.608 [http-nio-8080-exec-3] DEBUG io.lettuce.core.RedisChannelHandler.dispatch - dispatching command AsyncCommand [type=GET, output=ValueOutput [output=null, error='null'], commandType=io.lettuce.core.protocol.Command]
. The above log output is an example where the redis is running a GET command to search in the cache.  
. When there is no data in the cache, the repo call happens and entities are loaded naturally by spring (hibernate) except for the entities that are to be lazily loaded. 16-05-2019 15:42:47.661 [http-nio-8080-exec-3] DEBUG o.h.internal.util.EntityPrinter.toString - com.springboot.postgresql.model.ExchangeList{country=USA, lastUpdatedBy=admin, lastUpdatedDate=2019-05-01 16:59:37.914558, createdDate=2019-05-01 16:59:37.914558, createdBy=admin, name=Nasdaq, active=true, id=3, stockLists=<uninitialized>, exchangeCode=NASDAQ}
. Above is the log output where the stockLists collection has a FetchType of LAZY and hence it is not yet fetched from the db and hence it is marked as uninit.  
. Now, Redis will write the above output to the cache.  
. 16-05-2019 16:58:24.186 [http-nio-8080-exec-2] DEBUG o.s.orm.jpa.JpaTransactionManager.doCleanupAfterCompletion - Not closing pre-bound JPA EntityManager after transaction
16-05-2019 16:58:24.190 [http-nio-8080-exec-2] DEBUG io.lettuce.core.RedisChannelHandler.dispatch - dispatching command AsyncCommand [type=SET, output=StatusOutput [output=null, error='null'], commandType=io.lettuce.core.protocol.Command]
. As you can see from the above log output that Redis writes the fetched data to cache (using its SET command) as soon as the control gets transferred from the JPATransactionManager, kind of indicating that the transaction is over and hence data can now be written to cache but the thing is, the LAZYly loaded collections will be fetched only after this and hence they are not part of the cache.
. Now, since the output itself does not have the stockLists data, the cache data will also not have it or have it as uninit. and in the next call from the client when redis again looks for the data in the cache, it will find this data (along with uninit. stockList data) and return it and when spring tries to write this info. it will get an error saying 'Could not write JSON....Lazy Initi....'  

## Caching - Redis Cache Flow - With EagerLoading Collection
. When the control first goes to a method annotated with @Cacheable, redis then will first search in the cache to find the info., if it finds, this is then returned and the application throws it back to the client.  
. When there is no data in the cache, then the repo call happens and all entities are loaded.  
. 16-05-2019 16:26:06.455 [http-nio-8080-exec-2] DEBUG o.h.internal.util.EntityPrinter.toString - com.springboot.postgresql.model.ExchangeList{country=USA, lastUpdatedBy=admin, lastUpdatedDate=2019-05-01 16:59:37.914558, createdDate=2019-05-01 16:59:37.914558, createdBy=admin, name=Nasdaq, active=true, id=3, stockLists=[com.springboot.postgresql.model.StockList#8], exchangeCode=NASDAQ}
. The above is the log output and as you can see even the stockList (collection) is also loaded as it is EAGERly loaded.  
. redis will then write this into its cache using its SET command and in the next call it will again check its cache and since it will find the info the same will be returned, the only diff. b/w this flow and the lazyloading flow is that, the info. in the cache is full info, along with the stockList (collection) info, hence no error will be thrown while spring tries to write the json.  

## Caching - Eviction Strategy of redis
. In a 32 bit machine, redis limits maxmemory to 4 gb.   
. In 64 bit machine there is no limit on how much data that can be cached.  
. But, if you want you can set a maxmemory in redis.conf file and when the limit is reached, redis will perform eviction.  
. There are 2 eviction strategies of redis - lru (least recently used) and lfu (least frequently used).  
. For all version less than 4, redis uses only lru and from 4 onwards lfu is available.
. In LRU, when the limit is reached, the Least Recently Used keys will be evicted first whereas in LFU, the Least Frequently Used keys will be evicted, which is a better strategy.  
. There are also other parameters that needs to be set to decide the policy of eviction when a maxmemory limit is reached, in redis.conf file.  

## Caching - @EnableCaching
~ This annot. triggers a post processor that inspects every spring bean for the presence of caching annots. on public methods.  
~ If such an annot. is found, a proxy is automatically created to intercept method call and handle the caching behaviour accoridngly.  
~ The annots. managed by this post processor are Cacheable, CachePut and CacheEvict.  
~ If you want caching to be disable for your application, just comment this one annot.  

## Caching - Buffer vs Cache
~ A buffer is a temporary memory location which is used to store and transfer huge chunks of data rather than sending smaller chunks whereas caching is about storing frequently accessed data in memory in a particular data structure so that subseuent requests can be serviced directly from the memory rather than doing an IO op.  

## Caching - Cache hit vs Cache miss
~ Cache Hit: occurs when the requested data can be found in the cache.  
~ Cache Miss: occurs when the requested data cannot be found in the cache.  

## Caching - @Cacheable annot.
~ This is demarcate methods that are cacheable.  
~ At the minimum, this annot. requires the name of the cache to be mentioned as it's param. For e.g. @Cacheable("xyz"), where 'xyz' is the name of the cache.  
~ It is possible to declare more than one cache per method. For e.g.., @Cacheable({"books", "isbns"}), in this case, each of the caches is checked before executing the method, if at least one cache is hit, the associated value is returned.  
~ All the other caches that do not contain the value are also updated, even though the cached method was not actually executed.  

## Caching - Key Generation
~ Since caches are essentially key-value pairs, each invocation of a cached method needs to  be translated into a suitable key for cache access.  
~ The KeyGenerator algo is as follows:  
~ If no params are given, it returns a SimpleKey.EMPTY  
~ If one param is given, that instance will be returned  
~ If more than one param is given, return a SimpleKey that contains all params   

## Caching - Custom Key Generation
~ @Cacheable anno. allows you to specify how the key is generated using its key attribute.  
~ This strategy uses SpEL.  
~ Custom Key Generation is the recommended approach over the default approach.  
~ @Cacheable(cacheNames="books", key="#isbn")  
 public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)  
~ @Cacheable(cacheNames="books", key="#isbn.rawNumber")  
 public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)  
~ @Cacheable(cacheNames="books", key="T(someType).hash(#isbn)")  
 public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)  
~ The preceding snippets show how easy it is to select a certain argument, one of its properties, or even an arbitrary (static) method.  
~ You can also mention your KeyGeneration strategy using KeyGenerator attribute.  
~ KeyGenerator and Key attributes are mutually exclusive.  

## Caching - Conditional Caching using condition
~ The condition param takes a SpEL expression and it evaluates to either true or false.  
~ If true, the method is cached. If not, then the method is executed.  
~ @Cacheable(cacheNames="book", condition="#name.length() < 32")   
 public Book findBook(String name)  
~ In the example above, the method is cached only when the name para length is less than 32 otherwise it gets executed.  

## Caching - Conditional Caching using unless
~ Unlike condition, unless expressions are evaluated after the method has been called.  
~ @Cacheable(cacheNames="book", condition="#name.length() < 32", unless="#result.hardback")   
 public Book findBook(String name)  
~ In the example above, the results are cached only if it is not hardback i.e. hardback results are not cached.  
~ Here, #result is the the result of method call. This is available only in unless, cache put and cache evict expressions.  

## Caching - @CachePut
~ When the cache needs to be updated without interfering with the method execution, you can use this annot.  
~ The method annotated with this annot. is always executed and its results are placed in the cache as per its config.  
~ It supports the same options as that of @Cacheable.  
~ @CachePut(cacheNames="book", key="#isbn")  
 public Book updateBook(ISBN isbn, BookDescriptor descriptor)  

## Caching - using @CachePut and @Cacheable on same method
~ This is generally discouraged because both have different behaviours since the latter causes the method execution to be skipped whereas the former forces the method execution to happen and hence this leads to unexpected behaviour.  

## Caching - @CacheEvict
~ Methods demarcated with this annot. will result in the cache being evicted to remove stale data.  
~ This annot. requires at-least one cache name to be mentioned.  
~ The allEntries param of this annot. indicates that a cache-wide eviction needs to be performed rather than an entry eviction (key based) which is slower.  
~ @CacheEvict(cacheNames="books", allEntries=true)  
 public void loadBooks(InputStream batch)  
~ In the example above, rather than evicting each entry, entire cache region will be cleared out, which is much faster.  
~ By default, the eviction occurs after the method executes. You can use beforeInvocation=true to reverse this phenomenon.  
~ A good technique to use @CacheEvict is to use them on void methods, such methods can act as trigger to evict the cache and at the same time won't require any result to be returned which is not the case with @Cacheable which requires a result.  

## Caching - using multiple cache annot. of the same type
~ You can do this using @Caching annot.  
~ @Caching(evict = { @CacheEvict("primary"), @CacheEvict(cacheNames="secondary", key="#p0") })  
 public Book importBooks(String deposit, Date date)  
~ The above example allows multiple cache annot. of the same type on a method. 

## Caching - using the same cache for all ops/methods of a class
~ @CacheConfig is a class-level annot.  
~ @CacheConfig("books")  
 public class BookRepositoryImpl implements BookRepository {  
   @Cacheable  
   public Book findBook(ISBN isbn) {...}  
 }  
~ The above example allows the same cache to be shared by all ops/methods that are cacheable.  
~ However, any cache config that is specified on a method/op will always override the class-level config.   

## Hibernate - Default FetchType for @OneToOne, @OneToMany, @ManyToOne and @ManyToMany relationships
~ The default fetch type for all the above in hibernate is LAZY.  

## Caching - customizing cache manager/resolver
~ Spring boot by default will provide the appropriate cacheManager/Resolver on the basis of the cache used.  
~ You can also customize the same by implementing the CachingConfigurer interface.  
~ The class annot. with @EnableCaching has to implement this interface and override the methods - cacheManager, cacheResolver etc.  

## Caching - custom annotations for caching
~ Spring allows us to define our own custom annot. and we can do the same for caching in order to avoid redundancy.  
~ For instance, rather than using @Cacheable(cacheNames="books", key="#isbn") on multiple methods we can just do the following,  
~ @Retention(RetentionPolicy.RUNTIME)  
 @Target({ElementType.METHOD})  
 @Cacheable(cacheNames="books", key="#isbn")  
 public @interface SlowService {  
 }  
~ Now, you can just replace the annot. on the below method with our custom annot.  
~ @Cacheable(cacheNames="books", key="#isbn")  
 public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)  
~ @SlowService  
 public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)  
~ Even though @SlowService is not a Spring annotation, the container automatically picks up its declaration at runtime and understands its meaning.  

## OAuth2
~ OAuth or OAuth2 stands for Open Authorization.  
~ Its a free and Open protocol.  
~ It allows users to share their private resources with a third-party while keeping their credentials secret. These resources could be photos, videos etc.  
~ OAuth does this by granting the requesting (client) a token, once access is approved by the user. Each token grants limited access to a specific resource for a specific period.  
~ The OAuth 1.0 was a result of a small community based effort, built to support certain use-cases. OAuth 2.0, is a standard and it is built with the experiences of OAuth 1.0 and other use-cases. 2 is backward compatible with 1.  
 
## Accessing restricted resources in traditional client-server model i.e. w/o oauth2
~ The clients requests access to a protected resource on the server by authenticating with the server using the resource owner's creds. For this to happen, the resource owner must share it's creds with the third-party appi i.e. the client.  
~ There are several limitations of this approach:  
 ~ The third-party apps must store the user creds with them which can raise security, trust issues.  
 ~ Whenever the user changes the creds for the protected resource, the same change should also happen at the third-party app - duplication of efforts.  
 ~ Third-party apps will gain overly broad access to the protected resource and might lead to exploitation.  

## OAuth2 Roles
~ There are 4 roles in OAuth2.  
~ Resource Owner : an entity capable of granting access to protected resources which it owns. When a resource owner is a person, it is referred to as end-user.  
~ Resource Server: the server hosting the protected resources and capable of accepting and responding to protected resource requests using access tokens.  
~ Client: an application making protected resurce requests on behalf of the resource owner and with its authorization.  
~ Authorization Server: the server issuing access tokens to client after sucessfully authenticating the resource owner and obtaining authorization.  

## OAuth2 Protocol Flow
~ Following is the protocol flow:
~ the client request authorization from resource owner. The authorization request can be made directly to the resource owner, or preferably indirectly via the authorization server.  
~ the client receives the authorization grant, which is a credential represential resource owner's authorization, expressed using one of four grant types or using an extension grant type.  The grant type dependens upon the methd used by the client to request authorization and the types supported by the auth. server.  
~ the client requests an access token by authenticating with the auth server and presenting the auth grant.  
~ the auth server authenticates the client and validates the auth grant, if valid, issues an access token.  
~ the client requests the protected resource from the resource server and authenticates by presenting the access token.  
~ the resource server validates the access token, if valid, serves the request.  

## OAuth2 Authorization grant types
~ Auth. Code:  
 ~ Here the auth. code is obtained by using an auth. server as an intermediary b/w the client and the resource owner.  
 ~ Instead of requesting auth. directly from the resource owner, the client directs the resource owner to an auth. server which in turn directs the resource owner back to the client w/ the auth. code but before directing the resource owner back to the client with auth. code, the auth. server authenticates the resource owner and obatns auth.  
~ this is generally used with server-side applications.   
~ Implicit: 
 ~ this is a simplified auth. code flow optimized for clients implemented in a browser using a scripting language like javascript.  
 ~ here instead of issuing the auth. code, the client is issued an access token directly.  
 ~ this is generally used with mobile apps or web apps - apps that run on the user's device.  
~ Resource Owner password Creds:
 ~ the resource owner pwd creds (i.e. username and pwd) can be used directly as an auth. grant to obtain an access token.  
 ~ this should only be used when there is a high degree of trust b/w the resource owner and tthe client.  
 ~ but even though the resource owner creds are with client, they are with teh client for a single reqest and are then exchanged for an access token.  
 ~ this grant eliminates the need for the client to store the creds for future use, by exchanging creds with a long-lived access token. 
 ~ this is used with trusted applications, such as those owned by the service itself.  
~ Client creds:
 ~ here the client creds are used as auth. grant typically when the client is acting on its own behalf i.e. the client is also the resource owner.  
 ~ used for machine-to-machine interaction.  
 ~ used with Applications API access.  

## OAuth2 Access Token
~ Access tokens are strings representing an auth. issued by the auth. server in exchange of the auth. grant to the client to enable the client to access protected resources.  
~ These tokens repesent specific scopes and duration of access.  
~ Access tokens can have different formats, strucrures and cryptographic properties depending upon the resource server security requirements.  
 
## OAuth2 Refresh Token
~ Refresh tokens are creds issued to the clients by the auth. server which are used to obtain fresh/new access tokens when the existing ones become invalid/expire.  
~ These tokens are optional and it depends upon the auth. server whether to issue to refresh tokens or not. But if they are issued, they are issued along with the access tokens itself by the auth. server.  
~ But, unlike access tokens, refresh tokens are intended for use only with the auth. server and are never sent to the resource server.  
~ The flow for getting an refresh token and it's use is as follows:
~ the client requests an access token by authenticating with the auth. server and presenting an auth. grant.  
~ the auth. server authenticates the client and validates the auth. grant, and if valid, issues an access token and a refresh token.  
~ the client makes a protected resource request to the resource server by presenting an access token.  
~ the resource server validates the access token, and if valid, serves the request.  
~ If the access token is invalid or expired, the resource server returns an invalid token error.  
~ the client requests a new access token by authenticating with the auth. server and presenting the refresh token.  
~ the auth. server authenticates the client and validates the refresh token, and if valid, issues a new access token (and, optionally, a new refresh token).  

## OAuth2 Client registration
~ Before the client initiates the protocol, it has to register with the auth. server.  
~ This reg. can happen in many ways but typically involves an end-user interaction with an html form and filling out a few details.  

## OAuth2 Client Types
~ 2 client types based on their ability to maintain confidentiality of their client creds:  
~ confidential: clients capable of maintaining the confidentiality of their creds i.e. client implemented on a secure server.  
~ public: web-browser based applications etc.  

## Oauth2 Client Identifier
~ The authorization server issues the registered client a client identifier -- a unique string representing the registration information provided by the client.  

## OAuth2 Client Authentication
~ Confidential clients are typically issued (or establish) a set of client credentials used for authenticating with the authorization server (e.g., password, public/private key pair).  

## OAuth2 Client Password
~ Clients in possession of a client password MAY use the HTTP Basic authentication scheme to authenticate with tthe auth. server.  
~ The client identifier is encoded using the "application/x-www-form-urlencoded" encoding algorithm and this encoded value is used as the username.  
~ the client pwd is encoded using the same algo and used as the pwd.  
~ The authorization server MUST support the HTTP Basic authentication scheme for authenticating clients that were issued a client password.

## OAuth2 Protocol Endpoints
~ There are 2 protocol endpoints:
~ Auth. endpoint:  
 ~ used by the client to obtain authorization from the resource owner.  
 ~ Before this, The authorization server  MUST first verify the identity of the resource owner.  The way in which the authorization server authenticates the resource owner (e.g., username and password login, session cookies) is beyond the scope of this specification.  
 ~ The authorization server MUST support the use of the HTTP "GET" method for the authorization endpoint and MAY support the use of the "POST" method as well.  
~ Token endpoint:
 ~ this is used by the client for obtaining an access token by presenting its auth. grant or refresh token.  
 ~ The token endpoint is used with every authorization grant except for the implicit grant type (since an access token is issued directly).   
 ~ The client MUST use the HTTP "POST" method when making access token requests.  

## OAuth2 Access Token Scope
~ The authorization and token endpoints allow the client to specify the scope of the access request using the "scope" request parameter.  
~ In turn, the authorization server uses the "scope" response parameter to inform the client of the scope of the access token issued.  
~ The value of the scope parameter is expressed as a list of space-delimited, case-sensitive strings.  
~ The strings are defined by the authorization server.  If the value contains multiple space-delimited strings, their order does not matter.  
~ The authorization server MAY fully or partially ignore the scope requested by the client.  

## OAuth2 Issuing an Access Token
~ If the access token request is valid and authorized, the authorization server issues an access token and optional refresh token.  
~ If the request failed client authentication or is invalid, the authorization server returns an error response.  
~ Successful Response:
 ~ access_token: REQUIRED: The access token issued by the authorization server.  
 ~ token_type: REQUIRED: the type of the token issued. value is case insensitive.  
 ~ expires_in: RECOMMENDED: The lifetime in seconds of the access token. If omitted, the authorization server SHOULD provide the expiration time via other means or document the default value.  
 ~ refresh_toekn: OPTIONAL: The refresh token, which can be used to obtain new access tokens using the same authorization grant.  
 ~ scope: OPTIONAL, if identical to the scope requested by the client; otherwise, REQUIRED.  
~ The parameters are included in the entity-body of the HTTP response using the "application/json" media type.  
~ The client MUST ignore unrecognized value names in the response. The sizes of tokens and other values received from the authorization server are left undefined. The client should avoid making assumptions about value sizes.  The authorization server SHOULD document the size of any value it issues.  

## OAuth2 Accessing Protected Resources
~ The client accesses protected resources by presenting the access token to the resource server.  The resource server MUST validate the access token and ensure that it has not expired and that its scope covers the requested resource.  
~ The method in which the client utilizes the access token to authenticate with the resource server depends on the type of access token issued by the authorization server. Typically, it involves using the HTTP "Authorization" request header field [RFC2617] with an authentication scheme defined by the specification of the access token type used.  

## OAuth2 Access Token Types
~ The access token type provides the client with the information required to successfully utilize the access token to make a protected resource request (along with type-specific attributes).  
~ The client MUST NOT use an access token if it does not understand the token type.  
~ For example, the "bearer" token type defined in [RFC6750] is utilized by simply including the access token string in the request whereas the "mac" token type defined in [OAuth-HTTP-MAC] is utilized by issuing a Message Authentication Code (MAC) key together with the access token that is used to sign certain components of the HTTP   requests.  

## OAuth2 Extensibility Options
~ You can define new Access Token Types, New Endpoint Parameters, New Auth. Grant Types, New Auth. Endpoint Response Types and Additional Error Codes.  
~ Not going into these details.  

## OAuth2 Bearer Token
~ A bearer token is a type of a token issued by the auth. server to he client to access protected resources.  
~ Pure definition: A security token with the property that any party in possession of the token (a "bearer") can use the token in any way that any other party in possession of it can.  Using a bearer token does not require a bearer to prove possession of cryptographic key material (proof-of-possession).  

## OAuth2 Transmitting Bearer Token
~ There are 3 methods of sending bearer tokens in the resource request to the resource server.  
~ Clients MUST NOT use more than one method to send token in each request.  
~ 3 methods:  
~ Authorization Request Header Field: the client uses the "Bearer" authentication scheme to transmit the access token in this method.  
~ Form-Encoded Body Parameter: the client adds the access token to the request-body using the "access_token" parameter but there are many pre-conditions to use this method. Not going into its detail.  
~ URI Query Parameter: the client adds the access token to the request URI query component as defined by "Uniform Resource Identifier (URI): Generic Syntax" [RFC3986],   using the "access_token" parameter.  

## OAuth2 Bearer Token Recommendations
~ Safeguard bearer tokens:  Client implementations MUST ensure that bearer tokens are not leaked to unintended parties, as they will be able to use them to gain access to protected resources. This is the primary security consideration when using bearer tokens and underlies all the more specific recommendations that follow.  
~ Always use TLS (https):  Clients MUST always use TLS [RFC5246] (https) or equivalent transport security when making requests with bearer tokens.  Failing to do so exposes the token to numerous attacks that could give attackers unintended access.  
~ Don't store bearer tokens in cookies:  Implementations MUST NOT store bearer tokens within cookies that can be sent in the clear (which is the default transmission mode for cookies). Implementations that do store bearer tokens in cookies MUST take precautions against cross-site request forgery.  
~ Issue short-lived bearer tokens:  Token servers SHOULD issue short-lived (one hour or less) bearer tokens, particularly when issuing tokens to clients that run within a web browser or other environments where information leakage may occur. Using short-lived bearer tokens can reduce the impact of them being leaked.  
~ Issue scoped bearer tokens:  Token servers SHOULD issue bearer tokens that contain an audience restriction, scoping their use to the intended relying party or set of relying parties.  
~ Don't pass bearer tokens in page URLs:  Bearer tokens SHOULD NOT be passed in page URLs (for example, as query string parameters). Instead, bearer tokens SHOULD be passed in HTTP message headers or message bodies for which confidentiality measures are taken. Browsers, web servers, and other software may not adequately secure URLs in the browser history, web server logs, and other data structures.  If bearer tokens are passed in page URLs, attackers might be able to steal them from the history data, logs, or other unsecured locations.  


## JSON Web Token (JWT)
~ JSON Web Token (JWT) is a compact, URL-safe means of representing claims to be transferred between two parties.  
~ a compact claims representation format intended for space constrained environments such as HTTP Authorization headers and URI query parameters.  
~ JWT claims are encoded in a JSOn Web Signature (JWS) or a JSON Web Encryption (JWE) structure, enabling the claims to be digitally signed or MACed or encrypted.  
~ JWTs represent a set of claims as a JSON object that is encoded in a JWS and/or JWE structure.  This JSON object is the JWT Claims Set.  
~ As per Section 4 of RFC 7159 [RFC7159], the JSON object consists of zero or more name/value pairs (or members), where the names are strings and the values are arbitrary JSON values.  These members are the claims represented by the JWT.  
~ The member names within the JWT Claims Set are referred to as Claim Names.  The corresponding values are referred to as Claim Values.  
~ A JWT is represented as a sequence of URL-safe parts separated by period ('.') characters.  Each part contains a base64url-encoded value.  

## JSON JWT Example
~ The following example JOSE Header declares that the encoded object is a JWT, and the JWT is a JWS that is MACed using the HMAC SHA-256 algorithm:
~ {"typ":"JWT",  
     "alg":"HS256"}  
~ This is always base64url encoded.  

## JSOn JWT Claims
~ The JWT Claims Set represents a JSON object whose members are the claims conveyed by the JWT.  
~ The Claim Names within a JWT Claims Set MUST be unique.  
~ There are three classes of JWT Claim Names: Registered Claim Names, Public Claim Names, and Private Claim Names. Not going into its details.  
~ The following is an example of a JWT Claims Set:  
~ {"iss":"joe",  
     "exp":1300819380,  
     "http://example.com/is_root":true}  

## OAuth2 and JWT
~ The OAuth 2.0 Authorization Framework [RFC6749] provides a method for making authenticated HTTP requests to a resource using an access token.  
~ Access tokens are issued to third-party clients by an authorization server (AS) with the (sometimes implicit) approval of the resource owner.  
~ In OAuth, an authorization grant is an abstract term used to describe intermediate credentials that represent the resource owner authorization.  An authorization grant is used by the client to obtain an access token.  
~ Several authorization grant types are defined to support a wide range of client types and user experiences.  
~ OAuth also allows for the definition of new extension grant types to support additional clients or to provide a bridge between OAuth and other trust frameworks.  
~ Finally, OAuth allows the definition of additional authentication mechanisms to be used by clients when interacting with the authorization server.  
~ JWT Bearer Token can be used to request an access token and JWT can also be used as a client authentication mechanism.  

## Using JWTs as Authorization Grants (OAuth2)
~ Here is how you can use a Bearer JWT as an authorization grant to request the client for an access token request.  
~ Description of some of the parameters to be used in the access token request with a jwt bearer token auth. grant.  
~ The value of the "grant_type" is "urn:ietf:params:oauth:grant-type:jwt-bearer".  
~ The value of the "assertion" parameter MUST contain a single JWT.  
~ The following example demonstrates an access token request with a JWT as an authorization grant (with extra line breaks for display purposes only):  
    POST /token.oauth2 HTTP/1.1  
    Host: as.example.com  
    Content-Type: application/x-www-form-urlencoded  
 
    grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer  
    &assertion=eyJhbGciOiJFUzI1NiIsImtpZCI6IjE2In0.  
    eyJpc3Mi[...omitted for brevity...].  
    J9l-ZhwP[...omitted for brevity...]  
~ The following examples illustrate what a conforming JWT and an access token request would look like.  
~ The example shows a JWT issued and signed by the system entity identified as "https://jwt-idp.example.com".  The subject of the JWT is identified by email address as "mike@example.com".  The intended audience of the JWT is "https://jwt-rp.example.net", which is an identifier with which the authorization server identifies itself.   The JWT is sent as part of an access token request to the authorization server's token endpoint at "https://authz.example.net/token.oauth2".  
~ Below is the JSON object that could be encoded to produce the JWT Claims Set for a JWT:  
{"iss":"https://jwt-idp.example.com",  
     "sub":"mailto:mike@example.com",  
     "aud":"https://jwt-rp.example.net",  
     "nbf":1300815780,  
     "exp":1300819380,  
     "http://claims.example.com/member":true}  
~ The following example JSON object, used as the header of a JWT, declares that the JWT is signed with the Elliptic Curve Digital Signature Algorithm (ECDSA) P-256 SHA-256 using a key identified by the "kid" value "16".  
{"alg":"ES256","kid":"16"}  
~ To present the JWT with the claims and header shown in the previous example as part of an access token request, for example, the client might make the following HTTPS request (with extra line breaks for display purposes only):  
    POST /token.oauth2 HTTP/1.1  
    Host: authz.example.net  
    Content-Type: application/x-www-form-urlencoded  
 
    grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer  
    &assertion=eyJhbGciOiJFUzI1NiIsImtpZCI6IjE2In0.  
    eyJpc3Mi[...omitted for brevity...].  
    J9l-ZhwP[...omitted for brevity...]  

## Using JWTs for Client Authentication (OAuth2)
~ Not going into its details.  

## JWT Format (OAuth2)
~ The JWT MUST contain an "iss" (issuer) claim that contains a unique identifier for the entity that issued the JWT.  
~ The JWT MUST contain a "sub" (subject) claim identifying the principal that is the subject of the JWT.  
 ~ For the authorization grant, the subject typically identifies an authorized accessor for which the access token is being requested  
 ~ For client authentication, the subject MUST be the "client_id" of the OAuth client.  
~ The JWT MUST contain an "aud" (audience) claim containing a value that identifies the authorization server as an intended audience. The token endpoint URL of the authorization server MAY be used as a value for an "aud" element to identify the authorization server as an intended audience of the JWT.  
~ The JWT MUST contain an "exp" (expiration time) claim that limits the time window during which the JWT can be used. Note that the authorization server may reject JWTs
with an "exp" claim value that is unreasonably far in the future.  
~ There are other optional claims also, not going into those.  
~ The JWT MUST be digitally signed or have a Message Authentication Code (MAC) applied by the issuer.  

## Random Notes on OAuth2, JWT
~ While a JWT is longer than most access tokens, theyre able to avoid database lookups because the JWT contains a base64 encoded version of the data you need to determine the identity and scope of access.  ~ This is much efficient than looking up an access token in the db to determine who it belongs to and whether it is valid.  
~ Like OAuth access tokens, JWT tokens should be passed in the Authorization header.  
~ The downside of not looking up access tokens with each call is that a JWT cannot be revoked. For that reason, yout require the ability to immediately revoke access.  
~ A few years ago, before the JWT revolution, a <token> was just a string with no intrinsic meaning, e.g. 2pWS6RQmdZpE0TQ93X. That token was then looked-up in a database, which held the claims for that token. The downside of this approach is that DB access (or a cache) is required everytime the token is used.  
~ JWTs encode and verify (via signing) their own claims. This allows folks to issue short-lived JWTs that are stateless (read: self-contained, don't depend on anybody else). They do not need to hit the DB. This reduces DB load and simplifies application architecture.  

## Example Program: Spring Boot Security OAuth2
~ This is from https://www.devglan.com/spring-security/spring-boot-security-oauth2-example  
~ This is about securing REST APIs using Spring Boot Security OAuth2 with default token store.  
~ We will be implementing AuthorizationServer, ResourceServer and some REST API for different crud operations and test these APIs using Postman.  
~ we will be using mysql database to read user credentials instead of in-memory authentication. Also, to ease our ORM solution, we will be using spring-data and BCryptPasswordEncoder for password encoding.  
~ at the end we will make this configuration compatible with spring boot 2.  
~ the authorization server and resource server are implemented using spring boot.   
~ this program starts with spring-boot-starter-parent of version 1.5.8  
~ Project Code Structure:  
 ~ config: AuthorizationServerConfig.java, ResourceServerConfig.java and SecurityConfig.java    
 ~ controller: UserController.java    
 ~ dao: UserDao.java    
 ~ model: User.java  
 ~ service: impl folder, UserService.java  
 ~ impl: UserServiceImpl.java  
 ~ Application.java
 ~ resources folder has application.properties  
~ AuthorizationServerConig Class: This is the auth. server config.   
 ~ this class extends AuthorizationServerConfigurerAdapter which is from org.springframework.security.oauth2.... package 
 ~ this class is responsible for generating tokens specific to a client.  
 ~ this class overrides configure method (which takes ClientDetailsServiceConfigurer as param) of AuthorizationServerConfigurerAdapter class and configures CLIENT_ID, CLIENT_SECRET, GrantTypes, Scopes, Access and Refresh TokenValidity as an in-memory implementation.  
 ~ And also overrides configure method (which takes AuthorizationServerEndpointsConfigurer as param) and sets the tokenStore and authenticationManager.  
 ~ @EnableAuthorizationServer annot is used to enable auth. server.  
~ ResourceServerConfig Class: This is the Resource Server Config.  
 ~ Resource in our context is the REST API which we have exposed for the crud operation. To access these resources, client must be authenticated.  
 ~ @EnableResourceServer annot. enables a resource server.  
 ~ class extends ResourceServerConfigurerAdapter of org.springframework.security.oauth2... package  
 ~ overrides configure method taking ResourceServerSecurityConfigurer as param and sets the resourceId and stateless attributes.  
 ~ overrides configure method taking HttpSecurity as param and sets some values.  
~ SecurityConfig Class: 
 ~ extends WebSecurityConfigurerAdapter of org.springframework.security.config... package and provides usual spring security configuration.  
 ~ we are using bcrypt encoder to encode our passwords.  
 ~ @EnableWebSecurity : Enables spring security web security support.  
 ~ @EnableGlobalMethodSecurity: Support to have method level access control such as @PreAuthorize @PostAuthorize  
 ~ we are using inmemorytokenstore but you are free to use JdbcTokenStore or JwtTokenStore  
 ~ globalUserDetails method - calls UserDetailsService constructor and then calls passwordEncoder method  
 ~ overrides configure method that takes in HttpSecurity param and sets some values.  
 ~ corsFilter method - sets some CorsConfiguration class values.  
~ UserController class:
 ~ UserService is DI.  
 ~ listUser method which is invoked for request /user for requestType GET, calls findAll method of UserService.  
 ~ create method which is invoked for request /user for type POST, calls save method of UserService.  
 ~ delete method which is invoked for request /user/{id} for type DELETE, calls delete method of USerService.  
~ UserServiceImpl Class:  
 ~ Implements UserDetailsService of org.springframework.security.core.userdetails package.  
 ~ also implements UserService which basically defines the abstract methods which will fetch, save and delete info from db.  
 ~ UserDao is an inteface and is annotated with @Repository annot. and has one method i.e. findByUsername  
 ~ UserDao is DI.  
 ~ loadByUsername method calls findByUsername method of userDao and if not user is found, throws UsernameNotFoundException, this is a custom exception.  
 ~ If user is found, returns the User object by calling the constructor of User with username, pwd and authority params.  
 ~ getAuthority method returns a list of SimpleGrantedAuthority with a role ROLE_ADMIN.  
 ~ delete and save methods of UserService interface are overridden but they call userDao's delete and save methods??  
~ Testing the application  
 ~ we will test the app with Postman  
 ~ Run Application.java as a java application  
 ~ To Generate OAuth Token: settings in postman   
   ~ the url to hit is http://localhost:8080/oauth/token - is the url constant, how is this configured??  
   ~ it should be a POST request (can we use GET also??)  
   ~ Authorization Type: Basic Auth, Username: devglan-client, pwd: devglan-secret - what is the significance of these values and where are these configured??  
   ~ in the body provide username: Alex123, password: password, grant_type: password. All this should be under x-www-form-urlencoded since as per OAuth2 specs, Access token request should use application/x-www-form-urlencoded  
   ~ Once you make the request you will get following result.It has access token as well as refresh token.  
 ~ Accessing Resource w/o token:  
   ~ hit http://localhost:8080/users/user  
   ~ you will get a json response with error and error_description  
 ~ Accessing Resource w/ Token:  
   ~ hit http://localhost:8080/users/user?access_token=<token>  
   ~ and you will get the json response with id, username, salaray and age  
 ~ Using refresh Token:  
   ~ Usually, the token expiry time is very less in case of oAuth2 and you can use following API to refresh token once it is expired.  
   ~ POSt request  
   ~ http://localhost:8080/oauth/token  
   ~ Body params under x-www-form-urlencoded - username: Alex123, password: password, grant_type: refresh_token, refresh_token: <token>  
   ~ in response you will get a new access toke, refresh token, token type, expires in and scope.    
~ Spring Boot 2 and OAuth2  
 ~ If we run this application as is in spring boot 2 we will get an unauthorized error in postman  
 ~ in pom.xml change the spring-boot-starter-parent version to 2.0.0 and also add a dependency org.springframework.security.oauth  
 ~ and you need to Bcrypt CLIENT_SECRET also, so make this change in AuthorizationServerConfig.java  

## Example Program: Spring Boot Security OAuth2 JWT Auth
1. this is about about OAUTH2 implementation with spring boot security and JWT token and securing REST APIs
2. we will be creating a sample spring security OAUTH2 application using a custom token store i.e. a JwtTokenStore
3. Using JwtTokenStore as token provider allows us to customize the token generated with TokenEnhancer to add additional claims.
4. A JWT token consists of 3 parts seperated with a dot(.) i.e. Header.payload.signature
5. Header has 2 parts type of token and hashing algorithm used. The JSON structure comprising these two keys are Base64Encoded.  
 {  
 "alg": "HS256",  
 "typ": "JWT"  
 }  
6. Payload contains the claims. Primarily, there are three types of claims: reserved, public, and private claims. Reserved claims are predefined claims such as iss (issuer), exp (expiration time), sub (subject), aud (audience).In private claims, we can create some custom claims such as subject, role, and others.  
{  
 "sub": "Alex123",  
 "scopes": [  
   {  
     "authority": "ROLE_ADMIN"  
   }  
 ],  
 "iss": "http://devglan.com",  
 "iat": 1508607322,  
 "exp": 1508625322  
}  
7. Signature ensures that the token is not changed on the way.For example if you want to use the HMAC SHA256 algorithm, the signature will be created in the following way:
HMACSHA256(
 base64UrlEncode(header) + "." +
 base64UrlEncode(payload),
 secret)
8. Following is a sample JWT token:
 eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBbGV4MTIzIiwic2N.v9A80eU1VDo2Mm9UqN2FyEpyT79IUmhg  
9. **Maven Dependencies**:  
    * spring-boot-starter-security  
    * spring-security-oauth2  
    * spring-security-jwt  - this provides JwtTokenStore support  
10. Authorization Server Config  
    * AuthorizationServerConfig.java extends AuthorizationServerConfigurerAdapter of org.springframework.security.oauth2... package  
    * AuthenticationManager of org.springframework.security.authentication is DI.  
    * Method accessTokenConverter  
        * is configured as a bean and it instantiates JwtAccessTokenConverter of org.springframework.security.oauth2... package  
        * and it calls the setSigningKey of JwtAccessTokenConverter with some random string - **what is this value??**  
    * The tokenStore method  
        * is also configured as a bean and it returns a new instance of JwtTokenStore (which is of org.springframework.security.oauth2.. package), which takes accessTokenConverter method as input  
    * The configure method  
        * is overridden, taking an ClientDetailsServiceConfigurer of org.springframework.security.oauth2... package as input  
        * and it sets the inmemory details of CLIENT_ID, CLIENT_SECRET, GranTypes, Scopes, Access and refresh token validilty  
    * The configure method  
        * which takes AuthorizationServerEndpointsConfigurer of org.springframework.security.oauth2.. package as input is also overridden and tokenStore, authenticationManager and accessTokenConverter are set  
    * ClientDetailsServiceConfigurer can be used to define an in-memory or JDBC implementation of the client details service.In this example, we are using an in-memory implementation.  
    * @EnableAuthorizationServer - enables the auth. server.  
11. Resource Server Config  
    * Resource in our context is the REST API which we have exposed for the crud operation.To access these resources, client must be authenticated  
    * ResourceServerConfig.java extends ResourceServerConfigurerAdapter of org.springframework.security.oauth2.. package.  
    * configure method  
        * which takes ResourceServerSecurityConfigurer of org.springframework.security.oauth2.. package as input is overridden 
        * and it calls resourceId and stateless methods in it.  
    * configure method  
        * which takes HttpSecurity of org.springframework.security.config.. package as input is also overridden  
        * and anonymous, disable, authorizeRequests, antMatchers, access, and, exceptionHandling, accessDeniedHandler methods.  
        * in this we have configured that /users is a protected resource and it requires an ADMIN role for the access.  
    * Since, we have resource-server and auhorization server implementation in the same project, we don't require to redefine our JwtAccessTokenConverter in the resource server config else we need to provide similar JwtAccessTokenConverter implementation in resource server too.  
    * @EnableResourceServer enables resource server.  
12. SecurityConfig.java:  
    * extends WebSecurityConfigurerAdapter of org.springframework.security.config.. package  
    * UserDetailsService  
        * of org.springframework.security.core.userdetails is DI.  
        * **what is the @Resource(name = "userService") on UserDetailsService DI??**  
    * authenticationManagerBean method  
        *  is overridden and  
        *  **is also configured as a bean, but why, what is its purpose??**  
    * Method globalUserDetails  
        *  takes AuthenticationManagerBuilder of org.springframework.security.config.. package as input and  
        *  calls userDetailsService method and passwordEncoder method.  
    * the configure method  
        *  which takes an HttpSecurity of org.springframework.security.config.. package is also overridden and 
        *  **what does it basically do??** 
    * the encoder method
        * returns a new instance of BCryptPasswordEncoder of org.springframework.security.crypto.bcrypt and 
        * is configured as a bean  
    * the corsFilter method
        *  creates an instance UrlBasedCorsConfigurationSource of org.springframework.web.cors and  
        *   of that of CorsConfiguration which is from org.springframework.web.cors and  
        *   sets few params of it and  
        *   then returns an instance of FilterRegistrationBean of org.springframework.boot.web.servlet  
        * **what is the purpose of this?**  
        * this method is also configured as a bean



## Notes
~ https://dzone.com/articles/secure-spring-rest-with-spring-security-and-oauth2  
~ https://auth0.com/docs/protocols/oauth2  
~ https://www.devglan.com/spring-security/spring-boot-security-oauth2-example  
~ https://www.devglan.com/spring-security/spring-boot-oauth2-jwt-example  
~ http://blog.marcosbarbero.com/centralized-authorization-jwt-spring-boot2/  
~ https://www.toptal.com/spring/spring-boot-oauth2-jwt-rest-protection  
 
