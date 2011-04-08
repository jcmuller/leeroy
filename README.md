## Leeroy for Jenkins CI (and sometimes Hudson)
### Post a meme in campfire when the build fails (or doesn't)

Leeroy takes no prisoners. Leeroy charges ahead and sounds the alarm in your
campfire room when the build fails. Leeroy knows whodunnit. Leeroy even goes to
[memegenerator.net](http://memegenerator.net) for you and makes a picture [like
this](http://flic.kr/p/9ky6JM).

### Installation 

You can download [leeroy.hpi](/downloads/jcmuller/leeroy/leeroy.hpi) from the downloads section and then copy it to `/var/lib/hudson/plugins/` on
your Jenkins/Hudson server.

Then either use the advanced tab of the plugin manager to upload the hpi file or
just copy it to the plugins directory, e.g. 

    cp target/leeroy.hpi /var/lib/jenkins/plugins/

Finally, restart jenkins (note: not reload configuration, restart the jenkins
daemon).

### Configuration

Click on "Manage Jenkins" link on the home page. Scroll down to "Global Leeroy Settings".

Enter your campfire settings (subdomain, API Token and the room you want Leeroy
to speak in). Now, you can have some fun choosing Memes that will let you know
when your builds succeed and fail.

Go to [memegenerator.net](http://memegenerator.net) and click on a meme that
strikes your fancy. Like [Chuck Norris](http://memegenerator.net/chuck-norris).
Enter something in the caption fields and click "Caption". 

A generated URL can look like
`http://memegenerator.net/chuck-norris/ImageMacro/7037674/Hello-World`

What you need to get is the `chuck-norris` (generator name) and `7037674`
(template id). Enter those into the respective fields in the configuration page.

We assume that you'll use an `AdviceDogSpinoff` template rather than
[vertical](http://memegenerator.net/The-Rock-driving/ImageMacro/7037786/a-b),
since it is what we use.

### Building

If you want to build this package, you'll need to have JDK 6 and maven2
installed to build the plugin. This should be as simple as asking your package
manager to install maven2, e.g.

    brew install maven

Then clone the repository and build the package

    git clone https://github.com/jcmuller/leeroy.git 
    cd leeroy 
    mvn package

When the build has completed, you'll find a `leeroy.hpi` file in the target
directory, which needs to be uploaded to your Jenkins installation. If you
already have a leeroy plugin installed, you need to delete the hpi file and the
uncompressed directory, e.g.

    rm -rf /var/lib/jenkins/plugins/leeroy*

Then either use the advanced tab of the plugin manager to upload the hpi file or
just copy it to the plugins directory, e.g. 

    cp target/leeroy.hpi /var/lib/jenkins/plugins/

Finally, restart jenkins (note: not reload configuration, restart the jenkins
daemon).

### Troubleshooting

If you run into problems building the plugin with Maven, make sure Maven is
finding the right jdk...

run `mvn --version` and check the output, if it's not finding jdk 6, make sure you
have jdk 6 installed and make sure that the current jdk symlink points at
version 6.  On OSX, check that
`/System/Library/Frameworks/JavaVM.framework/Versions/1.6 exists` and that
`/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK` points to it.
If not, remove the CurrentJDK symlink and re-create it, pointing at the 1.6
directory. Other *nix users may run into similar issues, the solution should be
the same, just with different paths.

If you get HttpClient or WebClient exceptions, that probably means you've got
some configuration setting wrong (while there is some validation of
configuration settings, it's far from extensive).

### Thanks
* Thanks to Jens Lukowski for creating the original hudson campfire plugin
* Thanks to JGP for continuing development https://github.com/jgp/hudson_campfire_plugin
* Thanks to [ChallengePost](http://challengepost.com) for sponsoring additional development and
supporting open sourcing it from the start.
