# About

This is a small library providing some dependency injection capabilities . Use this when you have a small 
application, want DI comfort, but don't want the large pool of features like they are provided by Spring.

# Usage

This library provides a set of annotations, with which you can annotate classes and their properties. 

## Retrieving bean instances

To manually retrieve a bean instance simply call:

```java
YourBean bean = InjectionContext.getBean(YourBean.class);
```

This call looks up the bean cache in the context. When there is already an instance of the bean, then 
this instance is returned. Otherwise a new instance is created and values are injected into the new 
instance. When the injected values are not in the bean context a new bean instance is created for them
automatically as well.

The type of the bean must have been explicitly declared as a bean type!

## Marking Classes as Beans

There are two options, which can make a class a bean:

### `@Bean` Annotation

To make a class a bean with injected values add the `@Bean` annotation to the class and the `@BeanConstructor` annotation one of its constructors. That's it!

### Manual Registration

You can make non-annotated classes bean types by manually adding them as a simple descriptor to the 
`InjectionContext`:

```java
InjectionContext.addBeanDescriptor(YourBean.class, true|false)
```

Make sure to register beans this way only during application startup! That way you can be sure that an instance of these beans will be available during injection into other beans.

This method of bean registration is supposed to be used, when you don't have control over the source code of the class you want to use as a bean.

## Defining Injected Values

This library injects values via the constructor with the `@BeanConstructor` annotation. For each parameter a value is injected. These values can be other beans (either annotated or manually registered) and values from configuration files that have been added to the `@InjectionContext`. 

For configuration values a number of simple types (String, number, Class, enum) are supported. The values from the configuration files are converted automatically to the type of the respective parameter. To declare that a parameter gets its value from a configuration file use the `@ConfigValue` annotation. 

To add a configuration file to the context simply call one of the two methods `InjectionContext.addConfigValueSet(..)` or `InjectionContext.addExternalConfigValueSet(..)`. Supported files are plain old `*.properties` files (including their XML format).

# Example Application:

```java
@Bean
public class Application {
    
    public static main(String... args) {
        InjectionContext.addExternalConfigValueSet("myConfig", "/fully/path/to/config.properties");
        
        Application application = InjectionContext.getBean(Application.class);
        
        application.injectedBean.sayHello();
    }
    
    private OtherBean injectedBean;
    
    private int version;
    
    @BeanConstructor
    public Application(OtherBean otherBean,
                       @ConfigValue("config.version") int version) {
       this.otherBean = otherBean;
       this.version = version;
    }
}

@Bean
public class OtherBean {
    public void sayHello() {
        System.out.println("Hello from an injected bean!");
    }
}
