## Introduction

Cuba Microservice it is appComponent connected as microservice, like a remote app.
It doesn't work without Shared-Session AppComponent (https://github.com/ikuchmin/shared-session)

## How to use it

Add dependency to your CUBA project as microservice instead of appComponent:
```
dependencies {
    appComponent("com.haulmont.cuba:cuba-global:$cubaVersion")
    appComponent('ru.udya.microservice:microservice-global:0.1-SNAPSHOT')

    microservice('ru.example:app-global:0.1-SNAPSHOT')
}
``` 

## Getting started

Apply microservice plugin (https://github.com/ikuchmin/microservice-gradle-plugin).

1. Build microservice Gradle plugin (https://github.com/ikuchmin/microservice-gradle-plugin)
   ```shell script
   ./gradlew clean publishToMavenLocal
   ```
   
2. Add the plugin to your project
   
   Change buildscript section.
   ```groovy
   buildscript {
      // ...  
      dependencies {
           classpath "com.haulmont.gradle:cuba-plugin:$cubaVersion"
           classpath "ru.udya.microservice:microservice-gradle-plugin:1.0-SNAPSHOT"
       }
   }
   ```
   
3. Apply the plugin on the root project level
   ```groovy
   apply(plugin: 'cuba')
   apply(plugin: 'ru.udya.microservice.microservice-plugin')
   ```
   
4. Apply plugin for all modules
   ```groovy
   configure([globalModule, coreModule, portalModule, webModule]) {
       apply(plugin: 'java')
       apply(plugin: 'maven')
       apply(plugin: 'cuba')
       apply(plugin: 'ru.udya.microservice.microservice-plugin')
   ```
   
5. Replace `CubaDeployment` with `MicroserviceCubaDeployment`. It is needed because
   deployment task must collect dependencies from appComponent and microservice
   configurations. Do not forget do it for all deploy tasks in project. Full task
   path `ru.udya.microservice.gradle.MicroserviceCubaDeployment`
   ```groovy
   task deploy(dependsOn: [assemble, cleanConf], type: MicroserviceCubaDeployment) {
       appName = "${modulePrefix}-core"
       appJars(modulePrefix + '-global', modulePrefix + '-core')
   }
   ```

6. If you deploy you project as UberJAR you must use MicroserviceCubaUberJarBuilding
   task from MicroservicePlugin instead of CubaUberJarBuilding. The reason the same as
   in Deployment topic below.
   ```groovy
   task buildSingleUberJar(type: MicroserviceCubaUberJarBuilding) {
       singleJar = true
       logbackConfigurationFile = 'etc/logback.xml'
       appProperties = ['cuba.automaticDatabaseUpdate': true]
       distributionDir = "$baseDistributionUberJarPath/single"
   }
   ```
   
7. Change `web.xml` in modules. It is needed because microservices connect to the
   project by using `app-component.xml` and makes dependencies only on the microservice
   global module. It gives errors finding spring.xml files in runtime. To avoid errors
   add custom contexts and loaders

    **Core module**
    ```xml
    <listener>
        <listener-class>com.haulmont.cuba.core.sys.AppContextLoader</listener-class>
    </listener>
    
    <servlet>
        <servlet-name>remoting</servlet-name>
        <servlet-class>com.haulmont.cuba.core.sys.remoting.RemotingServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    ```
    
    replace on:
    ```xml
    <listener>
        <listener-class>ru.udya.microservice.sys.MicroserviceAppContextLoader</listener-class>
    </listener>
    
    <servlet>
        <servlet-name>remoting</servlet-name>
        <servlet-class>ru.udya.microservice.sys.remoting.MicroserviceRemotingServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    ```
    
    **Web module**
    ```xml
    <listener>
        <listener-class>com.haulmont.cuba.web.sys.WebAppContextLoader</listener-class>
    </listener>
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>com.haulmont.cuba.web.sys.CubaDispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    ```
    
    replace on:
    ```xml
    <listener>
        <listener-class>ru.udya.microservice.web.sys.MicroserviceWebAppContextLoader</listener-class>
    </listener>
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>ru.udya.microservice.web.sys.MicroserviceCubaDispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    ```
    
    **Portal module**
    ```xml
    <listener>
        <listener-class>com.haulmont.cuba.portal.sys.PortalAppContextLoader</listener-class>
    </listener>
    
    <!-- Handles all webportal requests into the application -->
    <servlet>
        <servlet-name>portal</servlet-name>
        <servlet-class>com.haulmont.cuba.portal.sys.PortalDispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    ```
    
    replace on:
    ```xml
    <listener>
        <listener-class>ru.udya.microservice.restapi.sys.MicroservicePortalAppContextLoader</listener-class>
    </listener>
    
    <!-- Handles all webportal requests into the application -->
    <servlet>
        <servlet-name>portal</servlet-name>
        <servlet-class>ru.udya.microservice.portal.sys.MicroservicePortalDispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    ```

## Known issues

1. Tasks with building war's work with errors. Do not test it.  