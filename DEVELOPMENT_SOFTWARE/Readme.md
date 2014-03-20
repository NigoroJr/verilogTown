# Setup guide

## Background info
We were first importing the Git repository that we cloned, but it turned out
that there were some issues setting up our individual environment. So as a
workaround, we decided to create our own libgdx project in a separate place
and then copy the source codes/resources TO that project.

## Required software/plugins
* JDK 1.7.0 or later (Google Plugin for Eclipse supports only >1.7.0)
* Android SDK (API 15 is currently required)
* GWT Designer
* Google Plugin for Eclipse
* SDKs

The last three plugins can be installed from
[http://dl.google.com/eclipse/plugin/4.3 ](http://dl.google.com/eclipse/plugin/4.3)
(change the 4.3 to your version of Eclipse)

## Setting up the project
Follow the setup
[tutorial](https://github.com/libgdx/libgdx/wiki/Project-setup%2C-running-%26-debugging)
to create a new project with the following properties:

* Name: `verilogTown`
* Package: `com.me.myverilogTown`
* Game class: `verilogTown` (need confirmation)
* Uncheck `Generate iOS project`

Use the shell script to copy source codes into the project you just created.

Note that the fixing the GWT error part in the page above only works if you
have GWT Designer installed.

## Error regarding GWT
Refer to
[http://stackoverflow.com/questions/16699301/unresolved-gwt-error-in-libgdx-html-project](http://stackoverflow.com/questions/16699301/unresolved-gwt-error-in-libgdx-html-project)

## Other issues
It seems like you cannot start the setup UI with `java -jar gdx-setup-ui.jar`
if you're using JDK 7. Couldn't start on Gentoo Linux with:

    java version "1.7.0_45"
    OpenJDK Runtime Environment (IcedTea 2.4.3) (Gentoo build 1.7.0_45-b31)
    OpenJDK 64-Bit Server VM (build 24.45-b08, mixed mode)

Changing the java version can be done with

    sudo java-config --set-system-vm icedtea-bin-6

