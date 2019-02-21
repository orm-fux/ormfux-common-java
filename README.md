# ormfux-common-java

This is a collection of utility Java libraries. 

## ormfux-common-utils

A collection of utilities. There are, for example, "every day" utilities for operating on 
collections, working with nullable values, or date values. Some of the more rarely used 
ones are the reflection utilities for class and property analysis and invocations.

## ormfux-common-di

A very simple injection framework. Use this, if you have a simple application and other
frameworks, like Spring, would be overkill.

## ormfux-common-db

A simple mini ORM for relational database (SQL) access. Again, this is supposed to be used for 
simple, for which the popular contenders, like Hibernate or EclipseLink, would be overkill.

Supports ```H2``` as database out of the box. More support can be added by simply implementing 
custom ```DbConnectionProviders```.

This one uses ```ormfux-common-utils``` and has an _optional_ dependency on ```ormfux-common-di```.
