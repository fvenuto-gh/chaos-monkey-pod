#!/bin/bash

##################################################################### 
# Start locally docker desktop and minikube BEFORE run this script. #
#####################################################################

# clean target
mvn clean

# clean Kubernetes application resources
kubectl delete -f kube

echo "Clean done."