# Surviving-Maps

## Where has the website gone?
As per the posts on Reddit, I was not receiving enough support via the Kofi link to continue paying out of my own pocket. 
However, due to the generosity of other users, we might be able to get back up and running at some point. 

## So what's this?
This is a repository to allow users to run the code themselves on their own computer. This is likely to be a stop gap, but some users might choose to run it locally or modify it if they want to dabble in some coding.

<details>
  <summary>Click me for change log</summary>

### Change Log
Version 0.6: 27/11/2022
* Fixing breakthrough bug in complex
* Updating JavaScript functions for complex/simple
* Changing functionality of Complex Filter to use QueryDSL
* Adding database caching to speed up subsequent launches
</details>

## How do I install it?

### Precursors
So the app needs things installed on your local machine before it will work: Java and Gradle.
0. Some engineering spirit and appetite for risk
1. Java JDK, this can be downloaded from [Oracle](https://www.oracle.com/java/technologies/downloads/)
2. Gradle is a build tool that is used to link to other software libraries. Details on installation can be found [here](https://gradle.org/install/).

#### Verification

To test these have been installed correctly it is easiest to use the terminal (Mac/Linux) or Command Prompt (Windows).
There are plenty of guides for installing these in a lot more detail. 

```shell
java -version
```

This should return with something like:

```shell
java version "17.0.3.1" 2022-04-22 LTS
Java(TM) SE Runtime Environment (build 17.0.3.1+2-LTS-6)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.3.1+2-LTS-6, mixed mode, sharing)
```

Then
```shell
gradle -version
```
This should return with something like:
```shell
------------------------------------------------------------
Gradle 7.4.2
------------------------------------------------------------

Build time:   2022-03-31 15:25:29 UTC
Revision:     540473b8118064efcc264694cbcaa4b677f61041

Kotlin:       1.5.31
Groovy:       3.0.9
Ant:          Apache Ant(TM) version 1.10.11 compiled on July 10 2021
JVM:          18.0.1.1 (Homebrew 18.0.1.1+0)
OS:           Mac OS X 12.6.1 x86_64
```


### Downloading the Code

As should be said with any code run from the internet, there are risks involved. I haven't tested this on any machines
but my own.
This is the point where you are downloading and running software that is unverified and probably contains bugs.
Java's Virtual Machine should prevent these bugs causing system instabilities, but I take no responsibility whatsoever.

The complete code can be downloaded from the green "code" button on
the [github page](https://github.com/trickster-is-weak/Surviving-Maps). You can then choose to download the code as a
zip, or using git if you have that installed. After you have it downloaded and unzipped, open a Terminal/Command Prompt
at this location.

#### Verification

Run the `ls` command in Mac/Linux or `dir` in windows. It should return the contents of the folder like:

```shell
build.gradle	database	gradlew		logs		src
gradle		gradlew.bat	settings.gradle
```

If you do not see `src`, `build.gradle` and `settings.gradle`, you're in the wrong place.

### Running the code

When you first run the code, it will fetch a load of libraries from the internet. This will only happen on the first run, or after an update.
These files are all from [MVN Repository](https://mvnrepository.com). It will then compile and run the code.  

Run the code using:
```shell
gradle run
```

This should start printing a load of stuff out on the screen such as files it's reading, and other logging information.

When it stops, it will probably have something like 

```shell
[INFO ] 2022-11-10 19:53:38.431 [restartedMain] SpringApplication - Started SpringApplication in 10.892 seconds (JVM running for 12.011)
<=======------> 60% EXECUTING [11m 59s]
> :run
```

Which is good... now we can go to our browser and navigate to "http://localhost". This should load the splash page.

#### Ending the run
The code will remain running after you shut the webpage. It can be stopped by closing the terminal window or Control+C.  


## Advanced Settings
If you have something running on port 80 already, it can be changed in the src/main/resources/application.properties file. 
Change the line `server.port=80` to another number, say 8080, the URL will now be "http://localhost:8080".