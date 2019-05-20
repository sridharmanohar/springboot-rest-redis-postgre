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

## Notes
~ https://www.concretepage.com/spring-boot/spring-boot-redis-cache  
~ https://www.journaldev.com/18141/spring-boot-redis-cache  
~ https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/integration.html#cache  
~ https://spring.io/guides/gs/caching/   
~ https://redis.io/documentation  
         
