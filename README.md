# restmakeover
Code for the Extreme REST Makeover Workshop


## Check out
Check out the repository to a new directory on your own computer. You may use any tool you like, but if you use git from the command line
then this is the command to run:

<pre>
git clone git@github.com:kantega/restmakeover.git
</pre>

## Open the code
Open the code in your favorite editor or IDE.

## Look at the code
The code you are supposed to edit is in plugins/rest. Feel free to look into the other code, but if you touch something it may break. :-)

## Build
Build the project using maven:

<pre>
cd restmakeover
mvn clean install
</pre>

## Start
Start the application server.

The application server will reload the code when you write, so you will never have to restart anything.

NB! Maven requires you to set JAVA_HOME before you start. Take a look into the [JAVA_HOME.md](JAVA_HOME.md) file if you run into problems with this.

<pre>
cd webapps
mvn jetty:run
</pre>

## GO
Open http://localhost:8080 in your web browser and follow the instructions.


