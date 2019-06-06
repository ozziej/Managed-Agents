drop database if exists managed_agents;
create database managed_agents character set 'utf8' collate 'utf8_general_ci';
#create user 'managed_agents'@'localhost' identified by 'Manag3dAg3nt5#';
#grant all on managed_agents.* to managed_agents@localhost;
#flush privileges;
use managed_agents;


/*
data-source add --name=ManagedAgentsDS --jndi-name=java:jboss/jdbc/managed_agents --driver-name=mysql --connection-url=jdbc:mysql://localhost:3306/managed_agents --user-name=managed_agents --password=Manag3dAg3nt5#
*/
