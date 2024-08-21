## Spark application in Kubernetes

Using a simple scala application to deploy different ways to k8s cluster. 
It can be used to validate configs or debug setup or just playing around
with the k8s cluster.

### Compile
Hello World ! 

```
cd scala

sbt clean package
```

### Building Docker image
We are buidling Hello World !

First step is to build the Docker container. 
Docker image also specifies the spark  , the serviceaccount used for k8s execution

```
docker build -t hello-world:1.0 .
```

Load it in minikube or push to your registry

```
minikube start 
minikube image load  hello-world:1.0
```

Proxy is required for minikube to submit the job 
```
kubectl proxy --port=8080
```

### Option 1 - using Spark submit 

Using Spark submit you can deploy the application to Kubernetes
jar file is the location inside the container

```
spark-submit \
  --master k8s://http://localhost:8080 \
  --deploy-mode cluster \
  --name hello-world-scala \
  --class HelloWorld \
  --conf spark.kubernetes.driverEnv.SPARK_SSL_NO_VERIFY_HOSTNAME=true \
  --conf spark.executor.instances=1 \
  --conf spark.kubernetes.container.image=hello-world-scala:1.0 \
  --conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
  --conf spark.kubernetes.namespace=spark \
  local:///app/helloworld.jar
  ```

### Option 2 - using Spark Operator
Installing spark operator

```
helm repo add spark-operator https://googlecloudplatform.github.io/spark-on-k8s-operator

helm install spark-operator spark-operator/spark-operator --namespace spark-operator --create-namespace --set  sparkJobNamespace=spark-everywhere

```

Create namespace for demo application and service account

```
kubectl create ns spark
kubectl create serviceaccount spark -n spark
kubectl create clusterrolebinding spark-role \
  --clusterrole edit \
  --serviceaccount spark:spark \
  -n spark
```
alternatively you can use the yaml files

```
kubectl apply -f spark-role-binding.yaml
kubectl apply -f spark-role.yaml
```

Now lets deploy
```
cd kubernetes

kubectl apply -f spark-application-scala.yaml
```

### Debugging

If there are any issues , these steps wil be helpful 


Unable to create pod  
```
kubectl auth can-i create pods --as=system:serviceaccount:spark:spark -n spark

kubectl port-forward kube-apiserver-minikube 8443:8443 -n kube-system

```

### Validate 

Check if application is deployed and has completed successfully 
```
kubectl get sparkapplications

kubectl get sparkapplications --all-namespaces 
```

### Clean up 
``` 
kubectl delete sparkapplication hello-world-scala -n spark
```