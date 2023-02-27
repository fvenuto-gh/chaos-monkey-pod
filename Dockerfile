FROM openjdk:8
RUN mkdir /ChaosMonkeyPod
RUN mkdir /ChaosMonkeyPod/bin
ADD target/_deploy/chaos-monkey-pod-0.0.1.jar /ChaosMonkeyPod/bin
WORKDIR /ChaosMonkeyPod/bin
ENTRYPOINT ["java", "-jar", "chaos-monkey-pod-0.0.1.jar"]
