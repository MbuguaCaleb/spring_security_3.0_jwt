**Important Docker Commands**

```aidl
docker images

docker ps

docker exec -it 6ed1893fd8ee /bin/bash

```


**Important POSTGRESQL Commands**

```aidl

psql -U postgres


/q ->quit terminal
```

**User Class**

```aidl

When we talk about authentication and authorization we are mostly dealing with users

```

**Important Interfaces**

```aidl

(a)UserDetails(Interface)

It is an interface that contains a bunch of important methods

It is a good practice if our use class implements this interface.

This makes my user class a spring user.....

(It is the interface for Spring Users,)

It is about making sure that the spring user is secure,this we extend the user details
interface to get the spring features

```

**JWT **

```aidl

```