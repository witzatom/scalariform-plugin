Scalariform IntelliJ IDEA plugin
================================

This plugin [IJ Marketplace link](https://plugins.jetbrains.com/plugin/12996-scalariform-formatter) adds a menu item that allows you to format all project files, or files in current changeset using Scalariform.

After installing the plugin, configure the plugin by adding a .scalariform.conf file to your project root directory. Check the deafault options [here](https://github.com/Tommassino/scalariform-plugin/blob/master/.scalariform.conf).

When editing a Scala file, you will find the reformat options in the Code menu, and there is also a pre-commit hook that reformats changed files before commiting.

Strongly inspired by the now unmaintained plugin by [thesamet](https://github.com/thesamet/scalariform-intellij-plugin).

