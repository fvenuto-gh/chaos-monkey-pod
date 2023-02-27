#!/bin/bash

##################################################################### 
# Start locally docker desktop and minikube BEFORE run this script. #
#####################################################################

# generate fat-jar
mvn clean package

# build chaos-monkey-pod docker image
eval $(minikube docker-env)
docker build -t chaos-monkey-pod .

# deploy chaos-monkey-pod and "dummy workloads" resources: 
kubectl apply -f ./kube

# forward HTTP traffic from local machine to service running in the cluster
kubectl --namespace=demo port-forward $(kubectl get pod --namespace=demo -l app=chaos-monkey-pod -o jsonpath="{.items[0].metadata.name}") 8080
