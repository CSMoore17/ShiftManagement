FROM gradle:jdk17 as builder
COPY --chown=gradle:gradle . /app
RUN apt-get update && apt-get install --no-install-recommends -y xorg libgl1-mesa-glx
WORKDIR /app
RUN gradle build --no-daemon
CMD gradle run