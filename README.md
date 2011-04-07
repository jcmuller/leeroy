## Leeroy for Jenkins (and sometimes Hudson)
### Post an image in campfire when the build fails (or doesn't)

Leeroy takes no prisoners. Leeroy charges ahead and sounds the alarm in your campfire room when the build fails. Leeroy knows whodunnit. Leeroy even goes to [memegenerator.net](http://memegenerator.net) for you and makes a picture [like this](http://flic.kr/p/9ky6JM).

### Installation 

You'll need to have JDK 6 and maven2 installed to build the plugin. This should
be as simple as asking your package manager to install maven2, e.g.

    sudo port install maven2

Then clone the repository and build the package

    git clone git://github.com/jgp/hudson_campfire_plugin.git 
    cd hudson_campfire_plugin 
    mvn package

When the build has completed, you'll find a campfire.hpi file in the target
directory, which needs to be uploaded to your Hudson installation. If you
already have a campfire plugin installed, you need to delete it first, e.g.

    rm -rf /var/lib/hudson/plugins/campfire*

Then either use the advanced tab of the plugin manager to upload the hpi file or
just copy it to the plugins directory, e.g. 

    cp target/campfire.hpi /var/lib/hudson/plugins/

Finally, restart hudson (note: not reload configuration, restart the hudson
daemon).

### Troubleshooting

If you run into problems building the plugin with Maven, make sure Maven is
finding the right jdk...

run mvn --version and check the output, if it's not finding jdk 6, make sure you
have jdk 6 installed and make sure that the current jdk symlink points at
version 6.  On OSX, check that
/System/Library/Frameworks/JavaVM.framework/Versions/1.6 exists and that
/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK points to it.
If not, remove the CurrentJDK symlink and re-create it, pointing at the 1.6
directory. Other *nix users may run into similar issues, the solution should be
the same, just with different paths.

If you get HttpClient or WebClient exceptions, that probably means you've got
some configuration setting wrong (while there is some validation of
configuration settings, it's far from extensive).

### Thanks
* Thanks to Jens Lukowski for creating the original plugin
* Thanks to JGP for continuing development https://github.com/jgp/hudson_campfire_plugin
* Thanks to [ChallengePost](http://challengepost.com) for sponsoring development and
supporting open sourcing it from the start.