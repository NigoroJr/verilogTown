# Setup guide

## Background info
We were first directly importing the Git repository that we cloned, but there
were some issues with build path due to different classpaths. Thus, an
approach to first create a project in a temporary directory using the setup
UI and then copy the libraries and the .classpath file was taken instead.
`copy_project.pl` in this directory can be used to do this for Perl v5.14 and
up.

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
* Location: whereever you want

Use `copy_project.pl` to copy the libraries and .classpath files to the local
repository.

Note that the fixing the GWT error part in the page above only works if you
have GWT Designer installed.

## Error regarding GWT
Refer to
[http://stackoverflow.com/questions/16699301/unresolved-gwt-error-in-libgdx-html-project](http://stackoverflow.com/questions/16699301/unresolved-gwt-error-in-libgdx-html-project)

## Configurations
Due to different directory structures between development and deployment,
environment variable `VERILOG_TOWN_DEVELOPMENT` must be set to a non-zero
value when developing with Eclipse. When this environment variable is 0 or
unset, the program will be executed in deployment mode. Run configurations in
the `verilogTown` and `LevelEditor` projects need to be changed.
