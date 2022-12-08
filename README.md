![banner](https://github.com/Hellaweird/IlluminationAPI/blob/master/assets/Project-Illumination.png?raw=true)

## Maven

Repo:
```xml
<repository>
    <id>dk-repo</id>
    <url>https://mvn.daankoster.nl/plugin/repository/everything/</url>
</repository>
```
Dependencies:
```xml
<dependency>
    <groupId>nl.daankoster</groupId>
    <artifactId>IlluminationAPI</artifactId>
    <version>1.0</version>
</dependency>
```

This API needs shading, its not a plugin of its own. 
For maven shading please see: https://maven.apache.org/plugins/maven-shade-plugin/
For gradle shading see: https://imperceptiblethoughts.com/shadow/

## How to use

Create a `new IlluminationAPI(this)` object in `onEnable()` and store it.
Call its `unregister()` method in `onDisable()`.

### Lights

Currently only philips hue is supported, but that will change in the future. 
Sending lights to the player is done with the set Lights command.

The `lights[]` part of the input variables, is an array wich contains numbers.
These numbers are:
[1] Left
[2] Middle
[3] Right

So to use all the lights use [1,2,3]

Use `sendLights()` to send lighting changes.
