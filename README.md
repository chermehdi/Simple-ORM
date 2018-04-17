# SIMPLE-ORM

### Description

* a project developed in java, to allow object relational mapping without the complexity
of transactions and advanced configurations, it's build for educational reasons . to make
the work of `ORM` easier to understand, especially beginners .


### Installation

* Via maven, just clone and make the project via `mvn clean install` and add it to your
project dependencies

### Usage

* the main APIs of the app are `DataSource` and `PersistenceProvider` .
* you connect to your database by supplying informations to the `PersistenceProvider`
via the `DataSource` class, you create the DataSource like :

```java 
DataSource source = new DataSourceBuilder("com.mysql.jdbc.Driver", "jdbc:mysql:")
        .source("Library")
        .host("localhost")
        .username("root")
        .password("root")
        .port(3306)
        .build();
``` 

and you just inject it to the `PersistenceProvider` like 

```java
PersistenceHandler provider = new DefaultPersistenceHandler(source);
```

now u just use the provider to query your database .

```java

Student student = provider.find(1, Student.class);
Teacher teacher = new Teacher(1, "John Doe");
teacher.addStudent(student);
provider.save(teacher);
```