# Going Live

## Oracle Cloud VM

### Create a VM Instance
- Sign in https://cloud.oracle.com => login.eu-zurich ...
- Compute => **Create a VM instance**
    - Instance name: Any name, but with timestamp is recommended
    - Image and shape => Change Image => Select ```Ubuntu 20.04``` 
    - **Export private ssh key** and save it in your users ```.ssh``` directory
    - => Create

### Connect to VM
- Visual Studio Code **Remote Explorer**
    - Add a new **ssh target** like this (see ```<your-user-home\.ssh\config>```)
        - ```User``` is always ```ubuntu```
        - ```HostName``` as listed in oracle cloud vm public address
        - ```IdentityFile```: Path to your private key file for this vm instance
    - Example:
        ```
        Host oravm-my-ssh-target-name
        HostName 152.67.71.54
        User ubuntu
        IdentityFile C:\Users\birgi\.ssh\ssh-key-2022-01-31.key
        ```

    - Connect to this target
- You can also use a **plain ssh** terminal command instead, example:
    ```
    ssh -i C:\Users\birgi\.ssh\ssh-key-2022-01-31.key ubuntu@152.67.71.54
    ```


### VCN (Virtual Cloud Network) Config
- This is the subnet attached to your vm instance
- **Add Security List**
    - There should already be an ingress rule for SSH, which enables TCP traffic from any source to target port 22 on your vm 
    - Add a new **Ingress Rule** for port **80**:
        - Any source: ```0.0.0.0/0```
        - Any source port: ```All```
        - IP protocol: ```TCP```
        - Destination port range: ```80```
- Perhaps also required: Allow port 80 in the local firewall
    - ```/etc/iptables/rules.v4``` => local firewall, ssh 22 is already allowed 
            ```
            -A INPUT -p tcp -m state --state NEW -m tcp --dport 22 -j ACCEPT
            ```
    - Copy this line and change port 22 to the port you want (80, ...)

### Update Ubuntu
```
sudo apt update
sudo apt upgrade
``` 

### Install Docker

- [Install docker with convenience script](https://docs.docker.com/engine/install/ubuntu/#install-using-the-convenience-script)
- Post installation step: Add your user to docker group
    - ```sudo usermod -aG docker ubuntu```
    - Reconnect 
    - Test with ```docker run hello-world```
- [Install docker-compose](https://docs.docker.com/compose/install/)

### Test nginx on port 80

- ```docker run -p 80:80 -d nginx```
- Check if available on localhost: ```curl http://localhost``` resp. ```docker ps```
- Check if available outside through vm firewall (see VM public IP address)

### Docker Desktop (Windows)

- [WSL2 (Windows Subsystem for Linux - Version 2)](https://docs.microsoft.com/en-us/windows/wsl/install)
- [Docker Desktop for Windows](https://docs.docker.com/get-docker/)
