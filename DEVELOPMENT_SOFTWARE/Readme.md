# Setup guide

## Background info
We were first importing the Git repository that we cloned, but it turned out
that there were some issues setting up our individual environment. So as a
workaround, we decided to create our own libgdx project in a separate place
and then copy the source codes/resources TO that project.

## Required software/plugins
* JDK 1.7.0 or later (Google Plugin for Eclipse supports only >1.7.0)
* Android SDK
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
