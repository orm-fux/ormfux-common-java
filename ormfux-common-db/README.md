# About

This is a small library for ORM and native query access to relational databases. The library is 
supposed to be used in small, single-user applications, where full-fledged ORM access with the 
means of Hibernate or EclipseLink would be overkill.

# Usage

_Please also have a look at the unit tests. They cover a great deal of what is possible!_

The heart of the library is the ```QueryManager```. The ```QueryManager``` provides query objects, 
which encapsulate the database invocations. Simply create a new instance and provide the database
connection to the ```QueryManager``` and you are good to go (well, for native queries at least)!

## Defining the Database Connection

Database connections are provided via ```DbConnectionProvider```s. Simply provide the type, database 
URL, and URL parameters to the ```QueryManager```. The ```QueryManager``` will pass the connection
information along to the queries created by the manager.

For example:
```java
QueryManager queryManager = new QueryManager();
queryManager.setDatabase(H2DbConnectionProvider.class, 
                         "jdbc:h2:mem:myInMemoryDb", 
                         "DB_CLOSE_DELAY=-1", 
                         "MODE=MYSQL", 
                         "DATABASE_TO_UPPER=false", 
                         "AUTOCOMMIT=false");
```

For custom connection provider implementations please keep in mind that the provider should only produce
connections that are for one time use! I.e. one connection for one query execution. 

## Creating and Executing Queries

Query creation  is pretty simple. The ```QueryManager``` provides method, which return query instances, 
which then can be simply executed. There are two kinds of queries that can be created:
1. Native SQL queries (```Query```): Simply provide a the native SQL String.
2. ORM queries (```TypedQuery```): Provide an annotated entity class and, optionally, native SQL constraints.

Regardless of the type of query they support named parameters. In the query Strings mark a parameter by 
using ```:``` in front of the parameter name.

### ```Query``` Execution

The native SQL query provides the following methods for query execution:
1. ```getResultList()```: Executes a "select" query and returns the result as an iterable ```QueryResult```.
2. ```getSingleResult()```: Executes a "select" query and returns the single row of the result as 
   a ```QueryResultRow```.
3. ```executeUpdate()```: Executes a query, which alters data ("insert", "update", "delete").

### ```TypedQuery```

ORM queries provide the following methods:
1. ```getResultList()```: Executes a "select" query and returns the result as a List of entities.
2. ```getSingleResult()```: Executes a "select" query and returns the single row of the result as an entity.
3. ```load(Object id)```: Loads and returns the entity with the given id.
4. ```update(T entity)```: Writes the state of the entity to the database. This either updates an existing
   entity or initially persists a new one.
5. ```delete(T entity)```: Removes the entity from the database.

### Regarding the Database Connection

Each query execution retrieves a database connection from 
the configured connection provider in the ```QueryManager```. _After the query is executed the connection is 
committed  and closed. This is due to the intention that the library is only supposed to be used for small 
applications and we don't want any connections flying around._

## Entity Declarations

For ORM mappings there are a few annotations, which you have to add to your entity classes. They mostly speak
for themselves and are outlined in the following example. Note that each entity _needs_ to have an id and a
version (identified based on annotations)!

```java
public abstract class AbstractNamedEntity {
    @Column(columnName = "id", columnLabel = "id")
    @Id(RandomIdGenerator.class)
    private String id;
    
    @Column(columnName = "version", columnLabel = "version")
    @Version
    private long version;
    
    @Column(columnName = "name", columnLabel = "name")
    @Id(RandomIdGenerator.class)
    private String name;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }
    
    public void setVersion(long version) {
        this.version = version;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

}

@Entity(table = "student")
public class Student extends AbstractNamedEntity {

    @CollectionOfEntities(joinTable = "student_course", joinColumn = "student_id", inverseJoinColumn = "course_id")
    private List<Course> courses = new ArrayList<>();
    
    public List<Course> getCourses() {
        return courses;
    }
    
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}

@Entity(table = "course")
public class Course extends AbstractNamedEntity {
}
```
