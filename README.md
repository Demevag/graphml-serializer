# graphml-serializer
Serialize objects marked with annotations to a graphML format
## Getting Started
All you need is just add annotations to your class. Let's see an example.
Our classes:
```java
@Graph(id ="testGraph")
public class TestingGraph
{
    private Map<String, TestingNode> nodes;
    private List<TestingEdge> edges;
}
```
```java
@Node
public class TestingNode
{
    @Id
    private String nodeId;

    private String stringData;
    private int intData;

    @ComplexData
    private TestEnum testEnum;
}
```

```java
@Edge
public class TestingEdge
{
    @Id
    private String edgeId;

    @EdgeSource
    private TestingNode sourceNode;

    @EdgeTarget
    private TestingNode targetNode;

    @ComplexData
    private TestEnum testEnum;
}
```
```java
public enum TestEnum
{
    ONE,
    TWO,
    HAHA
}
```
Than create `GmlSerializer` object and serializer your file:
```java
GmlSerializer serializer = new GmlSerializer();
serializer.serialize(testingGraph, outputStream);
```
Result will be like that:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<graphml xmlns="http://graphml.graphdrawing.org/xmlns" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns&#10; http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
    <key attr.name="c:TestEnum" attr.type="string" for="edge" id="c:TestEnum_key"/>
    <key attr.name="stringData" attr.type="string" for="node" id="stringData_key"/>
    <key attr.name="intData" attr.type="int" for="node" id="intData_key"/>
    <key attr.name="c:TestEnum" attr.type="string" for="node" id="c:TestEnum_key"/>
    <graph edgedefault="undirected" id="testGraph">
        <node id="TestingNode_second-node">
            <data key="stringData_key">I'm a string data of node2</data>
            <data key="intData_key">2281</data>
            <data key="c:TestEnum_key">TWO</data>
        </node>
        <node id="TestingNode_first-node">
            <data key="stringData_key">I'm a string data of node1</data>
            <data key="intData_key">1337</data>
            <data key="c:TestEnum_key">ONE</data>
        </node>
        <edge id="TestingEdge_First edge" source="TestingNode_first-node" target="TestingNode_second-node">
            <data key="c:TestEnum_key">HAHA</data>
        </edge>
    </graph>
</graphml>
```

### Installing

Add repository into repositories tag:
```xml
<repository>
  <id>graphml-serializer-mvn-repo</id>
  <url>https://raw.github.com/Demevag/graphml-serializer/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
</repository>

```
Than add dependency:

```xml
<dependency>
  <groupId>com.demevag</groupId>
  <artifactId>graphml-serializer</artifactId>
  <version>1.03-SNAPSHOT</version>
</dependency>

```

## Built With
* [lombok](https://projectlombok.org/)
* [junit](https://junit.org/junit5/) - For unit tests
* [xembly](https://www.xembly.org/) - For building xml document
* [jdom2](http://www.jdom.org/) -For parsing xml document
* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **Viktor Khrulev** - [Demevag](https://github.com/Demevag)

## License

This project is licensed under the Apache License - see the [LICENSE.md](LICENSE.md) file for details
