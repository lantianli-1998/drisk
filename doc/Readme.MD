# 一、部署环境

## Drools规则文件部署
为了提高Drools规则引擎的灵活性，采用Maven仓库作为规则部署容器。  
环境搭建步骤：  
一、Maven服务端配置：  
1、 在Hadoop01域名对应的服务器上，使用Nexus快速搭建Maven私服。部署时注意端口我配置成了8089(nexus默认是8081)  
2、 登录Maven管理页面，选择对应的Maven仓库，将仓库对应的Hosted下的Deployment policy修改为 Allow redeploy(默认是Disable redeploy)  
3、 在Maven服务端添加用户disk-dev 密码为 123qweasd (本地客户端中需要用到这个用户)。给该该用户配置一个新角色，角色具有对应仓库的所有权限(对应的release库和snapshot库。)  
二、客户端配置：  
1、在开发机本地配置houst文件，增加域名配置：
```properties
192.168.65.174 hadoop01
```
> IP地址修改为对应的服务器。  后面的域名可以随意配。  

1、在开发机本地的maven客户端的settings.xml中，用户信息
```xml
    <servers>
        <server>
            <id>drisk-dev</id>
            <username>drisk-dev</username>
            <password>123qweasd</password>
        </server>
        <server>
            <id>drisk-dev-snapshot</id>
            <username>drisk-dev</username>
            <password>123qweasd</password>
        </server>
    </servers>
```
> id需要与drisk-realtime-risk-deals模块中的pom.xml文件中配置的一致。  
> 用户名和密码为Maven仓库中配置的。

# 二、运行环境

# 三、技术要求
