## City Router Project

Here is solution for the task of a traveling salesman

There are approaches:
- Greedy algorithm;
- Silly algorithm;

Optimizer works on Johnson-Trotter algorithm
https://en.wikipedia.org/wiki/Steinhaus%E2%80%93Johnson%E2%80%93Trotter_algorithm


### Implementation of Johnson-Trotter algorithm in a non-recursive form.
#### Explanation of idea
Associate each element of the permutation p[i] with direction pointer d[i].

We will indicate the direction using the arrows ← ("left") or → ("right"). 

We call an element movable if there is an element smaller than it in the direction of the arrow. 

For example, for p={1,3,2,4,5} and  d={←,→,←,→,←}, moving elements are 3 and 5.

At each iteration of the algorithm, we will look for the largest movable element and swap with the element that is in the direction of the arrow. 

After that, we change the direction of the arrows to the opposite for all elements larger than the current one.
Initially p={1,…,n}, d={←,…,←}.

#### Example for n = 3
> p={1,2,3}; d={←,←,←}
>
> p={1,3,2}; d={←,←,←}
> 
> p={3,1,2}; d={←,←,←}
> 
> p={3,2,1}; d={→,←,←}
> 
> p={2,3,1}; d={←,→,←}
> 
> p={2,1,3}; d={←,←,→}
>

### Concurrency realisation
Concurrency is not realised here yet

## Some results
These results are obtained on 
> DELL Inspiron-15-5578, Intel i3-7100U 2.4 GHz, 2 cores, 4 threads;
> 4GB+8GB RAM (DDR4, 2400 MHz), 
> Win10-home 64bit
> 
No concurrency used

- For 120 points in rectangle of N51,E24 -> N46,E24 -> N46,E05 -> N51,E05
with start in point N50,E24 (near Lviv, Ukraine)
Legend: name of point starts with symbol ':', between dashes is distance in kilometres.
> 120 points, points list: EU_cross.csv
routeReport: (13_892.48) 13892.49 km. [:Lviv-50-24, -133- :Ustyluh-51-24, -84- :51x23, -84- :51x22, -84- :51x21, -133- :50x21, -86- :50x22, -133- :49x22, -88- :49x21, -88- :49x20, -88- :49x19, -133- :50x19, -86- :50x20, -133- :51x20, -168- :51x18, -84- :51x19, -158- :50x18, -86- :50x17, -133- :51x17, -84- :51x16, -133- :50x16, -133- :49x16, -88- :49x17, -88- :49x18, -160- :48x17, -89- :48x16, -133- :47x16, -91- :47x15, -91- :47x14, -133- :48x14, -89- :48x15, -133- :49x15, -133- :50x15, -86- :50x14, -86- :50x13, -133- :51x13, -84- :51x14, -84- :51x15, -280- :49x14, -88- :49x13, -88- :49x12, -133- :50x12, -86- :50x11, -133- :51x11, -84- :51x12, -168- :51x10, -84- :51x9, -133- :50x9, -86- :50x10, -133- :49x10, -88- :49x11, -133- :48x11, -89- :48x10, -89- :48x9, -133- :49x9, -159- :50x8, -86- :50x7, -158- :51x8, -168- :51x6, :51x6, -84- :Brussels-51-05, -133- :50x5, -86- :50x6, -159- :49x7, -88- :49x8, -133- :48x8, -133- :47x8, -91- :47x7, -133- :48x7, -89- :48x6, -133- :49x6, -88- :49x5, -133- :48x5, -161- :47x6, -91- :47x5, -133- :Lyon-46-05, -93- :46x6, -93- :46x7, -93- :46x8, -93- :46x9, -133- :47x9, -91- :47x10, -91- :47x11, -91- :47x12, -91- :47x13, -133- :48x13, -89- :48x12, -323- :46x10, -93- :Trento-46-11, -93- :46x12, -93- :46x13, -93- :46x14, -93- :46x15, -93- :46x16, -93- :46x17, -93- :46x18, -162- :47x17, -91- :47x18, -91- :47x19, -161- :48x18, -89- :48x19, -89- :48x20, -89- :48x21, -89- :48x22, -89- :48x23, -89- :48x24, -133- :49x24, -159- :50x23, -133- :49x23, -267- :47x23, -91- :47x22, -91- :47x21, -91- :47x20, -133- :46x20, -93- :46x19, -185- :46x21, -93- :46x22, -93- :46x23, -93- :Sibiu-46-24, -133- :47x24, -400- :Lviv-50-24] 
>
> GreedyWay1; iterations: 20_000; optimized by sector of 9, repeated 7 times, lines on processing 120, reducing 0.49; made at Sun Feb 19 15:56:26 EET 2023
>
> elapsed 51 min 57.0 sec
>

# *

## H2 db
H2 database is planned to use for soring points and sets of points

### Configure H2 database with Spring boot
https://howtodoinjava.com/spring-boot2/h2-database-example
https://www.tutorialspoint.com/h2_database/index.htm

### To utilize H2-database writing data into file use the next property in application.properties:
spring.datasource.url = jdbc:h2:file:C:/dev/H2/data/m11
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

### ... or using application.yaml
spring:
    datasource:
        url: jdbc:h2:file:C:/dev/H2/data/tacoclouddb
        driverClassName: org.h2.Driver
        username: sa
        password:
        generate-unique-name: false
        name: tacoclouddb
    jpa:
        database-platform: org.hibernate.dialect.H2Dialect

### Moreover
AND add dependency into build.gradle: 
> testImplementation 'com.h2database:h2:2.1.214'
### To utilize H2-database temporary writing data into memory:
> spring.datasource.url=jdbc:h2:mem:m11 ... and so on...
