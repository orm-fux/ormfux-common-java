# About

This is a small library providing some IOC capabilities (i.e. injection). Use this when you have a small 
application, want IOC comfort, but don't use more extensive dependencies like Spring.

# Usage

This library provides a set of annotations, with which you can annoate classes and their properties. 

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

### ```@Bean``` Annotation

To make a class a bean with injected properties add the ```@Bean``` annotation to the class. That's it!

### Manual Registration

You can make unannotated classes bean types by manually adding them as a simple descriptor to the 
```InjectionContext```. Simply to this call:
```java
InjectionContext.addBeanDescriptor(YourBean.class, true|false)
```

Make sure to register beans this way only during application startup! That way you can be sure that
an instance of these beans will be available during injection into other beans.

This method of bean registration is suppposed to be used, when you don't have control over the source
code of the class you want to use as a bean.

## Defining Injectable Properties

To make a property of a bean an injected value simply add the ```@Inject``` annotation to the property.
_Note: The types of the annotated properties must also be beans that can be instantiated by the 
InjectionContext!_

# Example Application:

```java

@Bean
public class Application {
    
    @Inject
    private OtherBean injectedBean;
    
    public static main(String... args) {
        Application application = InjectionContext.getBean(Application.class);
        
        application.injectedBean.sayHello();
    }
}

@Bean
public class OtherBean {
    public void sayHello() {
        System.out.println("Hello from an injected bean!");
    }
}
