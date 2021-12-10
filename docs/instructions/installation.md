# How to Install
## Windows
Step 1:
Install Docker Desktop, create an account, and log in via the Docker Desktop app.
To get started, follow this link: https://docs.docker.com/get-docker/

To use docker desktop, you may need to enable Hardware Assisted Virtualization in your systems BIOS.

Step 2:
Download and install xhost at this link:


Step 3:
Open the command prompt and enter this command:
Docker pull ccdrk13/csci540v3

Step 4:
Open xhost and leave it running

Step 5:
In the command prompt, enter this command and your app should be running:
docker run -it --rm -e DISPLAY=host.docker.internal:0.0 ccdrk13/csci540v3
