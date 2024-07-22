# Cloud Strategy Demo

<p>
Demonstrates how to create instance of services based on the application execution environment.

As an example, we simulate the traditional three-environment model:
</p>

* dev
* uat
* product

<p>
This scenario is indicated when for some reason the behavior of the application must be different depending on the environment in which it is running.
</p>

## Run this demo in the development environment

### Requirements for playing in a local environment
<p>
To reproduce this project in a local environment, the following technologies must be present:
</p>

* JDK 17+
* Apache Maven 3.9+
* _curl_ command line tool

<strong>ℹ️ Info:</strong> **PROJECT_ROOT** is an environment variable that represents the project root, the location where the git project was cloned.

### Starting the Quarkus version
```shell
cd $PROJECT_ROOT/quarkus-cloud-strategy-example
./mvnw clean quarkus:dev -Ddebug=false
```

### Starting the spring boot version
```shell
cd $PROJECT_ROOT/spring-cloud-strategy-example
./mvnw clean -Dspring-boot.run.profiles=dev spring-boot:run
```

<strong>⚠️ Warning:</strong> Pay attention to the ports used in each profile to avoid port conflicts in the local environment

### Testing using curl

#### Testing quarkus version
```shell
curl http://localhost:8081/api
```
Since we started with the dev profile, the following output is expected

> Executing demo method for DEV demo service [com.redhat.quarkus.service.DevDemoService]

#### Testing spring boot version
```shell
curl http://localhost:8082/api
```
Since we started with the dev profile, the following output is expected

> Executing demo method for DEV demo service [com.redhat.spring.service.DevDemoService]

It is possible to test other profiles and validate the execution result

#### Testing the uat profile with quarkus
```shell
./mvnw clean quarkus:dev -Dquarkus.profile=uat -Ddebug=false
curl http://localhost:8080/api
```

Since we started with the uat profile, the following output is expected

> Executing demo method for UAT demo service [com.redhat.quarkus.service.UatDemoService]

#### Testing the uat profile with spring boot
```shell
./mvnw clean -Dspring-boot.run.profiles=uat spring-boot:run
curl http://localhost:8080/api
```

Since we started with the uat profile, the following output is expected

> Executing demo method for UAT demo service [com.redhat.spring.service.UatDemoService]

<strong>⚠️ Warning:</strong> Pay attention to the ports used in each profile to avoid port conflicts in the local environment

## Testing on Openshift

### Requirements for playing in Openshift
<p>
To reproduce this project in a Openshift cluster, the following technologies must be present:
</p>

* Openshift Instance
* [Openshift Client command line tool](https://docs.openshift.com/container-platform/4.16/cli_reference/openshift_cli/getting-started-cli.html)

To test the deployment on Openshift, you must have a working instance of the [Openshift Container Platform](https://www.redhat.com/en/technologies/cloud-computing/openshift/container-platform) available.

If you don't have an instance, there are some options:

* [Developer Sandbox](https://developers.redhat.com/developer-sandbox)
* [Openshift Local](https://developers.redhat.com/products/openshift-local/overview)
* [Podman Desktop Openshift Local](https://podman-desktop.io/docs/openshift/openshift-local)

### Preparing the environment

With a working openshift instance, log in using the openshift client and create two projects to simulate different execution environments.

```shell
oc login --token=sha256~XXXXXX --server=https://api.XXXXXX:6443
oc new-project uat
oc create configmap app-config --from-literal=profile=uat
oc new-project prod
oc create configmap app-config --from-literal=profile=prod
oc project uat
```

<strong>⚠️ Warning:</strong> Adjust login information to suit your environment

<strong>ℹ️ Info:</strong> We also create a configmap per project to tell which profile should be configured in the application.

### Deploy Quarkus version on Openshift

Let's make use of some features that Quarkus offers. With the command below we will create a [native version](https://quarkus.io/guides/building-native-image) of our application and publish it directly to openshift with the uat profile in the uat project.

<strong>⚠️ Warning:</strong> Adjust login information to suit your environment

```shell
./mvnw clean package -DskipTests -Dnative -Dopenshift
```

Consult the route created by openshift using the command below:

```shell
oc get route quarkus-cloud-strategy-example --template='{{ .spec.host }}'
```
The result will be something like: `quarkus-cloud-strategy-example-uat.<OPENSHIFT_DOMAIN>`

Test exactly as you did locally, this time, pointing to the openshift route URL.

```shell
curl http://quarkus-cloud-strategy-example-uat.<OPENSHIFT_DOMAIN>/api
```

<strong>⚠️ Warning:</strong> Adjust the URL information to suit your environment

Let's deploy the same image, but this time, in the *prod* project.

When we asked openshift to deploy our application, an image was automatically generated and included in the internal registry. We can list the image and its version with the command below:

```shell
oc get is -o custom-columns=NAME:.metadata.name,VERSION:.status.tags[0].tag
```

<strong>⚠️ Warning:</strong> Depending on the shell you are using, you may need to use the escape character, for example: `oc get is -o custom-columns=NAME:.metadata.name,VERSION:.status.tags\[0\].tag` 

The result should be something similar to the one below:

> NAME                             VERSION
> quarkus-cloud-strategy-example   1.0-SNAPSHOT
> ubi-quarkus-native-binary-s2i    1.0

Let's create a new application using image `quarkus-cloud-strategy-example` in the *prod* project

```shell
oc new-app -n prod --name=quarkus-cloud-strategy-example uat/quarkus-cloud-strategy-example:1.0-SNAPSHOT
oc set env deployment/quarkus-cloud-strategy-example --from=configmap/app-config --prefix=APP_ -n prod
oc expose svc/quarkus-cloud-strategy-example -n prod
oc get route quarkus-cloud-strategy-example --template='{{ .spec.host }}' -n prod
```

Test by replacing the URL for the prod environment

```shell
curl http://quarkus-cloud-strategy-example-prod.<OPENSHIFT_DOMAIN>/api
```

### Deploy Spring Boot version on Openshift

We will use the [jkube](https://eclipse.dev/jkube/) plugin to deploy to Openshift.

```shell
./mvnw clean package oc:build oc:resource oc:apply -Popenshift -DskipTests
```

Consult the route created by openshift using the command below:

```shell
oc get route spring-cloud-strategy-example --template='{{ .spec.host }}'
```

<strong>ℹ️ Info:</strong> You can conduct the same tests performed for the quarkus version, just adjust the URL for the spring boot version.

```shell
curl http://spring-cloud-strategy-example-uat.<OPENSHIFT_DOMAIN>/api
```

Let's create a new application using image `spring-cloud-strategy-example` in the *prod* project

```shell
oc new-app -n prod --name=spring-cloud-strategy-example uat/spring-cloud-strategy-example:latest
oc delete -n prod svc/spring-cloud-strategy-example
oc expose -n prod deployment/spring-cloud-strategy-example --port=8080
oc patch -n prod deployment spring-cloud-strategy-example --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/env/-", "value": {"name": "SPRING_PROFILES_ACTIVE", "valueFrom": {"configMapKeyRef": {"name": "app-config", "key": "profile"}}}}]'
oc expose svc/spring-cloud-strategy-example -n prod
oc get route spring-cloud-strategy-example --template='{{ .spec.host }}' -n prod
```

## Resources consumptions

<strong>ℹ️ Info:</strong> Finally, observe the consumption of the native Quarkus application compared to the Spring Boot version.
```shell
oc adm top pod -n prod
```
> NAME                                              CPU(cores)   MEMORY(bytes)
> quarkus-cloud-strategy-example-7dc755b8cb-b9zpw   0m           15Mi
> spring-cloud-strategy-example-78c9c45f8c-cmg2f    1m           224Mi


## References
* https://quarkus.io/guides/
* https://quarkus.io/guides/cdi-reference
* https://quarkus.io/guides/deploying-to-openshift
* https://quarkus.io/guides/building-native-image
* https://quarkus.io/guides/config-reference
* https://eclipse.dev/jkube/
* https://docs.spring.io/spring-framework/reference/core/beans/environment.html
* https://www.springcloud.io/post/2022-04/spring-selective-injection
* https://spring.io/guides/gs/testing-web
