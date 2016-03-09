# Setting JAVA_HOME

## Windows

The easiest approach is to set JAVA_HOME in the global environment. This will only have effect for new terminal windows.

<http://www.robertsindall.co.uk/blog/setting-java-home-variable-in-windows/>

## Mac

You may set JAVA_HOME in each terminal window, before you run the commands that requires JAVA_HOME to be set.
 
<pre>
export JAVA_HOME=$(/usr/libexec/java_home -v1.8)
</pre>


Mac OSX before 10.5 does not have the /usr/libexec/java_home tool. 

<pre>
export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
</pre>

## Linux

Locate your Java installation. You may find it in /usr/java/. 

You may set JAVA_HOME in each terminal window, before you run the commands that requires JAVA_HOME to be set.
 
<pre>
export JAVA_HOME=/path/to/java/installation
</pre>